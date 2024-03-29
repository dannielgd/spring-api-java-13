package com.example.algamoney.api.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenPreProcessorFilter implements Filter {
	
	//Pegando o refreshToken do Cookie
	//Ele vem no cookie e colocamos na requisição e depois tiramos.
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		//se existe o cookie refresh token, pega o valor dele
		if("/oauth/token".equalsIgnoreCase(req.getRequestURI())
				&& "refresh_token".equals(req.getParameter("grant_type"))
				&& req.getCookies() != null) {
			for(Cookie cookie : req.getCookies()) {
				if(cookie.getName().equals("refreshToken")) {
					String refreshToken = cookie.getValue();
					//adicionar dentro da requisição, criando outra requisição
					//substitui a requisição
					req = new MyServletRequestWrapper(req, refreshToken);
				}
			}
		}
		
		//momento em que substituímos a requisição
		//continua a cadeia do filtro
		chain.doFilter(req, response);
	}
	
	//quando a requisição precisar do parâmetro, eu crio outro e entrego para ela.
	//OBS.: a requisição, depois de enviada, ela não pode ser mais alterada.
	static class MyServletRequestWrapper extends HttpServletRequestWrapper {

		String refreshToken;
		
		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}
		
		@Override
		public Map<String, String[]> getParameterMap() {
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] { refreshToken });
			map.setLocked(true); //trava na requisição
			return map;
		}
		
	}

}
