package net.atopecode.authservice.authentication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.atopecode.authservice.authentication.service.IAuthenticationService;
import net.atopecode.authservice.controller.utils.ResultMessage;
import net.atopecode.authservice.user.dto.authentication.LoginRequestDto;
import net.atopecode.authservice.user.dto.authentication.LoginResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/auth")
public class AuthenticationController {

    private IAuthenticationService authenticationService;

    public AuthenticationController(IAuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    /**
     * Se devuelve el token JWT para que el Usuario pueda realizar peticiones Http a los endpoints del Servicio Web que requieren 'Autenticación'.
     * @param loginRequest
     * @return
     * @throws JsonProcessingException
     */
    //Desde la clase 'SecurityConfig' se permite el acceso a este action para cualquier usuario (aunque no esté autenticado).
    @PostMapping("/login")
    public ResponseEntity<ResultMessage<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequest) throws JsonProcessingException {
        LoginResponseDto tokenJwt = authenticationService.generateJWT(loginRequest);
        return new ResultMessage<LoginResponseDto>(tokenJwt).toResponseEntity(HttpStatus.OK);
    }
}
