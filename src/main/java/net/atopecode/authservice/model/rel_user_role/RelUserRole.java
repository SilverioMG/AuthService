package net.atopecode.authservice.model.rel_user_role;

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
public class RelUserRole {
	
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
	public String toString() {
		String userString = (user != null) ? user.toString() : "null";
		String roleString = (role != null) ? role.toString() : "null";
		return String.format("user: { %d }, role: { %d }", userString, roleString);
	}
	
}
