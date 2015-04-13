package com.wonders.xlab.framework.repository;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

/**
 * Created by wangqiang on 15/3/31.
 */
public class MyRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements MyRepository<T, ID> {

    private final Class<T> domainClass;
    private final EntityManager entityManager;

    public MyRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);

        this.domainClass = domainClass;
        this.entityManager = entityManager;
    }

    @Override
    public List<T> findAll(Map<String, ?> filters) {
        return getQuery(filters, (Pageable) null).getResultList();
    }

    @Override
    public Page<T> findAll(Map<String, ?> filters, Pageable pageable) {

        TypedQuery<T> query = getQuery(filters, pageable);
        return pageable == null ? new PageImpl<T>(query.getResultList()) : readPage(query, pageable, filters);
    }

    protected TypedQuery<T> getQuery(Map<?, ?> filters, Pageable pageable) {
        Sort sort = pageable == null ? null : pageable.getSort();
        return getQuery(filters, sort);
    }

    protected TypedQuery<T> getQuery(Map<?, ?> filters, Sort sort) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(domainClass);

        Root<T> root = applyFiltersToCriteria(filters, query);
        query.select(root);

        if (sort != null) {
            query.orderBy(toOrders(sort, root, builder));
        }
        return entityManager.createQuery(query);
    }

    protected TypedQuery<Long> getCountQuery(Map<?, ?> filters) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<T> root = applyFiltersToCriteria(filters, query);

        if (query.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }

        return entityManager.createQuery(query);
    }

    protected <S> Root<T> applyFiltersToCriteria(Map<?, ?> filters, CriteriaQuery<S> query) {
        Root<T> root = query.from(domainClass);

        if (filters == null) {
            return root;
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<?, ?> entry : filters.entrySet()) {
            String key = (String) entry.getKey();

            String name = StringUtils.substringBefore(key, "_");
            String op = StringUtils.substringAfter(key, "_");

            String[] names = StringUtils.split(name, '.');
            Path path = root.get(names[0]);
            for (int i = 1; i < names.length; i++) {
                path = path.get(names[i]);
            }

            try {
                Predicate predicate = (Predicate) MethodUtils.invokeMethod(
                        cb, op, path, entry.getValue());
                predicates.add(predicate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }

        return root;
    }

    protected Page<T> readPage(TypedQuery<T> query, Pageable pageable, Map<?, ?> filters) {

        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        Long total = QueryUtils.executeCountQuery(getCountQuery(filters));
        List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.<T>emptyList();
        return new PageImpl<T>(content, pageable, total);
    }
}
