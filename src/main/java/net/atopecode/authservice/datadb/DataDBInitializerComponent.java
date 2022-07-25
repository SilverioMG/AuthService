package net.atopecode.authservice.datadb;

import net.atopecode.authservice.role.converter.RoleToRoleDtoConverter;
import net.atopecode.authservice.role.dto.RoleDto;
import net.atopecode.authservice.role.model.Role;
import net.atopecode.authservice.role.service.IRoleService;
import net.atopecode.authservice.role.service.query.IRoleQueryService;
import net.atopecode.authservice.role.value.RoleName;
import net.atopecode.authservice.user.dto.UserDto;
import net.atopecode.authservice.user.model.User;
import net.atopecode.authservice.user.service.IUserService;
import net.atopecode.authservice.user.service.query.IUserQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Este componente se encarga de insertar en la B.D. toda la info necesaria por defecto cuanda arranca el servicio web.
 */
@Component
public class DataDBInitializerComponent {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataDBInitializerComponent.class);

    @Value("${user.admin.name}")
    private String userAdminName;

    @Value("${user.admin.password}")
    private String userAdminPassword;

    @Value("${user.admin.email}")
    private String userAdminEmail;

    @Value("${user.admin.realName}")
    private String userAdminRealName;

    private IUserService userService;
    private IUserQueryService userQueryService;
    private IRoleService roleService;
    private IRoleQueryService roleQueryService;
    private RoleToRoleDtoConverter roleToRoleDtoConverter;

    public DataDBInitializerComponent(
            IUserService userService,
            IUserQueryService userQueryService,
            IRoleService roleService,
            IRoleQueryService roleQueryService,
            RoleToRoleDtoConverter roleToRoleDtoConverter){
        this.userService = userService;
        this.userQueryService = userQueryService;
        this.roleService = roleService;
        this.roleQueryService = roleQueryService;
        this.roleToRoleDtoConverter = roleToRoleDtoConverter;
    }


    @Transactional(rollbackFor = Exception.class)
    public void config() {
        insertDefaultRoles();
        insertDefaultUsers();
    }

    private void insertDefaultRoles() {
        for(String roleName: RoleName.getAllRolesName()) {
            if(roleQueryService.findByName(roleName).isEmpty()) {
                RoleDto roleDto = new RoleDto(null, roleName);
                roleService.insert(roleDto);
                LOGGER.info("Insertado en la B.D. el rol: " + roleName);
            }
        }
    }

    private void insertDefaultUsers(){
        User userAdmin = userQueryService.findByName(userAdminName).orElse(null);
        if(userAdmin == null) {
            LOGGER.info("No existe el User Admin en la B.D.");
            List<Role> allRolesInBD = roleQueryService.findAll();
            Set<RoleDto> rolesDtoUserAdmin = allRolesInBD.stream()
                    .map(roleToRoleDtoConverter::convert)
                    .collect(Collectors.toSet());

            UserDto userAdminDto = new UserDto(null, userAdminName, userAdminPassword, userAdminEmail, userAdminRealName, rolesDtoUserAdmin);
            userService.insert(userAdminDto);
            LOGGER.info("Se ha insertado el User Admin en la B.D.");
        }
    }
}
