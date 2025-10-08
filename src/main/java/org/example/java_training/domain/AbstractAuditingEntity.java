package org.example.java_training.domain;

import jakarta.persistence.Column;

import jakarta.persistence.EntityListeners;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 4453627901375825468L;

    @CreatedDate
    @Column(name = "created", nullable = false)
    private Instant created = Instant.now();

    @LastModifiedDate
    @Column(name = "modified", nullable = false)
    private Instant modified = Instant.now();

    @Column(name = "deleted")
    private Instant deleted;
}
