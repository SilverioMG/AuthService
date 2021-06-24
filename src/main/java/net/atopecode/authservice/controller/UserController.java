package net.atopecode.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.atopecode.authservice.controller.utils.EntityMessage;
import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.converter.UserToUserDtoConverter;
import net.atopecode.authservice.model.user.dto.UserDto;
import net.atopecode.authservice.service.user.UserService;
import net.atopecode.authservice.service.user.validator.UserValidatorComponent;
import net.atopecode.authservice.validators.exception.ValidationException;

@RestController
@RequestMapping("/user")
public class UserController {

	private UserService userService;
	private UserToUserDtoConverter userToUserDtoConverter;
	
	@Autowired
	public UserController(UserService userService,
			UserToUserDtoConverter userToUserDtoConverter) {
		this.userService = userService;
		this.userToUserDtoConverter = userToUserDtoConverter;
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
	public ResponseEntity<EntityMessage<UserDto>> save(@RequestBody UserDto userDto) throws ValidationException{
		User user = userService.save(userDto);
		userDto = userToUserDtoConverter.convert(user);
		return new EntityMessage<UserDto>(userDto, "Usuario creado.").toResponseEntity(HttpStatus.OK);
	}
	
	// /api/findAll?page=0&pageSize=10
	@PostMapping("/findAll")
	public ResponseEntity<EntityMessage<Page<UserDto>>> findAll(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize){
		PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Order.asc(UserValidatorComponent.UserFieldNames.ID)));
		Page<User> users = userService.findAll(pageRequest);
		Page<UserDto> result = users.map((user) -> userToUserDtoConverter.convertWithoutPassword(user));
		
		return new EntityMessage<Page<UserDto>>(result, "").toResponseEntity(HttpStatus.OK);
	}
	
	//TODO... Crear un Servicio Spring para la localizacion de mensajes i18n. Inyectarlo en este controller y utilizarlo en vez de los mensajes puestos a mano como "Usuario creado".
	//		  Otra opción es crear un @Component que utilice 'EntityMessage' y que utilizando el nuevo servicio de Localization creado traduzca directamente el mensaje sin tener que
	//		  utilizarlo en todos los Controllers.
}
