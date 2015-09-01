package com.wonders.xlab.framework.entity;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangqiang on 15/8/31.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractBaseEntity<PK extends Serializable> extends AbstractPersistable<PK> {

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    public DateTime getCreatedDate() {
        return null == createdDate ? null : new DateTime(createdDate);
    }

    public void setCreatedDate(final DateTime createdDate) {
        this.createdDate = null == createdDate ? null : createdDate.toDate();
    }

    public DateTime getLastModifiedDate() {
        return null == lastModifiedDate ? null : new DateTime(lastModifiedDate);
    }

    public void setLastModifiedDate(final DateTime lastModifiedDate) {
        this.lastModifiedDate = null == lastModifiedDate ? null : lastModifiedDate.toDate();
    }

}
