package net.atopecode.authservice.authentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.atopecode.authservice.user.dto.authentication.LoginResponseDto;
import net.atopecode.authservice.user.dto.authentication.LoginRequestDto;

public interface IAuthenticationService {

	LoginResponseDto generateJWT(LoginRequestDto request) throws JsonProcessingException;
}
