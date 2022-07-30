package net.atopecode.authservice.config.security;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Esta clase se utiliza para generar la respuesta personalizada cuando se hace una petición al servicio sin 'Authorizacion'.
 * Se utiliza desde la clase 'SecurityConfig' y solo se ejecuta cuando el Usuario no está autenticado e intenta acceder a un
 * endpoint que requiere autenticación.
 *
 * Si el Usuario está autenticado pero accede a un endpoint para el que no tiene permisos suficientes, este componente no se utiliza
 * y en su lugar SpringSecurity lanza una 'AccessDeniedException' que se manejar desde 'ExceptionHandlerComponent' para personalizar la
 * respuesta hacia la web.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger =  LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException ex) throws IOException {
    	String message = MessageFormat.format("Not Authorized: {0}", ex.getMessage());
        logger.error(message);
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }
}
