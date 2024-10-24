package com.java.product.parser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {

    @CreationTimestamp
    @Column(nullable = false, name = "CREATED_DATE")
    private Date createdDate;


    @UpdateTimestamp
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;


    @Version
    private Long version;
}
