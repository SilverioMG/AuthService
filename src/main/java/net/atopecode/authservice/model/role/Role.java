package net.atopecode.authservice.model.role;

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

import net.atopecode.authservice.model.interfaces.INormalizable;
import net.atopecode.authservice.model.rel_user_role.RelUserRole;
import net.atopecode.authservice.util.NormalizeString;

@Entity
@Table(
		uniqueConstraints = {
				@UniqueConstraint(name="role_unique_name", columnNames= {"name"}),
				@UniqueConstraint(name="role_unique_nm_name", columnNames= {"nm_name"})
				},
		indexes = {
				@Index(name="role_name", columnList="name"),
				@Index(name="role_nm_name", columnList="nm_name")
			}
		)
public class Role implements INormalizable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;
	
	@Column(nullable = false, length = 30)
	private String nm_name;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	private Set<RelUserRole> relUserRole = new HashSet<>();
	
	public Role() {
		
	}
	
	public Role(String name) {
		this.id = null;
		this.name = name;
		
		normalize();
	}
	
	public void setName(String name) {
		this.name = name;
		this.nm_name = NormalizeString.normalize(name);
	}
	
	public Set<RelUserRole> getRelUserRole() {
		return relUserRole;
	}

	public void normalize() {
		nm_name = NormalizeString.normalize(name);
	}
	
	@Override
	public String toString() {
		return String.format("name: %s, nm_name: %s", name, nm_name);
	}
}
