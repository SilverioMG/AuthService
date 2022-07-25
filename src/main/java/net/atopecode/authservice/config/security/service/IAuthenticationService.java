package net.atopecode.authservice.config.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.atopecode.authservice.user.dto.authentication.JwtAuthenticationResponse;
import net.atopecode.authservice.user.dto.authentication.LoginRequest;

public interface IAuthenticationService {

	JwtAuthenticationResponse generateJWT(LoginRequest request) throws JsonProcessingException;
}
