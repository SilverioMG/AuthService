package net.atopecode.authservice.dto;


/**
 * Dto con parámetros para búsquedas paginadas.
 * @author Silverio
 *
 */
public class PageRequestDto {

	public enum OrderSortValue { ASC, DESC }
	
	private int pageSize;
	
	private int pageNumber;
	
	private String sortFieldName;
	
	private OrderSortValue orderSort;

	public PageRequestDto(int pageSize, int pageNumber, String sortFieldName, OrderSortValue orderSort) {
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
		this.sortFieldName = sortFieldName;
		this.orderSort = orderSort;
	}


	public int getPageSize() {
		return pageSize;
	}



	public int getPageNumber() {
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

	@Override
	public String toString() {
		return "PageRequestDto [pageSize=" + pageSize + ", pageNumber=" + pageNumber + ", sortFieldName="
				+ sortFieldName + ", orderSort=" + orderSort + "]";
	}
	
}
