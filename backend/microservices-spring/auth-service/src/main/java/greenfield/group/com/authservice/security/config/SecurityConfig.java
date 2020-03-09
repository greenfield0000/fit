package greenfield.group.com.authservice.security.config;

import greenfield.group.com.authservice.security.security.JwtAccountDetailsService;
import greenfield.group.com.authservice.security.security.jwt.JwtConfigurer;
import greenfield.group.com.authservice.security.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Security configuration class for JWT based Spring Security application.
 *
 * @author Ivanov Roman
 * @version 1.0
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // Пути доступные без авторизации пользователя
    private static final List<String> LOGIN_ENDPOINT_LIST = Arrays.asList("/login", "/registry", "/logout");
    private static final List<String> KLADR_ENDPOINT_LIST = Arrays.asList(
            "/getRegion", "/getDistrict", "/getCity", "/getStreet", "/getBuilding"
    );
    private static Set<String> NO_AUTH_ENDPOINT_LIST = new HashSet<>();
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccountDetailsService jwtAccountDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, JwtAccountDetailsService jwtAccountDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAccountDetailsService = jwtAccountDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

        NO_AUTH_ENDPOINT_LIST.addAll(LOGIN_ENDPOINT_LIST);
        NO_AUTH_ENDPOINT_LIST.addAll(KLADR_ENDPOINT_LIST);
    }

    // регистрируем нашу реализацию UserDetailsService
    // а также PasswordEncoder для приведения пароля в формат SHA1
    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(jwtAccountDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] antPatterns = NO_AUTH_ENDPOINT_LIST.toArray(new String[NO_AUTH_ENDPOINT_LIST.size()]);
        http
                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(antPatterns).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .cors().disable()
                .formLogin().disable()
                .logout().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        String[] antPatterns = NO_AUTH_ENDPOINT_LIST.toArray(new String[NO_AUTH_ENDPOINT_LIST.size()]);
        web.ignoring()
                .antMatchers(antPatterns);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        CorsConfiguration defaultCorsConfigurations = corsConfiguration.applyPermitDefaultValues();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.HEAD.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name()));
        corsConfiguration.setMaxAge(3600L);
        defaultCorsConfigurations.combine(corsConfiguration);
        source.registerCorsConfiguration("/**", corsConfiguration); // you restrict your path here
        return source;
    }

}

