package com.greencross.lims.service;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.entity.Interpretation;
import org.springframework.stereotype.Repository;

@Repository("in")
public class InterpretationDAO extends AbstractJpaDAO<Interpretation> {
}
