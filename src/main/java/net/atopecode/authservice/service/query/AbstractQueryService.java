package net.atopecode.authservice.service.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.atopecode.authservice.dto.FilterPageableBase;
import net.atopecode.authservice.dto.PageRequestDto;

public abstract class AbstractQueryService<TEntity, TFilter extends FilterPageableBase> {
	
	protected enum PredicateLogicComparation { AND, OR }

	private JpaSpecificationExecutor<TEntity> repository;
	
	protected AbstractQueryService(JpaSpecificationExecutor<TEntity> repository) {
		this.repository = repository;
	}
	
	
	public Page<TEntity> query(TFilter filter) {
		Page<TEntity> result = Page.empty();
		Specification<TEntity> specification = getFilterSpecification(filter);
		PageRequest pageRequest = getFilterPageRequest(filter);
		
		if(pageRequest == null) {
			//Consulta sin paginación:
			List<TEntity> entities = repository.findAll(specification);
			result = new PageImpl<TEntity>(entities, Pageable.unpaged(), entities.size());
		}
		else {
			//Consulta paginada:
			Page<TEntity> pageResult = repository.findAll(specification, pageRequest);
			result = pageResult;
		}

		return result;
	}
	
	protected abstract Specification<TEntity> getFilterSpecification(TFilter filter);
	
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
	
	protected PageRequest getFilterPageRequest(TFilter filter) {
		PageRequest pageRequest = null;
		PageRequestDto pageRequestDto = filter.getPageRequest();
		if(pageRequestDto != null) {
			//TODO... Validar que todos los campos de 'pageRequestDto' tengan valor, en caso contrario lanzar una 'ValidationException'.
			pageRequest = PageRequest.of(pageRequestDto.getPageNumber(), pageRequestDto.getPageSize(), 
					Sort.Direction.fromOptionalString(pageRequestDto.getOrderSort().toString()).orElse(Sort.Direction.ASC), 
					pageRequestDto.getSortFieldName());
		}
		
		return pageRequest;
	}
}
