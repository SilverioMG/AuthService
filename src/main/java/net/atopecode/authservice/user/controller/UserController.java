package net.atopecode.authservice.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@Autowired
	public UserController(IUserService userService,
			UserToUserDtoConverter userToUserDtoConverter,
			ILocaleService localeService) {
		this.userService = userService;
		this.userToUserDtoConverter = userToUserDtoConverter;
		this.localeService = localeService;
	}
	
	/**
	 * Se crea un nuevo 'Usuario' en la B.D. y se devuelve su info (con el password incluído) como respuesta.
	 * Si el valor del campo 'id' vale 'null' se intetará insertar un nuevo 'Usuario'.
	 * Si el campo 'id' tiene valor, se intentará modificar a dicho 'Usuario'.
	 * @param userDto
	 * @return
	 * @throws ValidationException
	 */
	@PostMapping("/save")
	public ResponseEntity<ResultMessage<UserDto>> save(@RequestBody UserDto userDto) throws ValidationException{
		MessageLocalized messageLocalized = (userDto.getId() == null) ? new MessageLocalized(USER_INSERT_OK) : new MessageLocalized(USER_UPDATE_OK);
		User user = userService.save(userDto);
		userDto = userToUserDtoConverter.convert(user);
		return new ResultMessage<UserDto>(userDto, localeService, messageLocalized, true)
				.toResponseEntity(HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResultMessage<UserDto>> findById(@PathVariable("id") Long id) throws UserNotFoundException{
		User user = userService.findById(id);
		UserDto userDto = userToUserDtoConverter.convert(user);
		return new ResultMessage<UserDto>(userDto).toResponseEntity(HttpStatus.OK);		
	}
	
	// /api/findAll?page=0&pageSize=10
	/**
	 * Consulta paginada que devuelve los Usuarios ordenados por 'id'.
	 * Se devuelven los 'Users' sin el 'password' por seguridad.
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/findAll")
	public ResponseEntity<ResultMessage<Page<UserDto>>> findAll(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize){
		PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Order.asc(UserFieldNames.ID)));
		Page<User> users = userService.findAll(pageRequest);
		Page<UserDto> result = users.map(user -> userToUserDtoConverter.convert(user));
		
		return new ResultMessage<Page<UserDto>>(result).toResponseEntity(HttpStatus.OK);
	}
	
	@PostMapping("/query")
	public ResponseEntity<ResultMessage<Page<UserDto>>> query(@RequestBody UserFilter filter) throws ValidationException{
		Page<User> result = userService.query(filter);
		Page<UserDto> resultDto = result.map(userToUserDtoConverter::convert);
		
		return new ResultMessage<Page<UserDto>>(resultDto).toResponseEntity(HttpStatus.OK);
	}
}
