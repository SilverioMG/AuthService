package net.atopecode.authservice.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.atopecode.authservice.user.dto.authentication.JwtAuthenticationResponse;
import net.atopecode.authservice.user.dto.authentication.LoginRequest;

public interface IUserAuthenticationService {

	JwtAuthenticationResponse generateJWT(LoginRequest request) throws JsonProcessingException;
}
