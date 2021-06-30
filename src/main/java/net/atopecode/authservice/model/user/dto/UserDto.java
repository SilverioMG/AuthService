package net.atopecode.authservice.model.user.dto;


public class UserDto {

	private Long id;
	
	private String name;
	
	private String password;
	
	private String email;
	
	private String realName;
	
	public UserDto() {
		//Empty Constructor.
	}

	public UserDto(Long id, String name, String password, String email, String realName) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.email = email;
		this.realName = realName;
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

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", name=" + name + ", password=" + password + ", email=" + email + ", realName="
				+ realName + "]";
	}
	
}
