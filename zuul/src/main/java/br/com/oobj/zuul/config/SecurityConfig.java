package br.com.oobj.zuul.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.oobj.auth.jwt.JwtAuthenticationEntryPoint;
import br.com.oobj.auth.jwt.JwtAuthenticationTokenFilter;
import br.com.oobj.auth.jwt.JwtTokenUtil;
import br.com.oobj.zuul.security.repository.AplicativoRepository;
import br.com.oobj.zuul.security.service.AplicativoServiceImpl;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//@ComponentScan(basePackages = {"br.com.oobj.auth", "br.com.oobj.zuul.security"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("#{'${authorized.static.endpoints:/docs/**,/webjars/**,/css/**,/js/**,/images/**,/favicon.ico,"
			+ "/swagger**,/swagger-resources/**,/v2/**,/configuration/**,/uploadError,/error}'.split(',')}")
	protected List<String> authorizedStaticEndpoints;

	@Value("#{'${authorized.endpoints:/authenticate}'.split(',')}")
	protected List<String> authorizedEndpoints;
	
	@Value("${jwt.expiration:30}") 
	private int expiration;
	
	@Autowired
	private AplicativoRepository aplicativoRepository;

	// ~~~~~~~~~~~~~~~~~~~~
	
	// Configuracoes de autorizacao
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint()).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
		
		String[] endpoints = authorizedEndpoints.toArray(new String[0]);
		if (endpoints != null && endpoints.length > 0) {
			http.authorizeRequests().antMatchers(endpoints).permitAll();
		}
		http.authorizeRequests().anyRequest().authenticated();

		http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.headers().cacheControl();
	}

	// Configuracoes de autenticacao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	// Configuracoes de recursos estaticos(js, css, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {
		String[] endpoints = authorizedStaticEndpoints.toArray(new String[0]);

		if (endpoints != null && endpoints.length > 0) {
			web.ignoring().antMatchers(endpoints);
		}
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	/**
	 * @return filtro responsavel por verificar se a requisicao contem um token JWT
	 *         valido
	 */
	@Bean
	public JwtAuthenticationTokenFilter authenticationTokenFilter() {
		return new JwtAuthenticationTokenFilter(userDetailsService(), jwtTokenUtil());
	}

	@Bean
	public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}

	@Bean
	@Primary
	public JwtTokenUtil jwtTokenUtil() {
		return new JwtTokenUtil(expiration);
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new AplicativoServiceImpl(aplicativoRepository);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}
