package net.atopecode.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.atopecode.authservice.controller.utils.EntityMessage;
import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.model.user.converter.UserToUserDtoConverter;
import net.atopecode.authservice.model.user.dto.UserDto;
import net.atopecode.authservice.service.user.UserService;
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
	
	//TODO... Crear un Servicio Spring para la localizacion de mensajes i18n. Inyectarlo en este controller y utilizarlo en vez de los mensajes puestos a mano como "Usuario creado".
	//		  Otra opción es crear un @Component que utilice 'EntityMessage' y que utilizando el nuevo servicio de Localization creado traduzca directamente el mensaje sin tener que
	//		  utilizarlo en todos los Controllers.
}
