package com.example.algamoney.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {
	
	@Autowired
	AlgamoneyApiProperty algamoneyApiProperty; //define a propriedade dinamicamente
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		
		//para mover o refresh token para o cookie
		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();
		
		//para remover o refresh token do body -> o DefaultOAuth2AccessToken tem o setRefreshToken.
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
		
		String refreshToken = body.getRefreshToken().getValue();
		adicionarRefreshTokenNoCookie(refreshToken, req, resp);
		removerRefreshTokenDoBody(token);
		return body;
	}

	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);
		
	}

	private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("====PostProcessor===");
		System.out.println("HTTPS: " + algamoneyApiProperty.getSeguranca().isEnableHttps());
		System.out.println("Origem Permitida: " + algamoneyApiProperty.getOriginPermitida());
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps()); // TODO: Mudar para true em producao
		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
		refreshTokenCookie.setMaxAge(2592000);
		System.out.println("====PostProcessor===");
		resp.addCookie(refreshTokenCookie);
	}

}
