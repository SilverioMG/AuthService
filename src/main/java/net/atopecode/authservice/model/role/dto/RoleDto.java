package net.atopecode.authservice.model.role.dto;

public class RoleDto {

	private Long id;
	
	private String name;

	public RoleDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "RoleDto [id=" + id + ", name=" + name + "]";
	}
	
}
