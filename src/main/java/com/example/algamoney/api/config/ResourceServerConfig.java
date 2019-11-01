package com.example.algamoney.api.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Profile("oauth-security")
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true) //habilita a segurança nos métodos
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
//	@Autowired
//	private UserDetailsService userDetailsService; //passou para o AuthorizationServerConfig
	
//	@Autowired
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////		auth.inMemoryAuthentication()
////			.withUser("admin").password("admin").roles("ROLE");
//		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors() //adicionar o cors aqui para deixar o cors configurado aqui nesta classe!
			.and()
			.authorizeRequests()
			.antMatchers("/categorias").permitAll() //permitir a url para solicitar sem logar
			.anyRequest().authenticated() //qualquer url, deve estar autenticado
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //não mantén nenhum estado de sessão
			.and()
			.csrf().disable(); //desabilita o cross side request forward - Não tem a parte web na aplicação.
	}
	
	@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.stateless(true);
		}
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//	    User.UserBuilder builder = User.withDefaultPasswordEncoder();
//	    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//	    manager.createUser(builder.username("admin").password("admin").roles("ROLE").build());
//	    return manager;
//	}
	
	@Bean //habilita a segurança nos métodos
	public MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}
	
	@Bean //Faz toda a configuração de cors . Declara n o config que recebe http e no authorizationconfig.
	public CorsFilter corsFilter() {
		
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setMaxAge(3600L);
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:4100", "http://localhot:8000"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config); //intercepta todos os paths da aplicação
		
		return new CorsFilter(source);
	}
}
