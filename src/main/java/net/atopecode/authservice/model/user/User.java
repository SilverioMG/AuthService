package net.atopecode.authservice.model.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import net.atopecode.authservice.model.interfaces.INormalizable;
import net.atopecode.authservice.model.rel_user_role.RelUserRole;
import net.atopecode.authservice.util.NormalizeString;

@Entity
@Table(name = "users",
	   uniqueConstraints = {
			   @UniqueConstraint(name="users_unique_name", columnNames= {"name"}),
			   @UniqueConstraint(name="users_unique_nm_name", columnNames= {"nm_name"}),
			   @UniqueConstraint(name="users_unique_email", columnNames= {"email"})
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
public class User implements INormalizable, Serializable {
	

	private static final long serialVersionUID = -5283345980003142562L;

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
	private String nm_realName;
	
	//Propiedades de navegación:
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<RelUserRole> relUserRole = new HashSet<RelUserRole>();
	
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
		
	public Set<RelUserRole> getRelUserRole() {
		return relUserRole;
	}
	
	public void normalize() {
		this.nm_name = NormalizeString.normalize(name);
		this.nm_email = NormalizeString.normalize(email);
		this.nm_realName = NormalizeString.normalize(realName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("id: %d, name: %s, email: %s, realName: %s", id, name, email, realName);
	}
	
}
