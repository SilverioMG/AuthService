package net.atopecode.authservice.role.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import net.atopecode.authservice.role.converter.RoleToRoleDtoConverter;
import net.atopecode.authservice.role.dto.RoleDto;
import net.atopecode.authservice.role.dto.RoleFilter;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.role.model.RoleFieldNames;
import net.atopecode.authservice.role.service.IRoleService;
import net.atopecode.authservice.validation.exceptions.ValidationException;


@RestController
@RequestMapping("/role")
public class RoleController {
	
	public static final String ROLE_INSERT_OK = "role.insert.ok";
	public static final String ROLE_UPDATE_OK = "role.update.ok";

	private final IRoleService roleService;
	private final RoleToRoleDtoConverter roleToRoleDtoConverter;
	private final ILocaleService localeService;
	
	@Autowired
	public RoleController(IRoleService roleService,
			RoleToRoleDtoConverter roleToRoleDtoConverter,
			ILocaleService localeService) {
		this.roleService = roleService;
		this.roleToRoleDtoConverter = roleToRoleDtoConverter;
		this.localeService = localeService;
	}
	
	/**
	 * Se crea un nuevo 'Role' en la B.D. y se devuelve su info como respuesta.
	 * Si el valor del campo 'id' vale 'null' se intentará insertar un nuevo 'Role'.
	 * Si el campo 'id' tiene valor, se intentará modificar a dicho 'Role'.
	 * @param roleDto
	 * @return
	 * @throws ValidationException
	 */
	@PostMapping("/save")
	public ResponseEntity<ResultMessage<RoleDto>> save(@RequestBody RoleDto roleDto) throws ValidationException{
		MessageLocalized messageLocalized = (roleDto.getId() == null) ? new MessageLocalized(ROLE_INSERT_OK) : new MessageLocalized(ROLE_UPDATE_OK);
		Role role = roleService.save(roleDto);
		roleDto = roleToRoleDtoConverter.convert(role);
		return new ResultMessage<RoleDto>(roleDto, localeService, messageLocalized, true)
				.toResponseEntity(HttpStatus.OK);
	}
	
	/**
	 * Se recupera un 'Role' por su 'id'.
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ResultMessage<RoleDto>> findById(@PathVariable("id") Long id){
		Role role = roleService.findById(id).orElse(null);
		if(role != null) {
			RoleDto roleDto = roleToRoleDtoConverter.convert(role);
			return new ResultMessage<RoleDto>(roleDto, "").toResponseEntity(HttpStatus.OK);
		}
		else {
			return new ResultMessage<RoleDto>(null, false).toResponseEntity(HttpStatus.NOT_FOUND);
		}		
	}
	
	// /api/findAll?page=0&pageSize=10
	/**
	 * Consulta paginada que devuelve los Roles ordenados por 'id'.
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/findAll")
	public ResponseEntity<ResultMessage<Page<RoleDto>>> findAll(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize){
		PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Order.asc(RoleFieldNames.ID)));
		Page<Role> roles = roleService.findAll(pageRequest);
		Page<RoleDto> result = roles.map(role -> roleToRoleDtoConverter.convert(role));
		
		return new ResultMessage<Page<RoleDto>>(result, "").toResponseEntity(HttpStatus.OK);
	}
	
	@PostMapping("/query")
	public ResponseEntity<ResultMessage<List<RoleDto>>> query(@RequestBody RoleFilter roleFilter){
		List<Role> result = roleService.query(roleFilter);
		List<RoleDto> resultDto = result.stream()
				.map(role -> roleToRoleDtoConverter.convert(role))
				.collect(Collectors.toList());
		
		return new ResultMessage<List<RoleDto>>(resultDto, "").toResponseEntity(HttpStatus.OK);
	}
}
