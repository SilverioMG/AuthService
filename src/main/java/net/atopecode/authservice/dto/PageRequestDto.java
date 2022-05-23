package net.atopecode.authservice.dto;

import java.util.List;
import java.util.stream.Collectors;

import net.atopecode.authservice.dto.PageRequestDto.OrderSortValue;

/**
 * Dto con parámetros para búsquedas paginadas.
 * @author Silverio
 *
 */
public class PageRequestDto {

	public enum OrderSortValue { ASC, DESC }
	
	private Integer pageSize;
	
	private Integer pageNumber;
	
	private String sortFieldName;
	
	private OrderSortValue orderSort;

	public PageRequestDto(Integer pageSize, Integer pageNumber, String sortFieldName, OrderSortValue orderSort) {
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
		this.sortFieldName = sortFieldName;
		this.orderSort = orderSort;
	}


	public Integer getPageSize() {
		return pageSize;
	}



	public Integer getPageNumber() {
		return pageNumber;
	}



	public String getSortFieldName() {
		return sortFieldName;
	}



	public OrderSortValue getOrderSort() {
		return orderSort;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public static List<OrderSortValue> getOrderSortValues(){
		return List.of(OrderSortValue.values());
	}
	
	public static List<String> getOrderSortValuesString(){
		return getOrderSortValues().stream()
				.map(Enum::toString)
				.collect(Collectors.toList());
	}
	
	@Override
	public String toString() {
		return "PageRequestDto [pageSize=" + pageSize + ", pageNumber=" + pageNumber + ", sortFieldName="
				+ sortFieldName + ", orderSort=" + orderSort + "]";
	}
	
}
