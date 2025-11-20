package com.greencross.lims.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(schema="panel",name = "panel_type")
@Getter
@Setter
@Accessors(fluent = true)
public class PanelType {
    @Id
    Long id;
    String panel;
    String type;
    String service;
    @Column(name="effective_date") LocalDateTime effectiveDate;
    @Column(name="expiration_date") LocalDateTime expirationDate;
}
