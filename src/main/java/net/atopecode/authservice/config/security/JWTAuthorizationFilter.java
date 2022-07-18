package net.atopecode.authservice.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.atopecode.authservice.config.security.utils.JwtPayload;
import net.atopecode.authservice.config.security.utils.JwtTokenProvider;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	
	private JwtTokenProvider jwtProvider;
	
	
	public JWTAuthorizationFilter(AuthenticationManager authManager, JwtTokenProvider jwtProvider) {
		super(authManager);
		this.jwtProvider = jwtProvider;
	}

	@Override
	public void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
		String header = req.getHeader(AUTHORIZATION_HEADER);
		
		if(header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(req,res);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req,res);
	}
	
	//Lee el el token JWT del Header Http y se valida el Token para añadir el usuario al context de la petición Http.
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);

		if ((token != null) && (jwtProvider.validateToken(token))) {
			try {
				JwtPayload payload = jwtProvider.getPayloadFromJWT(token);
				if (payload != null) {
					return new UsernamePasswordAuthenticationToken(payload.getUserName(), null, payload.getRoles());
				}
			}
			catch(JsonProcessingException ex) {
				return null;
			}
		}
   
        return null;
        
	}
} 
