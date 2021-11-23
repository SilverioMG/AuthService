package net.atopecode.authservice.service.query;

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
import net.atopecode.authservice.dto.PageRequestDtoFieldNames;
import net.atopecode.authservice.dto.PageRequestDto.OrderSortValue;
import net.atopecode.authservice.validators.base.ValidatorEntity;
import net.atopecode.authservice.validators.exception.ValidationException;

public abstract class AbstractQueryService<TEntity, TFilter extends FilterPageableBase> {
	
	protected enum PredicateLogicComparation { AND, OR }

	public static final int MAX_PAGE_SIZE = 300;
	
	private static final String PAGE_REQUEST_NULL_FIELD_LOG_MESSAGE = "No se ha recibido valor para el campo '%s' en consulta paginada.";
	private static final String PAGE_REQUEST_INVALID_FIELD_VALUE_LOG_MESSAGE = "Valor incorrecto para el campo '%s' en consulta paginada.";
	
	private JpaSpecificationExecutor<TEntity> repository;
	private ValidatorEntity<PageRequestDto> validatorPageRequest;
	
	protected AbstractQueryService(JpaSpecificationExecutor<TEntity> repository) {
		this.repository = repository;
		this.validatorPageRequest = new ValidatorEntity<>(PageRequestDto.class);
	}
	
	
	public Page<TEntity> query(TFilter filter) throws ValidationException {
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
		
		//TODO... Cambiar el 'switch case' para que en devuelva una expresión lambda y se llame en cada iteración del bucle 'for' en vez de hacer el 'switch case' por cada iteración.
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
	
	protected PageRequest getFilterPageRequest(TFilter filter) throws ValidationException {
		PageRequest pageRequest = null;
		PageRequestDto pageRequestDto = filter.getPageRequest();
		if(pageRequestDto != null) {
			validatePageRequest(pageRequestDto);
			pageRequest = PageRequest.of(pageRequestDto.getPageNumber(), pageRequestDto.getPageSize(), 
					Sort.Direction.fromOptionalString(pageRequestDto.getOrderSort().toString()).orElse(Sort.Direction.ASC), 
					pageRequestDto.getSortFieldName());
		}
		
		return pageRequest;
	}
	
	protected void validatePageRequest(PageRequestDto pageRequest) throws ValidationException {
		validatorPageRequest.notNull(pageRequest.getPageNumber(), 
				String.format(PAGE_REQUEST_NULL_FIELD_LOG_MESSAGE, PageRequestDtoFieldNames.PAGE_NUMBER), 
				PageRequestDtoFieldNames.PAGE_NUMBER);
		
		validatorPageRequest.notNull(pageRequest.getPageSize(), 
				String.format(PAGE_REQUEST_NULL_FIELD_LOG_MESSAGE, PageRequestDtoFieldNames.PAGE_SIZE), 
				PageRequestDtoFieldNames.PAGE_SIZE);
		if(pageRequest.getPageSize() > MAX_PAGE_SIZE) {
			pageRequest.setPageSize(MAX_PAGE_SIZE);
		}
		
		validatorPageRequest.notNull(pageRequest.getOrderSort(), 
				String.format(PAGE_REQUEST_NULL_FIELD_LOG_MESSAGE, PageRequestDtoFieldNames.ORDER_SORT), 
				PageRequestDtoFieldNames.ORDER_SORT);
		validatorPageRequest.valueIn(pageRequest.getOrderSort(), 
				List.of(OrderSortValue.values()), 
				String.format(PAGE_REQUEST_INVALID_FIELD_VALUE_LOG_MESSAGE, PageRequestDtoFieldNames.ORDER_SORT), 
				PageRequestDtoFieldNames.ORDER_SORT);
		
		validatorPageRequest.notNull(pageRequest.getSortFieldName(), 
				String.format(PAGE_REQUEST_NULL_FIELD_LOG_MESSAGE, PageRequestDtoFieldNames.SORT_FIELD_NAME), 
				PageRequestDtoFieldNames.SORT_FIELD_NAME);
	}
}
