package com.daydaycook.ddc.core.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.daydaycook.ddc.core.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebExceptionHandler implements HandlerExceptionResolver {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private String errorPage = "error";

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		StringBuilder errorMessage = new StringBuilder();
		if (handler != null) {
			errorMessage.append(handler);
			errorMessage.append("\n");
			errorMessage.append(request.getRequestURI());
		}
		logger.error(errorMessage.toString(), ex);
		ModelAndView modelAndView = new ModelAndView();
		// JSP格式返回
		if (!isJsonRequest(request, handler)) {
			String viewName = getErrorPage();
			int code = -1;
			if (ex instanceof BusinessException) {
				code = ((BusinessException) ex).getCode();
			}
			modelAndView.setViewName(viewName);
			modelAndView.addObject("ex", ex);
			modelAndView.addObject("code", code);
			return modelAndView;
		} else {
			// JSON格式返回
			try {
				int code = -1;
				JsonResult<Object> jsonResult = new JsonResult<Object>();
				jsonResult.setError("发生未知错误!");
				if (ex instanceof BusinessException) {
					code = ((BusinessException) ex).getCode();
					jsonResult.setError(ex.getMessage());
				}
				jsonResult.setCode(code);
				ObjectMapper objectMapper = new ObjectMapper();
				String json = objectMapper.writeValueAsString(jsonResult);

				String charset = request.getCharacterEncoding();
				response.setCharacterEncoding(charset);
				response.setContentType("application/json");
				OutputStream output = response.getOutputStream();
				output.write(json.getBytes(charset));
				output.flush();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return modelAndView;
	}

	private boolean isJsonRequest(HttpServletRequest request, Object handler) {
		String uri = request.getRequestURI();
		if (uri != null && uri.endsWith(".json")) {
			return true;
		}
		if (handler != null && handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			ResponseBody responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class);
			if (responseBody != null)
				return true;
		}
		if (request.getHeader("accept").indexOf("application/json") > -1)
			return true;
		if (request.getHeader("X-Requested-With") != null) {
			return request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1;
		}
		return false;
	}

	public String getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}
}