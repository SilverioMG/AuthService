package net.atopecode.authservice.user.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.atopecode.authservice.authentication.service.AuthenticationService;
import net.atopecode.authservice.config.swagger.SwaggerConfig;
import net.atopecode.authservice.role.value.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.atopecode.authservice.controller.utils.ResultMessage;
import net.atopecode.authservice.localization.ILocaleService;
import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;
import net.atopecode.authservice.user.converter.UserToUserDtoConverter;
import net.atopecode.authservice.user.dto.UserDto;
import net.atopecode.authservice.user.dto.filter.UserFilter;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.user.model.UserFieldNames;
import net.atopecode.authservice.user.service.IUserService;
import net.atopecode.authservice.user.service.exceptions.UserNotFoundException;
import net.atopecode.authservice.validation.exceptions.ValidationException;

@RestController
@RequestMapping("/user")
public class UserController {
	
	public static final String USER_INSERT_OK = "user.insert.ok";
	public static final String USER_UPDATE_OK = "user.update.ok";

	private final IUserService userService;
	private final UserToUserDtoConverter userToUserDtoConverter;
	private final ILocaleService localeService;
	private final AuthenticationService authService;
	
	@Autowired
	public UserController(IUserService userService,
						  UserToUserDtoConverter userToUserDtoConverter,
						  ILocaleService localeService,
						  AuthenticationService authService) {
		this.userService = userService;
		this.userToUserDtoConverter = userToUserDtoConverter;
		this.localeService = localeService;
		this.authService = authService;
	}

	/**
	 * Se crea un nuevo 'Usuario' en la B.D. y se devuelve su info (sin el password) como respuesta.
	 * @param userDto
	 * @return
	 * @throws ValidationException
	 */
	//Desde la clase 'SecurityConfig' se permite el acceso a este action para cualquier usuario (aunque no esté autenticado).
	@PostMapping("/new")
	public ResponseEntity<ResultMessage<UserDto>> newUser(@RequestBody UserDto userDto) throws ValidationException{
		MessageLocalized messageLocalized = new MessageLocalized(USER_INSERT_OK);
		User user = userService.insert(userDto);
		userDto = userToUserDtoConverter.convert(user);
		return new ResultMessage<UserDto>(userDto, localeService, messageLocalized, true)
				.toResponseEntity(HttpStatus.OK);
	}

	//TODO... Este método solo se puede llamar si el usuario está autenticado (SecurityConfig, configuración por defecto para todos los endpoints).
	//		  Habría que comprobar con '@PreAuthorize()' que además de estar autenticado, el usuario solo pueda modificarse a sí mismo (obtener el id del
	//		  user autenticado del security context y compararlo con el id del 'UserDto' que se quiere modificar. Además, que si el Usuario tiene role 'Admin'
	//		  siempre pueda modificar a cualquier usuario.
	/**
	 * Se actualiza un 'Usuario' existente en la B.D. y se devuelve su info (sin el password) como respuesta.
	 * @param userDto
	 * @return
	 * @throws ValidationException
	 */
	@PutMapping("/update")
	public ResponseEntity<ResultMessage<UserDto>> updateUser(@RequestBody UserDto userDto) throws ValidationException{
		MessageLocalized messageLocalized = new MessageLocalized(USER_UPDATE_OK);
		User user = userService.update(userDto);
		userDto = userToUserDtoConverter.convert(user);
		return new ResultMessage<UserDto>(userDto, localeService, messageLocalized, true)
				.toResponseEntity(HttpStatus.OK);
	}

	//TODO... Este método solo se puede llamar si el usuario está autenticado (SecurityConfig, configuración por defecto para todos los endpoints).
	//		  Habría que comprobar con '@PreAuthorize()' que además de estar autenticado, el usuario solo pueda consultarse a sí mismo (obtener el id del
	//		  user autenticado del security context y compararlo con el id de la petición Http. Además, que si el Usuario tiene role 'Admin'
	//		  siempre pueda consultar a cualquier usuario.
	/**
	 * Se recupera a un Usuario por su 'id'.
	 * @param id
	 * @return
	 * @throws UserNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ResultMessage<UserDto>> findById(@PathVariable("id") Long id) throws UserNotFoundException{
		User user = userService.findById(id);
		UserDto userDto = userToUserDtoConverter.convert(user);
		return new ResultMessage<UserDto>(userDto).toResponseEntity(HttpStatus.OK);		
	}
	
	/**
	 * Ejemplo Petición Http: /api/findAll?page=0&pageSize=10
	 * Consulta paginada que devuelve los Usuarios ordenados por 'id'.
	 * Se devuelven los 'Users' sin el 'password' por seguridad.
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('" + RoleName.ROLE_ADMIN + "')")
	@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEMA_NAME)
	@GetMapping("/findAll")
	public ResponseEntity<ResultMessage<Page<UserDto>>> findAll(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize){
		PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Order.asc(UserFieldNames.ID)));
		Page<User> users = userService.findAll(pageRequest);
		Page<UserDto> result = users.map(user -> userToUserDtoConverter.convert(user));
		
		return new ResultMessage<Page<UserDto>>(result).toResponseEntity(HttpStatus.OK);
	}
	
	/**
	 * Query con filtro para buscar por campos específicos y con posibilidad de paginación.
	 * @param filter
	 * @return
	 * @throws ValidationException
	 */
	@PreAuthorize("hasAnyAuthority('" + RoleName.ROLE_ADMIN + "')")
	@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEMA_NAME)
	@PostMapping("/query")
	public ResponseEntity<ResultMessage<Page<UserDto>>> query(@RequestBody UserFilter filter) throws ValidationException{
		Page<User> result = userService.query(filter);
		Page<UserDto> resultDto = result.map(userToUserDtoConverter::convert);
		
		return new ResultMessage<Page<UserDto>>(resultDto).toResponseEntity(HttpStatus.OK);
	}

}
