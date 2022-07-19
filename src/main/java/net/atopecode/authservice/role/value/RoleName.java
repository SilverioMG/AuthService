package net.atopecode.authservice.role.value;

import net.atopecode.authservice.role.dto.RoleDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Esta clase contiene los nombres de los 'Roles' de la B.D.
 * Nota.- El nombre de los roles debe comenzar por 'ROLE_' para tener compatibilidad con SpringSecurity.
 */
public class RoleName {

    public static final String ROLE_ADMINISTRADOR = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    public static List<String> getAllRolesName(){
        String[] roleNames = new String[]{ ROLE_ADMINISTRADOR, ROLE_USER };
        return Arrays.asList(roleNames);
    }

    public static Set<RoleDto> getSetWithAllRolesDto(){
        Set<RoleDto> rolesDto = getAllRolesName().stream()
                    .map(roleName -> new RoleDto(null, roleName))
                    .collect(Collectors.toSet());
        return rolesDto;
    }
}
