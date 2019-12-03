package com.example.algamoney.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

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
		http.authorizeRequests()
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
}
