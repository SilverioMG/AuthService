package net.atopecode.authservice.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import net.atopecode.authservice.config.security.utils.EncryptPasswordEncoder;
import net.atopecode.authservice.config.security.utils.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true, //para poder utilizar @Secured("ROLE_ROLE") en los Actions de los Controllers/Services.
        jsr250Enabled = true, //para poder utilizar @RolesAllowed("ROLE") en los Actions de los Controllers/Services.
        prePostEnabled = true //para poder utilizar @PreAuthorize("isAnonymous()") ó @PreAuthorize("hasRole('ROLE')") en los Actions de los Controllers/Services.
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder(); //El que usa por defecto 'SpringSecurity', pero en la B.D. los 'passwords' de los 'Usuarios' no se encryptan con este algoritmo.
        boolean webClientPasswordEncoded = false; //Cuando se ejecute el método 'LoginManager.authenticateUser()' si 'webClientPasswordEncoded' vale 'false', se encrypta el password para comprobar con el de la B.D.
        return new EncryptPasswordEncoder(webClientPasswordEncoded);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
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
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                //.antMatchers("/auth/**")
                //.permitAll()
                .antMatchers("/login/registeruser", "/login/authenticateuser")
                .permitAll()
                //.antMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**")
                //.permitAll()
                .anyRequest()
                .authenticated(); //Hay que estar authenticado para todas las peticiones REST excepto para la declaradas como '.permitAll()'.
                //.requiresChannel().requiresSecure(); //Para forzar que todas las llamadas al Servicio Web deban ser por 'https'.

        //Add our custom JWT security filter. Este filtro se ejecuta antes de procesar cada petición REST desde el cliente para obtener el Token de Authenticacion (JWT).
        //Si se recibe Token se verifica y se asigna como 'UserPrincipal' en el 'Context' de SpringSecurity para esta petición Rest (hilo) para poder realizar la 'Autorización'
        //en los métodos 'Action' de los 'Controller' y permitir denegar el acceso según corresponda (decoradores o atributos de SpringSecurity).
        //Según la configuración superior del objeto 'HttpSecurity', solo se permiten conexiones anónimas (sin Token o aquellas en las que Token no se pudo autenticar)
        //para las urls de 'Crear un Nuevo Usuario' y 'Loguear un usuario existente (crea el token para esa sesión del usuario)).
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

}
