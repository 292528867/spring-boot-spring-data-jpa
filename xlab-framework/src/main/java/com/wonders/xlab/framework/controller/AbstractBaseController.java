package com.wonders.xlab.framework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonders.xlab.framework.repository.MyRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 15/3/28.
 */
public abstract class AbstractBaseController<T, ID extends Serializable> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract MyRepository<T, ID> getRepository();

    @Autowired
    protected ObjectMapper objectMapper;


    @RequestMapping(value = "query", method = RequestMethod.GET)
    public List<T> findAll(String filters) {
        return findAll(filters, null).getContent();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<T> findAll(String filters, Pageable pageable) {
        Map<String, ?> filterMap = null;
        if (StringUtils.isNotEmpty(filters)) {
            try {
                filterMap = objectMapper.readValue(filters, HashMap.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return getRepository().findAll(filterMap, pageable);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public T findOne(@PathVariable ID id) {
        return getRepository().findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public T add(@RequestBody T entity) {
        return getRepository().save(entity);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public T modify(@RequestBody T entity) {
        return getRepository().save(entity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable ID id) {
        getRepository().delete(id);
    }

}
