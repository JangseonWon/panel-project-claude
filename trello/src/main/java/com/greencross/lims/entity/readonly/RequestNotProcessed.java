package com.greencross.lims.entity.readonly;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "panel", name = "request_not_processed")
public class RequestNotProcessed extends Request {
}
