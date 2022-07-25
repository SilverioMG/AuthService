package net.atopecode.authservice.user.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.atopecode.authservice.role.dto.RoleDto;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.user.dto.UserDto;
import net.atopecode.authservice.user.dto.filter.UserFilter;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.user.service.exceptions.UserNotFoundException;
import net.atopecode.authservice.validation.exceptions.ValidationException;


public interface IUserService {
	
	public User save(UserDto user) throws ValidationException;
	
	public User insert(UserDto user) throws ValidationException;
	
	public User update(UserDto user) throws ValidationException;
	
	/**
	 * Establece los 'Roles' de un 'User'.
	 * Se asigna justamente los 'Roles' recibidos como parámetro y se eliminan los que ya tuviese asignados pero no venga en dicha lista.
	 * La lista de roles recibida como parámetro deben ser los 'Roles' actuales del 'User'. 
	 * @param idUser
	 * @param rolesDto
	 * @return
	 * @throws ValidationException
	 */
	public User setRolesToUser(Long idUser, Set<RoleDto> rolesDto) throws ValidationException;
	
	/**
	 * Devuelve los 'Roles' de un 'User'.
	 * Si la propiedad de navegación (LazyLoad) está cargada se devuelven directamente, sino se buscan los 'Roles' en 
	 * la B.D. para el 'User'.
	 * @param user
	 * @return
	 */
	public List<Role> getRoles(User user);
	
	public void delete(Long idUser);
	
	//UserQueryService Methods:
	public User findById(Long id) throws UserNotFoundException;
	
	public User findByIdWithRoles(Long id) throws UserNotFoundException;
	
	public User findByName(String name) throws UserNotFoundException;
	
	public User findByNameWithRoles(String name) throws UserNotFoundException;

	public User findByNameOrEmailWithRoles(String nameOrEmail) throws UserNotFoundException;
	
	public User findByEmail(String email) throws UserNotFoundException;
	
	public User findByEmailWithRoles(String email) throws UserNotFoundException;
	
	public Page<User> findAll(PageRequest pageRequest);
	
	public Page<User> query(UserFilter filter) throws ValidationException;
	
	public Page<User> findAllWithRoles(PageRequest pageRequest);
}
