package com.rose.lzk.service;

import com.github.pagehelper.PageInfo;
import com.rose.lzk.entity.domain.BaseDomain;

/**
 * 基于basemapper实现统一的crud，可封装一些domian的相同操作
 * @param <T>
 */
public interface BaseService<T extends BaseDomain>{
  int insert(T t, String createBy);

  int delete(T t);

  int update(T t, String updateBy);

  int count(T t);

  T selectOne(T t);

  PageInfo<T> page(int pageNum, int pageSize, T t);
}
