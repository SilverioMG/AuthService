package net.atopecode.authservice.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import net.atopecode.authservice.config.security.utils.JwtTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider tokenProvider;
    private CustomUserDetailsService customUserDetailsService;

    //@Autowired
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Este método se ejecuta en cada Petición Rest del cliente web para Authenticar al Usuario y asignarlo al SecurityContext
     * (con sus roles) para posteriormente comprobar en los métodos de los 'Controllers' (Actions) si tiene acceso o no.
     * En caso de estar recibiendo una 'Petición REST' para un 'Action' que no require 'Authentication' (como el de 'Login' o 'RegistrarUsuario')
     * aunque este método lance una Excepción si no se pudo comprobrar correctamente el token, la ejecución de la petición continua y se ejecuta
     * el 'Action' correspondiente.
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try { //TODO... Permitir también authentication básica en el filtro (no solo por token).
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromJWT(jwt);
                //Se puede saltar este paso y en vez de recuperar la info del 'Usuario' de la B.D., guardarla en el Token y
                //leerla de ahí directamente (el problema es si el usuario cambia de rol, que entonces tendría que volver a Autenticarse para obtener otro Token.)
                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //Se añade el usuario con sus 'Roles' al 'context' para posteriormente realizar la 'Authorization' en los métodos 'Action' de los 'Controllers'.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
