package net.atopecode.authservice.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.atopecode.authservice.config.security.utils.JwtTokenProvider;
import net.atopecode.authservice.user.dto.authentication.JwtAuthenticationResponse;
import net.atopecode.authservice.user.service.exceptions.UserAuthenticacionFailedException;
import net.atopecode.authservice.validation.Validator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.atopecode.authservice.user.dto.authentication.LoginRequest;

@Service
public class UserAuthenticationService implements IUserAuthenticationService {

	private AuthenticationManager authenticationManager;
	private JwtTokenProvider jwtTokenProvider;
	private Validator validator;

	public UserAuthenticationService(
			AuthenticationManager authenticationManager,
			JwtTokenProvider jwtTokenProvider) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.validator = new Validator();
	}

	@Override
	public JwtAuthenticationResponse generateJWT(LoginRequest request) throws JsonProcessingException {
		validate(request);

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
		usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(),
				request.getPassword());
		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtTokenProvider.generateToken(authentication);

		return new JwtAuthenticationResponse(token);
	}

	private void validate(LoginRequest request) {
		validator.notNull(request, new UserAuthenticacionFailedException());
		validator.notEmpty(request.getUsernameOrEmail(), new UserAuthenticacionFailedException());
		validator.notEmpty(request.getPassword(), new UserAuthenticacionFailedException());
	}
}
