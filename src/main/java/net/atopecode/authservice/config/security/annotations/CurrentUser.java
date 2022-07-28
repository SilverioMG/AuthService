package net.atopecode.authservice.config.security.annotations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;

//Esta anotación (@) se puede utilizar en los 'parámetros' de los métodos 'Action' de nuestros 'Controllers' para
//que se le inyecte directamente el 'Usuario' que realiza la petición.
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}
