package net.atopecode.authservice.model.rel_user_role;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import net.atopecode.authservice.model.role.Role;
import net.atopecode.authservice.model.user.User;

@Entity
@Table(
		uniqueConstraints = {
				@UniqueConstraint(name="rel_user_role_unique_iduser_idrole", columnNames = {"idUser", "idRole"})
		},
		indexes = {
				@Index(name = "reluserrole_iduser", columnList = "idUser"),
				@Index(name = "reluserrole_idrole", columnList = "idRole")
		}
	)
public class RelUserRole implements Serializable {
	
	private static final long serialVersionUID = -1814471768732451144L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUser", nullable = false, foreignKey = @ForeignKey(name = "fk_idUser"))
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idRole", nullable = false, foreignKey = @ForeignKey(name = "fk_idRole"))
	private Role role;
	
	public RelUserRole() {

	}
	
	public RelUserRole (User user, Role role) {
		this.user = user;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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
		RelUserRole other = (RelUserRole) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String userString = (user != null) ? user.toString() : "null";
		String roleString = (role != null) ? role.toString() : "null";
		return String.format("user: { %d }, role: { %d }", userString, roleString);
	}
	
}
