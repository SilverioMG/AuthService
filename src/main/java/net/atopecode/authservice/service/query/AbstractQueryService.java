package net.atopecode.authservice.service.query;

import java.util.Arrays;
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
import net.atopecode.authservice.util.functionalinterfaces.TriFunction;
import net.atopecode.authservice.validation.ValidationMessageLocalized;
import net.atopecode.authservice.validation.Validator;
import net.atopecode.authservice.validation.exceptions.ValidationException;
import net.atopecode.authservice.dto.PageRequestDto.OrderSortValue;


public abstract class AbstractQueryService<TEntity, TFilter extends FilterPageableBase> {
	
	protected enum PredicateLogicComparation { AND, OR }

	public static final int MAX_PAGE_SIZE = 300;
	
	private static final String PAGE_REQUEST_NULL_FIELD_LOG_MESSAGE = "No se ha recibido valor para el campo '%s' en consulta paginada.";
	private static final String PAGE_REQUEST_INVALID_FIELD_VALUE_LOG_MESSAGE = "Valor incorrecto para el campo '%s' en consulta paginada.";
	
	private JpaSpecificationExecutor<TEntity> repository;
	private Validator validatorPageRequest;
	
	protected AbstractQueryService(JpaSpecificationExecutor<TEntity> repository) {
		this.repository = repository;
		this.validatorPageRequest = new Validator();
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
		
		TriFunction<CriteriaBuilder, Predicate, Predicate, Predicate> concatNullablePredicateFunc = null; 
		switch (logicComparation) {
			case AND:
				concatNullablePredicateFunc = this::concatNullablePredicateAnd;
				break;

			case OR:
				concatNullablePredicateFunc = this::concatNullablePredicateOr;
				break;

			default:
				concatNullablePredicateFunc = this::concatNullablePredicateAnd;
				break;
		}
		
		for(Predicate p: predicates) {
			if(p != null) {
				predicate = concatNullablePredicateFunc.apply(builder, predicate, p);
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
				new ValidationException(String.format(PAGE_REQUEST_NULL_FIELD_LOG_MESSAGE, PageRequestDtoFieldNames.PAGE_NUMBER),
						ValidationMessageLocalized.forNotNullValue(PageRequestDtoFieldNames.PAGE_NUMBER)));
		
		validatorPageRequest.notNull(pageRequest.getPageSize(), 
				new ValidationException(String.format(PAGE_REQUEST_NULL_FIELD_LOG_MESSAGE, PageRequestDtoFieldNames.PAGE_SIZE),
						ValidationMessageLocalized.forNotNullValue(PageRequestDtoFieldNames.PAGE_SIZE)));
				
		if(pageRequest.getPageSize() > MAX_PAGE_SIZE) {
			pageRequest.setPageSize(MAX_PAGE_SIZE);
		}
		
		validatorPageRequest.notNull(pageRequest.getOrderSort(), 
				new ValidationException(String.format(PAGE_REQUEST_NULL_FIELD_LOG_MESSAGE, PageRequestDtoFieldNames.ORDER_SORT),
						ValidationMessageLocalized.forNotNullValue(PageRequestDtoFieldNames.ORDER_SORT)));

		OrderSortValue orderSortValue = pageRequest.getOrderSort();
		validatorPageRequest.valueIn(orderSortValue, PageRequestDto.getOrderSortValues(), 
				new ValidationException(String.format(PAGE_REQUEST_INVALID_FIELD_VALUE_LOG_MESSAGE, PageRequestDtoFieldNames.ORDER_SORT),
						ValidationMessageLocalized.forValueIn(PageRequestDtoFieldNames.ORDER_SORT, orderSortValue.toString(), PageRequestDto.getOrderSortValuesString()))); 	
		
		validatorPageRequest.notNull(pageRequest.getSortFieldName(),
				new ValidationException(String.format(PAGE_REQUEST_NULL_FIELD_LOG_MESSAGE, PageRequestDtoFieldNames.SORT_FIELD_NAME),
						ValidationMessageLocalized.forNotNullValue(PageRequestDtoFieldNames.SORT_FIELD_NAME)));
	}
}
