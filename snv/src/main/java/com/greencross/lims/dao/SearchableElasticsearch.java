package com.greencross.lims.dao;

import com.greencross.lims.dto.QueryServerside;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;

import java.util.List;
import java.util.stream.Collectors;

public interface SearchableElasticsearch {
    AbstractElasticsearchDAO.CriteriaBuilderAbstract criteria(String suffix);
    default long count(String suffix, QueryServerside query) {
        if(query.filters()==null || query.filters().isEmpty()) query.filters(List.of());
        AbstractElasticsearchDAO.CriteriaBuilderAbstract cb = query.filters().stream().collect(()->criteria(suffix), (c, f)->where(suffix, c, f), AbstractElasticsearchDAO.CriteriaBuilderAbstract::and);
        return cb.count();
    }
    default SearchHits<Document> list(String suffix, QueryServerside query, Pageable pageable) {
        if(query.filters()==null || query.filters().isEmpty()) return criteria(suffix).pageable(pageable).search();
        AbstractElasticsearchDAO.CriteriaBuilderAbstract cb = query.filters().stream().collect(()->criteria(suffix), (c, f)->where(suffix, c, f), AbstractElasticsearchDAO.CriteriaBuilderAbstract::and);
        return cb.pageable(pageable).search();
    }
    default Page<SearchHit<Document>> search(String suffix, QueryServerside query) {
        assert query != null;
        Sort sort = query.sortBy()!=null? Sort.by(query.asc()? Sort.Direction.ASC: Sort.Direction.DESC, query.sortBy()):null;
        Pageable pageable = PageRequest.of(query.page(), query.limit(), sort);
        return new PageImpl<>(list(suffix, query, pageable).stream().collect(Collectors.toList()), pageable, count(suffix, query));
    }
    void where(String suffix, AbstractElasticsearchDAO.CriteriaBuilderAbstract cb, QueryServerside.Filter filter);
}
