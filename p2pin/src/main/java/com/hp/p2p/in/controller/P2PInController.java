/**
 * 
 */
package com.hp.p2p.in.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;

import com.hp.p2p.domain.CallLog;
import com.hp.p2p.repository.CallLogRepository;

/**
 * @author wangxif
 *
 */
@Controller
@RequestMapping("/p2p/in")
public class P2PInController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private CallLogRepository callLogRepository;
	
	@RequestMapping(value = "/**", method = RequestMethod.GET)
	public @ResponseBody Object get(WebRequest request, HttpEntity<byte[]> requestEntity) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		path = path.substring("/p2p/in".length());
		if (this.allow(path)) {
			// replace /TestInApp/inapp/items/1 to /inapp/inapp/items/1
			path = "http://localhost:8080" + path.replace("TestInApp", "inapp") + "?" + buildQueryString(request);
			return restTemplate.getForObject(path, Object.class, extractParameters(request));
		}
		return null;
	}
	
	@RequestMapping(value="/**", method=RequestMethod.POST)
	public @ResponseBody Object post(WebRequest request, @RequestBody Object body) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		path = path.substring("/p2p/in".length());
		if (this.allow(path)) {
			// replace /TestInApp/inapp/items/1 to /inapp/inapp/items/1
			path = "http://localhost:8080" + path.replace("TestInApp", "inapp");
			return restTemplate.postForObject(path, body, Object.class);
		}
		return null;
	}
	
	@RequestMapping(value="/**", method=RequestMethod.PUT)
	public @ResponseBody void put(WebRequest request, @RequestBody Object body) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		path = path.substring("/p2p/in".length());
		if (this.allow(path)) {
			// replace /TestInApp/inapp/items/1 to /inapp/inapp/items/1
			path = "http://localhost:8080" + path.replace("TestInApp", "inapp");
			restTemplate.put(path, body);
		}
	}
	
	@RequestMapping(value = "/**", method = RequestMethod.DELETE)
	public @ResponseBody void delete(WebRequest request) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		path = path.substring("/p2p/in".length());
		if (this.allow(path)) {
			// replace /TestInApp/inapp/items/1 to /inapp/inapp/items/1
			path = "http://localhost:8080" + path.replace("TestInApp", "inapp");
			restTemplate.delete(path);
		}
	}
	
	private boolean allow(String path) {
		if (path.matches(".+/inapp/items/.+")) {
			return true;
		}
		if (path.matches(".+/inapp/items")) {
			return true;
		}
		return false;
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
