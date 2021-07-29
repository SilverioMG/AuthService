package net.atopecode.authservice.user.dto.filter;

import net.atopecode.authservice.dto.FilterPageableBase;
import net.atopecode.authservice.dto.PageRequestDto;

public class UserFilter extends FilterPageableBase {
	
	private Long id;

	private String name;
	
	private String email;
	
	private String realName;
	
	private UserRoleFilter roles;
	
	/*Si existe este constructor, Spring lo utiliza por defecto para mapear el Json recibido desde los Controllers que utilicen este Dto. Sino se utiliza el constructor que mejor
	 *se adapte según los nombres de los campos del Json recibido en el Controller.
	public UserFilter() {
		super(null);
	}
	*/
	
	public UserFilter(PageRequestDto pageRequest) {
		super(pageRequest);
		this.roles = null;
	}

	public UserFilter(Long id, String name, String email, 
			String realName, UserRoleFilter roles, PageRequestDto pageRequest) {
		this(pageRequest);
		this.id = id;
		this.name = name;
		this.email = email;
		this.realName = realName;
		this.roles = roles;
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

	public UserRoleFilter getRoles() {
		return roles;
	}

	@Override
	public String toString() {
		return "UserFilter [id=" + id + ", name=" + name + ", email=" + email + ", realName="
				+ realName + ", roles=" + roles + "]";
	}

}
