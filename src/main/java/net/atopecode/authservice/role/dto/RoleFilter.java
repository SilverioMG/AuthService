package net.atopecode.authservice.role.dto;

import net.atopecode.authservice.dto.FilterPageableBase;
import net.atopecode.authservice.dto.PageRequestDto;

public class RoleFilter extends FilterPageableBase {

	private Long id;
	
	private String name;
	
	/*Si existe este constructor, Spring lo utiliza por defecto para mapear el Json recibido desde los Controllers que utilicen este Dto. Sino se utiliza el constructor que mejor
	 *se adapte según los nombres de los campos del Json recibido en el Controller.
	public RoleFilter() {
		super(null);
	}
	*/
	
	public RoleFilter(PageRequestDto pageRequest) {
		super(pageRequest);
	}
	
	public RoleFilter(Long id, String name, PageRequestDto pageRequest) {
		this(pageRequest);
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "RoleFilter [id=" + id + ", name=" + name + "]";
	}
		
}
