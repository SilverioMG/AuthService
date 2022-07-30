package net.atopecode.authservice.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import net.atopecode.authservice.config.security.token.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        /*securedEnabled = true, //para poder utilizar @Secured("ROLE_ROLE") en los Actions de los Controllers/Services.*/
        /*jsr250Enabled = true, //para poder utilizar @RolesAllowed("ROLE") en los Actions de los Controllers/Services.*/
        prePostEnabled = true //para poder utilizar @PreAuthorize("isAnonymous()") ó @PreAuthorize("hasRole('ROLE')") en los Actions de los Controllers/Services.
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private CustomUserDetailsService customUserDetailsService;
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    public SecurityConfig(
    		CustomUserDetailsService customUserDetailsService, 
    		JwtAuthenticationEntryPoint unauthorizedHandler, 
    		BCryptPasswordEncoder bCryptPasswordEncoder,
    		JwtTokenProvider jwtTokenProvider) {
    	this.customUserDetailsService = customUserDetailsService;
    	this.unauthorizedHandler = unauthorizedHandler;
    	this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    	this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
        //return super.authenticationManager();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                //.accessDeniedHandler() //TODO...
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .antMatchers(
                                "/",
                                "/favicon.ico",
                                "/**/*.png",
                                "/**/*.gif",
                                "/**/*.svg",
                                "/**/*.jpg",
                                "/**/*.html",
                                "/**/*.css",
                                "/**/*.js"
                        ) //Recursos estáticos web.
                        .permitAll()
                        .antMatchers(
                                "/user/new",
                                "/auth/login"
                        ) //Authenticacion JWT.
                        .permitAll()
                        .antMatchers(
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/swagger-resources/**"
                        ) //Swagger Doc endpoint.
                        .permitAll()
                .anyRequest()
                    .authenticated();   //Hay que estar authenticado para todas las peticiones REST excepto para la declaradas con '.permitAll()'.
                                        // Después se puede modificar este comportamiento en cada Controller o Action con la correspondiente anotación de Seguridad (@PreAuthorize(), @Secured() o @RolesAllowed()).
                //.requiresChannel().requiresSecure(); //Para forzar que todas las llamadas al Servicio Web deban ser por 'https'.

        //Add our custom JWT security filter. Este filtro se ejecuta antes de procesar cada petición REST desde el cliente para obtener el Token de Authenticacion (JWT).
        //Si se recibe Token se verifica y se asigna como 'UserPrincipal' en el 'Context' de SpringSecurity para esta petición Rest (hilo) para poder realizar la 'Autorización'
        //en los métodos 'Action' de los 'Controller' y permitir denegar el acceso según corresponda (decoradores o atributos de SpringSecurity).
        //Según la configuración superior del objeto 'HttpSecurity', solo se permiten conexiones anónimas (sin Token o aquellas en las que Token no se pudo autenticar)
        //para las urls de 'Crear un Nuevo Usuario' y 'Loguear un usuario existente (crea el token para esa sesión del usuario).
        http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtTokenProvider)); 
    }

}
