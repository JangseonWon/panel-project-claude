package com.greencross.lims.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(schema="public", name = "handbook_test")
@Data
@Accessors(fluent = true)
public class Test {
    @Id
    @Column(name="id")
    private String id;
    @Column(name="effective_date")
    private LocalDateTime dateEffective;
    @Column(name="expiry_date")
    private LocalDateTime dateExpiry;
    @Column(name="code")
    private String code;
    @Column(name="labs")
    private String labs;
}
