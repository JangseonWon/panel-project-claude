package com.greencross.lims.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("INACTIVATE")
public class UserInactivated extends User<UserInactivated> {

}
