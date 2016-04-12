/*
 * Copyright (c) 2004, 2014, Garden Lee. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 
package org.garden.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.garden.dao.IDAO;
import org.garden.utils.Pager;
import org.garden.utils.PaginationUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;

/**
 * 
 * DAO.java
 *
 * @author Garden
 * create on 2014年9月4日 下午4:06:46
 */
public class DAO<T> implements IDAO<T> {

	private Log log = LogFactory.getLog(DAO.class);
	
	protected SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    } 
	/* (non-Javadoc)
	 * @see org.garden.sysadmin.dao.IDAO#save(java.lang.Class)
	 */
	@Override
	public Long save(T obj) {
		Session session = this.sessionFactory.getCurrentSession();
		Serializable rlt = session.save(obj);
		
		if ( rlt instanceof Long) {
			Long id = (Long)rlt;
			return id;
		} else {
			return null;
		}
	}
	/* (non-Javadoc)
	 * @see org.garden.sysadmin.dao.IDAO#saveOrUpdate(java.lang.Object)
	 */
	@Override
	public void saveOrUpdate(T obj) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(obj);
	}
	/* (non-Javadoc)
	 * @see org.garden.sysadmin.dao.IDAO#findAllObjects()
	 */
	@Override
	public List<T> findAll(Class<T> clz) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from " + clz.getName());
		return query.list();
	}
	/* (non-Javadoc)
	 * @see org.garden.sysadmin.dao.IDAO#delete(java.lang.Object)
	 */
	@Override
	public void delete(T obj) {
		this.sessionFactory.getCurrentSession().delete(obj);
	}
	/* (non-Javadoc)
	 * @see org.garden.sysadmin.dao.IDAO#deleteAll()
	 */
	@Override
	public void deleteAll(Class<T> clz) {
		String hql = "delete from " + clz.getName();
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		int rlt = query.executeUpdate();
		log.debug("remove " + clz.getName() + " total [" + rlt + "] records");
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findById(java.lang.Long)
	 */
	@Override
	public T findById(Class<T> clz, Long id) {
		return (T)this.sessionFactory.getCurrentSession().get(clz, id);
	}

	@Override
	public List<T> findByHql(String hql, List<Map<String, Object>> states) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		for ( Map<String, Object> state : states) {
			for ( String key : state.keySet()) {
				Object value = state.get(key);
				
				if ( value instanceof List) {
					query.setParameterList(key, (List<?>) value);
				} else {
					query.setParameter(key, value);
				}
			}
		}
		
		return query.list();
	}
	
	@Override
	public List<T> findByHql(String hql, List<Map<String, Object>> states, Pager pager) {
		
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		
		// 增加查询条件
		if ( !states.isEmpty()) {
			for ( Map<String, Object> state : states) {
				for ( String key : state.keySet()) {
					Object value = state.get(key);
					
					if ( value instanceof List) {
						query.setParameterList(key, (List<?>) value);
					} else {
						query.setParameter(key, value);
					}
				}
			}
		}
			
		// 获取总记录数 -- 目前查询出所有记录,需要改进
		int totalCount = getTotalCountByHQL(hql, states);
		pager.setTotalItems(totalCount);
		if (pager != null && pager.getItemsPerPage() > 0) {
			pager.calc();

			query.setFirstResult(pager.getFirstResult());
			query.setMaxResults(pager.getItemsPerPage());
		}

		return query.list();
	}
	
	@Override
	public void excuteHql(String hql, List<Map<String, Object>> states) {
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		
		if ( !states.isEmpty()) {
			for ( Map<String, Object> state : states) {
				for ( String key : state.keySet()) {
					Object value = state.get(key);
					
					if ( value instanceof List) {
						query.setParameterList(key, (List<?>) value);
					} else {
						query.setParameter(key, value);
					}
				}
			}
			
			query.executeUpdate();
		} else {
			log.warn("cannot excute hql with no statements");
		}
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findObject(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Object> findObject(String tableName, String[] columnNames, String state) {
		String columnStr = "";
		if (columnNames == null) {
			columnStr = "*";
		} else {
			for ( int i=0; i<columnNames.length; i++) {
				columnStr += columnNames[i];
				if ( i < columnNames.length - 1) {
					columnStr += ",";
				}
			}
		}

		String sql = "select " + columnStr + " from " + tableName + " where " + state;
		SQLQuery query = this.sessionFactory.getCurrentSession().createSQLQuery(sql);

		return query.list();
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findCount(java.lang.String, java.util.List)
	 */
	@Override
	public long findCount(String hql, List<Map<String, Object>> states) {
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		
		if ( !states.isEmpty()) {
			for ( Map<String, Object> state : states) {
				for ( String key : state.keySet()) {
					Object value = state.get(key);
					
					if ( value instanceof List) {
						query.setParameterList(key, (List<?>) value);
					} else {
						query.setParameter(key, value);
					}
				}
			}
			
			return ((Long)query.uniqueResult()).longValue();
		} else {
			log.warn("cannot excute hql with no statements");
		}
		
		return 0;
	}

	/**
	 * 根据查询语句HQL查询数据库并返回结果的记录总数
	 *
	 * @param hqlQuery 指定查询语句
	 * @return 返回记录总数
	 */
	@Override
	public int getTotalCountByHQL(final String hqlQuery) {
		int totalCount = 0;
		Query query = null;

		query = this.sessionFactory.getCurrentSession().createQuery(PaginationUtils.getCountString(hqlQuery)); // 查询符合条件数据
		List list = query.list();
		if (!list.isEmpty()) {
			totalCount = Integer.parseInt(list.get(0).toString());
		}
		return new Integer(totalCount);
	}
	
	@Override
	public int getTotalCountByHQL(final String hqlQuery, List<Map<String, Object>> states) {
		return findByHql( hqlQuery, states).size();
	}
	
	/**
	 * 根据查询语句SQL查询数据库并返回结果的记录总数
	 *
	 * @param sqlQuery 指定查询语句
	 * @return 返回记录总数
	 */
	@Override
	public int getTotalCountBySQL(final String sqlQuery) {
		int totalCount = 0;
		Query query = null;

		query = this.sessionFactory.getCurrentSession().createSQLQuery(PaginationUtils.getCountString(sqlQuery)); // 查询符合条件数据
		List list = query.list();
		if (!list.isEmpty()) {
			totalCount = Integer.parseInt(list.get(0).toString());
		}
		return new Integer(totalCount);
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findByHql(java.lang.String, org.garden.util.Pager)
	 */
	@Override
	public List<Object> findByHql(String hql, Pager pager) {
		log.debug(hql);
		long startTime = System.currentTimeMillis();
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		
		// 获取总记录数 -- 目前查询出所有记录,需要改进
		int totalCount = getTotalCountByHQL(hql);
		pager.setTotalItems(totalCount);
		if (pager != null && pager.getItemsPerPage() > 0) {
			pager.calc();

			query.setFirstResult(pager.getFirstResult());
			query.setMaxResults(pager.getItemsPerPage());
		}

		List items = query.list();
		log.debug("数据库查询耗时:" + (System.currentTimeMillis() - startTime));
		return items;
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findBySql(java.lang.String, org.garden.util.Pager)
	 */
	@Override
	public List<Object> findBySql(String sql, Pager pager) {
		log.debug(sql);
		long startTime = System.currentTimeMillis();
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		
		// 获取总记录数 -- 目前查询出所有记录,需要改进
		int totalCount = getTotalCountBySQL(sql);
		pager.setTotalItems(totalCount);
		if (pager != null && pager.getItemsPerPage() > 0) {
			pager.calc();

			query.setFirstResult(pager.getFirstResult());
			query.setMaxResults(pager.getItemsPerPage());
		}

		List items = query.list();
		log.debug("数据库查询耗时:" + (System.currentTimeMillis() - startTime));
		return items;
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findAll(java.lang.Class, org.garden.util.Pager)
	 */
	@Override
	public List<T> findAll(Class<T> clz, Pager pager) {
		Criteria criteria =this.sessionFactory.getCurrentSession().createCriteria(clz);
		
		pager.setTotalItems( ((Long) (criteria.setProjection(Projections.rowCount()).uniqueResult())).intValue()); //获取条件查询总数Count
		criteria.setProjection(null);

		pager.calc();

		criteria.setFirstResult(pager.getFirstResult());
		criteria.setMaxResults(pager.getItemsPerPage());
		
		return criteria.list();
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findById(java.lang.Class, java.lang.Object)
	 */
	@Override
	public T findById(Class<T> clz, Object id) {
		return (T)this.sessionFactory.getCurrentSession().get(clz, (Serializable) id);
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findBySql(java.lang.String)
	 */
	@Override
	public List findBySql(String sql) {
		return this.sessionFactory.getCurrentSession().createSQLQuery(sql).list();
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findListByHQL(java.lang.String, java.util.List)
	 */
	@Override
	public List<Object> findListByHQL(String hql, List<Map<String, Object>> states) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		for ( Map<String, Object> state : states) {
			for ( String key : state.keySet()) {
				Object value = state.get(key);
				
				if ( value instanceof List) {
					query.setParameterList(key, (List<?>) value);
				} else {
					query.setParameter(key, value);
				}
			}
		}
		
		return query.list();
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#excuteSql(java.lang.String, java.util.List)
	 */
	@Override
	public void excuteSql(String sql) {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		
		query.executeUpdate();
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#update(java.lang.Object)
	 */
	@Override
	public void update(T obj) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(obj);
	}
}
