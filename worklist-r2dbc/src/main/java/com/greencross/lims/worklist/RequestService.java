package com.greencross.lims.worklist;

import com.greencross.lims.dto.Query;
import com.greencross.lims.dto.Request;
import com.greencross.lims.repo.RequestRepository;
// import com.greencross.lims.trans.RequestToDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {
	private final RequestRepository dao;
	public RequestService(RequestRepository dao) {
		this.dao = dao;
	}
//	@Transactional(value="transactionManagerLims", readOnly = true)
//	public Page<Request> list(Query query) {
//		return dao.search(query).map(RequestToDTO::map);
//	}
	@Transactional("transactionManagerLims")
	public Request reception(long sample, String service) {
		return null;
	}
}
