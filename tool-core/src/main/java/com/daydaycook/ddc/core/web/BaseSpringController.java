package com.daydaycook.ddc.core.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

public class BaseSpringController {
	public Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	public ModelAndView fail(String viewName, String key, Object data, String key1, Object data1) {
		ModelAndView view = success(viewName);
		view.addObject(key1, data1);
		return view.addObject(key, data);
	}

	public ModelAndView fail(String viewName, String key, Object data) {
		ModelAndView view = success(viewName);
		return view.addObject(key, data);
	}

	/**
	 * 错误处理方法
	 * 
	 * @date 2016年8月16日 下午3:41:58
	 * @param code
	 * @param error
	 * @return
	 */
	public <T> JsonResult<T> fail(int code, String error) {
		JsonResult<T> result = new JsonResult<T>();
		result.setCode(code);
		result.setError(error);
		return result;
	}

	public <T> JsonResult<T> success(String success, T t) {
		JsonResult<T> result = new JsonResult<T>();
		result.setSuccess(success);
		result.setData(t);
		return result;
	}

	public <T> JsonResult<T> success(T t) {
		JsonResult<T> result = new JsonResult<T>();
		result.setData(t);
		return result;
	}

	public <T> JsonResult<T> success() {
		JsonResult<T> result = new JsonResult<T>();
		return result;
	}

	/**
	 * 错误处理方法
	 * 
	 * @date 2016年8月16日 下午3:41:58
	 * @param code
	 * @param error
	 * @return
	 */
	public void fail(JsonResult<? extends Object> result, int code, String error) {
		result.setCode(code);
		result.setError(error);
	}

	public ModelAndView success(String viewName, String key, Object data, String key1, Object data1) {
		ModelAndView view = success(viewName);
		view.addObject(key1, data1);
		return view.addObject(key, data);
	}

	public ModelAndView success(String viewName, String key, Object data, String key1, Object data1, String key2,
			Object data2) {
		ModelAndView view = success(viewName);
		view.addObject(key1, data1);
		view.addObject(key2, data2);
		return view.addObject(key, data);
	}

	public ModelAndView success(String viewName, String key, Object data) {
		ModelAndView view = success(viewName);
		return view.addObject(key, data);
	}

	public ModelAndView success(String viewName) {
		ModelAndView view = new ModelAndView();
		view.setViewName(viewName);
		return view;
	}
}
