package com.greencross.lims.dao;

import com.greencross.lims.dto.QueryServerside;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;
import java.util.stream.Collectors;

public interface SearchableElasticsearch {
    AbstractElasticsearchDAO.CriteriaBuilderAbstract criteria();
    default long count(QueryServerside query) {
        if(query.filters()==null || query.filters().isEmpty()) query.filters(List.of());
        AbstractElasticsearchDAO.CriteriaBuilderAbstract cb = query.filters().stream().collect(this::criteria, this::where, AbstractElasticsearchDAO.CriteriaBuilderAbstract::and);
        return cb.count();
    }
    default SearchHits<Object> list(QueryServerside query, Pageable pageable) {
        if(query.filters()==null || query.filters().isEmpty()) return criteria().pageable(pageable).search();
        AbstractElasticsearchDAO.CriteriaBuilderAbstract cb = query.filters().stream().collect(this::criteria, this::where, AbstractElasticsearchDAO.CriteriaBuilderAbstract::and);
        return cb.pageable(pageable).search();
    }
    default Page<SearchHit<Object>> search(QueryServerside query) {
        assert query != null;
        Sort sort = query.sortBy()!=null? Sort.by(query.asc()? Sort.Direction.ASC: Sort.Direction.DESC, query.sortBy()):null;
        Pageable pageable = PageRequest.of(query.page(), query.limit(), sort);
        return new PageImpl<>(list(query, pageable).stream().collect(Collectors.toList()), pageable, count(query));
    }
    void where(AbstractElasticsearchDAO.CriteriaBuilderAbstract cb, QueryServerside.Filter filter);
}
