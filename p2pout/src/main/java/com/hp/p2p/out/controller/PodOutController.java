/**
 * 
 */
package com.hp.p2p.out.controller;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;

/**
 * @author wangxif
 *
 */
@Controller
public class PodOutController {
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/**")
	public ResponseEntity<Object> forward(WebRequest request, HttpEntity<Object> requestEntity, HttpServletRequest servletRequest, HttpServletRequest servletResponse) throws IOException {
		String url = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		url = "http://localhost:8080/p2pin/p2p/in" + url + "?" + buildQueryString(request);
		return restTemplate.exchange(url, HttpMethod.valueOf(servletRequest.getMethod()), requestEntity, Object.class);
//		restTemplate.execute(url, HttpMethod.valueOf(servletRequest.getMethod()), requestCallback, responseExtractor, urlVariables)
	}
	
	private String buildQueryString(WebRequest request) {
		StringBuilder sb = new StringBuilder("&");
		Iterator<String> it = request.getParameterNames();
		while (it.hasNext()) {
			String p = (String) it.next();
			sb.append(p).append("={").append(p).append("}");
		}
		return sb.substring(1);
	}
	
}
