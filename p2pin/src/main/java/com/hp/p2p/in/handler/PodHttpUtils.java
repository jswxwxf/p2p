/**
 * 
 */
package com.hp.p2p.in.handler;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;

/**
 * @author wangxif
 *
 */
public class PodHttpUtils {
	
	public static void copyRequest(HttpServletRequest fromRequest, ClientHttpRequest toRequest) throws IOException {
		// copy headers
		PodHttpUtils.copyRequestHeaders(fromRequest, toRequest);
		// copy body
		PodHttpUtils.copyRequestBody(fromRequest, toRequest);
	}
	
	private static void copyRequestHeaders(HttpServletRequest fromRequest, ClientHttpRequest toRequest) {
		Enumeration<?> headerNames = fromRequest.getHeaderNames();
		HttpHeaders toHeaders = toRequest.getHeaders();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			toHeaders.add(headerName, fromRequest.getHeader(headerName));
		}
	}
	
	private static void copyRequestBody(HttpServletRequest fromRequest, ClientHttpRequest toRequest) throws IOException {
		try {
			IOUtils.copy(fromRequest.getInputStream(), toRequest.getBody());
		} finally {
			IOUtils.closeQuietly(fromRequest.getInputStream());
		}
	}
	
	public static void copyResponse(ClientHttpResponse fromResponse, HttpServletResponse toResponse) throws IOException {
		// copy headers
		PodHttpUtils.copyResponseHeaders(fromResponse, toResponse);
		// copy body
		PodHttpUtils.copyResponseBody(fromResponse, toResponse);
	}
	
	private static void copyResponseHeaders(ClientHttpResponse fromResponse, HttpServletResponse toResponse) {
		HttpHeaders fromHeaders = fromResponse.getHeaders();
		Set<String> keys = fromHeaders.keySet();
		for (String key : keys) {
			fromHeaders.get(key);
			toResponse.setHeader(key, StringUtils.join(fromHeaders.get(key), ","));
		}
	}
	
	private static void copyResponseBody(ClientHttpResponse fromResponse, HttpServletResponse toResponse) throws IOException {
		try {
			IOUtils.copy(fromResponse.getBody(), toResponse.getOutputStream());
		} finally {
			IOUtils.closeQuietly(fromResponse.getBody());
		}
	}

}
