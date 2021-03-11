package com.lovemesomecoding.pizzaria.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.lovemesomecoding.pizzaria.exception.ApiErrorResponse;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class CustomAcccessDeniedHandler implements AccessDeniedHandler {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.info("access denied");
		response.setStatus(UNAUTHORIZED.value());
        ObjMapperUtils.getObjectMapper().writeValue(response.getWriter(), new ApiErrorResponse(ApiErrorResponse.FAILURE));
	}

}
