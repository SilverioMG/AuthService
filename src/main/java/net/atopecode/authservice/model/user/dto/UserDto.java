package net.atopecode.authservice.model.user.dto;

import java.util.HashSet;
import java.util.Set;

import net.atopecode.authservice.model.role.dto.RoleDto;

public class UserDto {

	private Long id;
	
	private String name;
	
	private String password;
	
	private String email;
	
	private String realName;
	
	private Set<RoleDto> roles;
	
	public UserDto() {
		this.roles = new HashSet<>();
	}

	public UserDto(Long id, String name, String password, String email, String realName,
			Set<RoleDto> roles) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.email = email;
		this.realName = realName;
		this.roles = (roles != null) ? roles : new HashSet<>();
	}

	public Long getId() {
		return id;
	}

	public UserDto setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public UserDto setName(String name) {
		this.name = name;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public UserDto setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public UserDto setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getRealName() {
		return realName;
	}

	public UserDto setRealName(String realName) {
		this.realName = realName;
		return this;
	}
	
	public void setRoles(Set<RoleDto> roles) {
		this.roles = (roles != null) ? roles : new HashSet<RoleDto>();
	}
	
	public Set<RoleDto> getRoles(){
		return roles;
	}

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", name=" + name + ", password=" + password + ", email=" + email + ", realName="
				+ realName + ", roles=" + roles + "]";
	}
	
}
