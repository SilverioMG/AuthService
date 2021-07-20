package net.atopecode.authservice.dto;

/**
 * Clase abstracta para búsquedas paginadas con filtro.
 * La clase que herede de ésta debe indicar los campos por los que filtrar la consulta en cuestión.
 * @author Silverio
 *
 */
public abstract class FilterPageableBase {

	private PageRequestDto pageRequest;

	protected FilterPageableBase(PageRequestDto pageRequest) {
		this.pageRequest = pageRequest;
	}
	
	

	public PageRequestDto getPageRequest() {
		return pageRequest;
	}


	@Override
	public String toString() {
		return "FilterPageableBase [pageRequest=" + pageRequest + "]";
	}	
	
}
