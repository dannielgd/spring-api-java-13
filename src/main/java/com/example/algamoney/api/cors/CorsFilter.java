package com.example.algamoney.api.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter{
	
	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;
	
	//private String originPermitida = algamoneyApiProperty.getOriginPermitida(); //TODO: Configurar para diferentes ambientes
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		System.out.println("HTTPS " + algamoneyApiProperty.getSeguranca().isEnableHttps());
		System.out.println("Origem " + algamoneyApiProperty.getOriginPermitida());
		
		response.setHeader("Access-Control-Allow-Origin", algamoneyApiProperty.getOriginPermitida()); //permitir a origem
		response.setHeader("Access-Control-Allow-Credentials", "true"); //para permitir o cookie
		
		//falar que pode se for options
		if ("OPTIONS".equals(request.getMethod()) && algamoneyApiProperty.getOriginPermitida().equals(request.getHeader("Origin"))) {
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
			response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
			response.setHeader("Access-Control-Max-Age", "3600");
			
			response.setStatus(HttpServletResponse.SC_OK);
			
		} else {
			chain.doFilter(req, resp);
		}	
	}
}
