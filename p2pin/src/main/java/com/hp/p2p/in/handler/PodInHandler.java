/**
 * 
 */
package com.hp.p2p.in.handler;

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
public class PodInHandler implements HttpRequestHandler {
	
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PodInHandler.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	/**
	 * @param urlPathHelper the urlPathHelper to set
	 */
	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		this.urlPathHelper = urlPathHelper;
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
	
	private String getUrl(HttpServletRequest request) {
		String url = urlPathHelper.getServletPath(request);
		String queryString = urlPathHelper.getOriginatingQueryString(request);
		if (!StringUtils.isEmpty(queryString)) {
			url += "?" + queryString;
		}
		return url.substring("/p2p/in".length());
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.HttpRequestHandler#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void handleRequest(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) throws ServletException, IOException {
		if (!this.allow(urlPathHelper.getServletPath(servletRequest))) {
			return;
		}
		String url = getUrl(servletRequest);
		url = "http://localhost:8080" + url.replace("TestInApp", "inapp");
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
