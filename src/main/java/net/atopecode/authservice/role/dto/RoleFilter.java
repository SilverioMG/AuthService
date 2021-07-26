package net.atopecode.authservice.role.dto;

import net.atopecode.authservice.dto.FilterPageableBase;
import net.atopecode.authservice.dto.PageRequestDto;

public class RoleFilter extends FilterPageableBase {

	private Long id;
	
	private String name;
	
	
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
