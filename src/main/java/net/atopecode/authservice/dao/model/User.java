package net.atopecode.authservice.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import net.atopecode.authservice.dao.model.interfaces.INormalizable;
import net.atopecode.authservice.util.NormalizeString;

@Entity
@Table(name = "users",
	   uniqueConstraints = {
			   @UniqueConstraint(name="users_unique_name", columnNames= {"name"}),
			   @UniqueConstraint(name="users_unique_nm_name", columnNames= {"nm_name"})
			   },
	   indexes = {
			   @Index(name="users_name", columnList="name"),
			   @Index(name="users_nm_name", columnList="nm_name"),
			   @Index(name="users_email", columnList="email"),
			   @Index(name="users_nm_email", columnList="nm_email"),
			   @Index(name="users_real_name", columnList="realName"),
			   @Index(name="users_nm_real_name", columnList="nm_realName")
	   }
)
public class User implements INormalizable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Version
	private long version = 0L;
	
	@Column(nullable = false, length = 30)
	private String name;
	
	@Column(nullable = false, length = 30)
	private String password;
	
	@Column(nullable = false, length = 50)
	private String email;
	
	@Column(length = 100)
	private String realName;
	
	//Campos normalizados:
	@Column(nullable = false, length = 30)
	private String nm_name;
	
	@Column(nullable = false, length = 50)
	private String nm_email;
	
	@Column(length = 100) 
	String nm_realName;
	
	public User() {

	}
	
	public User(String name, String password, String email, String realName) {
		this.id = null;
		this.name = name;
		this.password = password;
		this.email = email;
		this.realName = realName;
		
		normalize();
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
		this.nm_name = NormalizeString.normalize(name);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		this.nm_email = NormalizeString.normalize(email);
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
		this.nm_realName = NormalizeString.normalize(realName);
	}

	public String getNm_name() {
		return nm_name;
	}

	public String getNm_email() {
		return nm_email;
	}

	public String getNm_realName() {
		return nm_realName;
	}

	public void normalize() {
		this.nm_name = NormalizeString.normalize(name);
		this.nm_email = NormalizeString.normalize(email);
		this.nm_realName = NormalizeString.normalize(realName);
	}
	
	@Override
	public String toString() {
		return String.format("id: %d, name: %s, email: %s, realName: %s", id, name, email, realName);
	}
	
}
