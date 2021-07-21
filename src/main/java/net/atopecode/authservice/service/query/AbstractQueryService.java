package net.atopecode.authservice.service.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.atopecode.authservice.dto.FilterPageableBase;
import net.atopecode.authservice.dto.PageRequestDto;

public abstract class AbstractQueryService<TEntity> {
	
	protected enum PredicateLogicComparation { AND, OR };

	private JpaSpecificationExecutor<TEntity> repository;
	
	public AbstractQueryService(JpaSpecificationExecutor<TEntity> repository) {
		this.repository = repository;
	}
	
	
	public <TFilter extends FilterPageableBase> List<TEntity> query(TFilter filter) {
		List<TEntity> result = new ArrayList<>();
		Specification<TEntity> specification = getFilterSpecification(filter);
		PageRequest pageRequest = getFilterPageRequest(filter);
		
		if(pageRequest == null) {
			result = repository.findAll(specification);
		}
		else {
			Page<TEntity> pageResult = repository.findAll(specification, pageRequest);
			result = pageResult.getContent();
		}
		
		return result;
	}
	
	protected abstract <TFilter extends FilterPageableBase> Specification<TEntity> getFilterSpecification(TFilter filter);
	
	protected Predicate composeNullablePredicates(CriteriaBuilder builder, PredicateLogicComparation logicComparation, Predicate[] predicates) {
		Predicate predicate = null;
		if(predicates == null) {
			return predicate;
		}
		
		for(Predicate p: predicates) {
			if(p != null) {
				switch(logicComparation) {
					case AND:
						predicate = concatNullablePredicateAnd(builder, predicate, p);
					break;
					
					case OR:
						predicate = concatNullablePredicateOr(builder, predicate, p);
					break;
					
					default:
						predicate = concatNullablePredicateAnd(builder, predicate, p);
					break;
				}
			}
		}
		
		return predicate;
	}
	
	private Predicate concatNullablePredicateAnd(CriteriaBuilder builder, Predicate predicateResult, Predicate predicateToConcat) {
		return (predicateResult == null) ? predicateToConcat : builder.and(predicateResult, predicateToConcat);
	}
	
	private Predicate concatNullablePredicateOr(CriteriaBuilder builder, Predicate predicateResult, Predicate predicateToConcat) {
		return (predicateResult == null) ? predicateToConcat : builder.or(predicateResult, predicateToConcat);
	}
	
	protected <TFilter extends FilterPageableBase> PageRequest getFilterPageRequest(TFilter filter) {
		PageRequest pageRequest = null;
		PageRequestDto pageRequestDto = filter.getPageRequest();
		if(pageRequestDto != null) {	
			pageRequest = PageRequest.of(pageRequestDto.getPageNumber(), pageRequestDto.getPageSize(), 
					Sort.Direction.fromOptionalString(pageRequestDto.getOrderSort().toString()).orElse(Sort.Direction.ASC), 
					pageRequestDto.getSortFieldName());
		}
		
		return pageRequest;
	}
}
