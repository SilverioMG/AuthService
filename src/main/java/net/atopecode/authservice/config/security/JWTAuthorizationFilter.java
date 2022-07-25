package net.atopecode.authservice.config.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
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

		String token = header.replace(TOKEN_PREFIX, "");
		UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req,res);
	}
	
	//Lee el el token JWT del Header Http y se valida el Token para añadir el usuario al context de la petición Http.
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if ((token != null) && (jwtProvider.validateToken(token))) {
			try {
				JwtPayload payload = jwtProvider.getPayloadFromJWT(token);
				if (payload != null) {
					Collection<? extends GrantedAuthority> authorities = UserPrincipal.mapToGrantedAuthorities(payload.getAuthorities());
					return new UsernamePasswordAuthenticationToken(payload.getUserName(), null, authorities);
				}
			}
			catch(JsonProcessingException ex) {
				return null;
			}
		}
   
        return null;
        
	}
} 
