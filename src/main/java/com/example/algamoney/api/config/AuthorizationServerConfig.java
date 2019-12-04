package com.example.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Profile("oauth-security")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory() //vai buscar o cliente de onde? jbdc é do banco de dados e inMemory é da memória.
			.withClient("angular") //usuário que está acessando
			.secret("$2a$10$G1j5Rf8aEEiGc/AET9BA..xRR.qCpOUzBZoJd8ygbGy6tb3jsMT9G") //senha encriptada em Brypt (@angul@r0)
			.scopes("read", "write") //escopo de acesso. Se só vai ler ou ler e escrever.
			.authorizedGrantTypes("password", "refresh_token") //tipo de autorização usada. Recebe usuário e senha. O angular tem acesso ao usuário e senha.
			.accessTokenValiditySeconds(1800) //fica 30 minutos funcionando
			.refreshTokenValiditySeconds(3600*24)
			.and()
			.withClient("mobile") //usuário que está acessando
			.secret("$2a$10$snSw8BTenEzkbRU8bdS0lusePdZanNSe/B3KWuQ7KftYCDBtLTtaK") //senha encriptada em Brypt (m0b1l30)
			.scopes("read") //escopo de acesso. Se só vai ler ou ler e escrever.
			.authorizedGrantTypes("password", "refresh_token") //tipo de autorização usada. Recebe usuário e senha. O angular tem acesso ao usuário e senha.
			.accessTokenValiditySeconds(1800) //fica 30 minutos funcionando
			.refreshTokenValiditySeconds(3600*24);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore()) //armazena o token(string) e devolve de volta para acessar a api.
			.accessTokenConverter(accessTokenConverter())
			.reuseRefreshTokens(false)
			.userDetailsService(this.userDetailsService) //adiciona o userdsetailsservice
			.authenticationManager(authenticationManager); //para validar se está tudo certo
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("algaworks");
		return accessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
}
