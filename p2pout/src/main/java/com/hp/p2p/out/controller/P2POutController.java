/**
 * 
 */
package com.hp.p2p.out.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;

/**
 * @author wangxif
 *
 */
@Controller
public class P2POutController {
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/**", method = RequestMethod.GET)
	public ResponseEntity<Object> get(WebRequest request, HttpEntity<byte[]> requestEntity) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		HttpHeaders headers = new HttpHeaders();
		headers.putAll(requestEntity.getHeaders());
		headers.add("testhead", "testvalue");
		HttpEntity<byte[]> newRequest = new HttpEntity<byte[]>(null, headers);
		return restTemplate.exchange("http://localhost:8080/p2pin/p2p/in" + path + "?" + buildQueryString(request), HttpMethod.GET, newRequest, Object.class, extractParameters(request));
//		return restTemplate.getForObject("http://localhost:8080/p2pin/p2p/in" + path + "?" + buildQueryString(request), Object.class, extractParameters(request));
	}
	
	@RequestMapping(value = "/**", method = RequestMethod.POST)
	public @ResponseBody Object post(WebRequest request, @RequestBody Object body) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		return restTemplate.postForObject("http://localhost:8080/p2pin/p2p/in" + path, body, Object.class);
	}
	
	@RequestMapping(value = "/**", method = RequestMethod.PUT)
	public @ResponseBody void put(WebRequest request, @RequestBody Object body) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		restTemplate.put("http://localhost:8080/p2pin/p2p/in" + path, body);
	}
	
	@RequestMapping(value = "/**", method = RequestMethod.DELETE)
	public @ResponseBody void delete(WebRequest request) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		restTemplate.delete("http://localhost:8080/p2pin/p2p/in" + path);
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
	
	private Map<String, String> extractParameters(WebRequest request) {
		Map<String, String> parameters = new HashMap<String, String>();
		Iterator<String> it = request.getParameterNames();
		while (it.hasNext()) {
			String p = (String) it.next();
			parameters.put(p, request.getParameter(p));
		}		
		return parameters;
	}
	
}
