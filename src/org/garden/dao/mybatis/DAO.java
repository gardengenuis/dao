/*
 * Copyright (c) 2004, 2015, Garden Lee. All rights reserved.
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
package org.garden.dao.mybatis;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.garden.dao.IDAO;
import org.garden.utils.Pager;
import org.mybatis.spring.SqlSessionTemplate;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/** 
* @ClassName: DAO 
* @Description: TODO
* @author Garden Lee
* @date 2016年4月8日 下午4:51:25 
*/
public class DAO<T> implements IDAO<T> {
	private Log log = LogFactory.getLog(DAO.class);
	
	protected SqlSessionTemplate sqlSessionTemplate;
	
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findById(java.lang.Class, java.lang.Long)
	 */
	@Override
	public T findById(Class<T> clz, Long id) {
		return this.sqlSessionTemplate.selectOne(clz.getSimpleName() + "." + "findById", id);
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findById(java.lang.Class, java.lang.Object)
	 */
	@Override
	public T findById(Class<T> clz, Object id) {
		return this.sqlSessionTemplate.selectOne(clz.getSimpleName() + "." + "findById", id);
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#save(java.lang.Object)
	 */
	@Override
	public Long save(T obj) {
		return Long.valueOf(this.sqlSessionTemplate.insert(obj.getClass().getSimpleName() + ".insert", obj));
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#saveOrUpdate(java.lang.Object)
	 */
	@Override
	public void saveOrUpdate(T obj) {
		log.error("MyBatis do not support saveOrUpdate method...");
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findAll(java.lang.Class)
	 */
	@Override
	public List<T> findAll(Class<T> clz) {
		return this.sqlSessionTemplate.selectList(clz.getSimpleName() + ".query");
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#delete(java.lang.Object)
	 */
	@Override
	public void delete(T obj) {
		this.sqlSessionTemplate.delete(obj.getClass().getSimpleName() + ".delete", obj);
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#deleteAll(java.lang.Class)
	 */
	@Override
	public void deleteAll(Class<T> clz) {
		this.sqlSessionTemplate.delete(clz.getSimpleName() + ".deleteAll");
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findObject(java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public List<Object> findObject(String tableName, String[] columnNames, String state) {
		log.error("MyBatis do not support findObject(String tableName, String[] columnNames, String state) method...");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findByHql(java.lang.String, java.util.List)
	 */
	@Override
	public List<T> findByHql(String hql, List<Map<String, Object>> states) {
		log.error("MyBatis do not support findByHql(String hql, List<Map<String, Object>> states) method...");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findByHql(java.lang.String, java.util.List, org.garden.utils.Pager)
	 */
	@Override
	public List<T> findByHql(String hql, List<Map<String, Object>> states, Pager pager) {
		log.error("MyBatis do not support findByHql(String hql, List<Map<String, Object>> states, Pager pager) method...");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findCount(java.lang.String, java.util.List)
	 */
	@Override
	public long findCount(String hql, List<Map<String, Object>> states) {
		log.error("MyBatis do not support findCount(String hql, List<Map<String, Object>> states, Pager pager) method...");
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findAll(java.lang.Class, org.garden.utils.Pager)
	 */
	@Override
	public List<T> findAll(Class<T> clz, Pager pager) {
		PageBounds pageBounds = new PageBounds(pager.getCurPage(), pager.getItemsPerPage());
		return this.sqlSessionTemplate.selectList(clz.getSimpleName() + ".findAll", null, pageBounds);
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findByHql(java.lang.String, org.garden.utils.Pager)
	 */
	@Override
	public List<Object> findByHql(String hql, Pager pager) {
		log.error("MyBatis do not support findByHql(String hql, Pager pager) method...");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findBySql(java.lang.String, org.garden.utils.Pager)
	 */
	@Override
	public List<Object> findBySql(String sql, Pager pager) {
		Class<T> obj = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		PageBounds pageBounds = new PageBounds(pager.getCurPage(), pager.getItemsPerPage());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sql", sql);
		return this.sqlSessionTemplate.selectList(obj.getSimpleName() + ".findBySql", param, pageBounds);
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findBySql(java.lang.String)
	 */
	@Override
	public List<Object> findBySql(String sql) {
		Class<T> obj = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sql", sql);
		return this.sqlSessionTemplate.selectList(obj.getSimpleName() + ".findBySql", param);
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#getTotalCountBySQL(java.lang.String)
	 */
	@Override
	public int getTotalCountBySQL(String sqlQuery) {
		Class<T> obj = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sql", sqlQuery);
		String namespace = obj.getSimpleName();
		Integer rlt =  this.sqlSessionTemplate.selectOne(namespace + ".findBySql", param);
//		return this.sqlSessionTemplate.selectOne(namespace + ".findBySql", param);
		return rlt;
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#getTotalCountByHQL(java.lang.String)
	 */
	@Override
	public int getTotalCountByHQL(String hqlQuery) {
		log.error("MyBatis do not support getTotalCountByHQL(String hqlQuery) method...");
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#getTotalCountByHQL(java.lang.String, java.util.List)
	 */
	@Override
	public int getTotalCountByHQL(String hqlQuery, List<Map<String, Object>> states) {
		log.error("MyBatis do not support getTotalCountByHQL(String hqlQuery, List<Map<String, Object>> states) method...");
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#findListByHQL(java.lang.String, java.util.List)
	 */
	@Override
	public List<Object> findListByHQL(String hql, List<Map<String, Object>> states) {
		log.error("MyBatis do not support findListByHQL(String hql, List<Map<String, Object>> states) method...");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#excuteHql(java.lang.String, java.util.List)
	 */
	@Override
	public void excuteHql(String hql, List<Map<String, Object>> states) {
		log.error("MyBatis do not support excuteHql(String hql, List<Map<String, Object>> states) method...");
	}

	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#excuteSql(java.lang.String)
	 */
	@Override
	public void excuteSql(String sql) {
		log.error("MyBatis do not support excuteSql(String sql) method...");
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#update(java.lang.Object)
	 */
	@Override
	public void update(T obj) {
		this.sqlSessionTemplate.update(obj.getClass().getSimpleName() + ".update", obj);
	}
	/* (non-Javadoc)
	 * @see org.garden.dao.IDAO#update(java.lang.String, java.util.List)
	 */
	@Override
	public void update(String hql, List<Map<String, Object>> states) {
		log.error("MyBatis do not support update(String hql, List<Map<String, Object>> states) method...");
	}

}
