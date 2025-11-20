package com.gcgenome.lims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("INACTIVATE")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class UserBlocked extends User<UserBlocked> {

}
