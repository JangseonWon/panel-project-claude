package com.greencross.lims.sample;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.entity.RequestRareDisease;
import org.springframework.stereotype.Repository;

@Repository("RequestDAOSample")
public class RequestDAO extends AbstractJpaDAO<RequestRareDisease> {

}
