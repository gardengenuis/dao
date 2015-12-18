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
package org.garden.dao;

import java.util.List;
import java.util.Map;

import org.garden.utils.Pager;

/**
 * 
 * IDAO.java
 *
 * @author Garden
 * create on 2014年9月4日 下午4:06:54
 */
public interface IDAO<T> {
	public T findById(Class<T> clz, Long id);
	public T findById(Class<T> clz, Object id);
	public Long save(T obj);
	public void saveOrUpdate(T obj);
	public List<T> findAll(Class<T> clz);
	public void delete(T obj);
	public void deleteAll(Class<T> clz);
	public List<Object> findObject(String tableName, String[] columnNames,String state);
	public List<T> findByHql(String hql, List<Map<String, Object>> states);
	public List<T> findByHql(String hql, List<Map<String, Object>> states, Pager pager);
	public long findCount(String hql, List<Map<String, Object>> states);
	public List<T> findAll(Class<T> clz, Pager pager);
	public List<Object> findByHql(String hql, Pager pager);
	public List<Object> findBySql(String sql, Pager pager);
	public List<Object> findBySql(String sql);
	public int getTotalCountBySQL(final String sqlQuery);
	public int getTotalCountByHQL(final String hqlQuery);
	public int getTotalCountByHQL(final String hqlQuery, List<Map<String, Object>> states);
	public List<Object> findListByHQL(String hql, List<Map<String, Object>> states);
	public void excuteHql(String hql, List<Map<String, Object>> states);
	public void excuteSql(String sql);
}

