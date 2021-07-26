package net.atopecode.authservice.user.dto;


import java.util.Arrays;

import net.atopecode.authservice.dto.FilterPageableBase;
import net.atopecode.authservice.dto.PageRequestDto;

public class UserFilter extends FilterPageableBase {
	
	private Long id;

	private String name;
	
	private String email;
	
	private String realName;
	
	private String[] roles;
	
	public UserFilter(PageRequestDto pageRequest) {
		super(pageRequest);
		this.roles = new String[0];
	}
	
	public UserFilter(Long id, String name, String email, 
			String realName, String[] roles, PageRequestDto pageRequest) {
		this(pageRequest);
		this.id = id;
		this.email = email;
		this.realName = realName;
		this.roles = (roles != null) ? roles : new String[0]; //Optional.ofNullable(roles).orElse(new String[0]);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getRealName() {
		return realName;
	}

	public String[] getRoles() {
		return roles;
	}

	@Override
	public String toString() {
		return "UserFilter [id=" + id + ", name=" + name + ", password=" + email + ", realName="
				+ realName + ", roles=" + Arrays.toString(roles) + "]";
	}

}
