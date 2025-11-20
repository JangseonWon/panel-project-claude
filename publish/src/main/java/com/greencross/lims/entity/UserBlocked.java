package com.greencross.lims.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("INACTIVATE")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class UserBlocked extends User<UserBlocked> {

}
