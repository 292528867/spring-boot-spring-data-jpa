package com.wonders.xlab.framework.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 15/3/28.
 */
@NoRepositoryBean
public interface MyRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    T find(Map<String, ?> filters);

    List<T> findAll(Map<String, ?> filters);

    List<T> findAll(Map<String, ?> filters, Sort sort);

    Page<T> findAll(Map<String, ?> filters, Pageable pageable);

}
