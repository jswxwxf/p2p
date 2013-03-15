/**
 * 
 */
package com.hp.p2p.out.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UrlPathHelper;

/**
 * @author wangxif
 *
 */
@Component
public class PodOutHandler implements HttpRequestHandler {
	
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PodOutHandler.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	/**
	 * @param urlPathHelper the urlPathHelper to set
	 */
	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		this.urlPathHelper = urlPathHelper;
	}
	
	private String getUrl(HttpServletRequest request) {
		String url = urlPathHelper.getRequestUri(request);
		if (url.equals("/") || url.equals("/favicon.ico")) {
			return null;
		}
		String queryString = urlPathHelper.getOriginatingQueryString(request);
		if (!StringUtils.isEmpty(queryString)) {
			url += "?" + queryString;
		}
		return "http://localhost:8080/p2pin/p2p/in" + url;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.HttpRequestHandler#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void handleRequest(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) throws ServletException, IOException {
		String url = getUrl(servletRequest);
		if (url == null) {
			return;
		}
//		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//		ClientHttpRequest request = requestFactory.createRequest(toURI(url), HttpMethod.valueOf(servletRequest.getMethod()));
//		PodHttpUtils.copyRequest(servletRequest, request);
//		ClientHttpResponse response = request.execute();
//		PodHttpUtils.copyResponse(response, servletResponse);
		restTemplate.execute(url, HttpMethod.valueOf(servletRequest.getMethod()), new RequestCallback() {
			@Override
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				PodHttpUtils.copyRequest(servletRequest, request);
			}
		}, null);
//		}, new ResponseExtractor<Object>() {
//			@Override
//			public Object extractData(ClientHttpResponse response) throws IOException {
//				PodHttpUtils.copyResponse(response, servletResponse);
//				return null;
//			}
//		});
	}
	
	private URI toURI(String url) throws ServletException {
		try {
			return new URI(url);
		} catch (URISyntaxException e) {
			throw new ServletException();
		}
	}
	

}
