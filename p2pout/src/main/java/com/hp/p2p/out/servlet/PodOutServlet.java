/**
 * 
 */
package com.hp.p2p.out.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.springframework.web.util.UrlPathHelper;

/**
 * @author wangxif
 *
 */
public class PodOutServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6597873915743641402L;
	
	private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	private static final int TIMEOUT = 20000; // Set time out
	
	private static final int SO_TIMEOUT = 60000; // Set transaction time out  

	private static HttpClient httpClient;
	
	{
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

		PoolingClientConnectionManager connectionMgr = new PoolingClientConnectionManager(schemeRegistry);
		// Increase max total connection to 800
		connectionMgr.setMaxTotal(800);
		// Increase default max connection per route to 400
		connectionMgr.setDefaultMaxPerRoute(400);

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);

		httpClient = new DefaultHttpClient(connectionMgr);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
		String url = getUrl(servletRequest);
		if (url == null) {
			return;
		}
		HttpUriRequest getRequest = copyRequest(servletRequest, new HttpGet(url));
		HttpResponse response = httpClient.execute(getRequest);
		copyResponse(response, servletResponse);
	}
	
	/**
	 * @param response
	 * @param servletResponse
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@SuppressWarnings("deprecation")
	private void copyResponse(HttpResponse fromResponse, HttpServletResponse toResponse) throws IllegalStateException, IOException {
		toResponse.setStatus(fromResponse.getStatusLine().getStatusCode(), fromResponse.getStatusLine().getReasonPhrase());
		HttpEntity responseEntity = fromResponse.getEntity();
		Header header = responseEntity.getContentEncoding();
		if (header != null) {
			toResponse.setHeader(responseEntity.getContentEncoding().getName(), responseEntity.getContentEncoding().getValue());
		}
		toResponse.setContentType(responseEntity.getContentType().getValue());
		toResponse.setContentLength((int) responseEntity.getContentLength());
		InputStream contentStream = responseEntity.getContent();
		try {
			IOUtils.copy(contentStream, toResponse.getOutputStream());
		} finally {
			IOUtils.closeQuietly(contentStream);
		}
	}

	/**
	 * @param servletRequest
	 * @param getRequest
	 */
	private HttpUriRequest copyRequest(HttpServletRequest fromRequest, HttpUriRequest toRequest) {
		toRequest.setHeaders(getHeaders(fromRequest));
		return toRequest;
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
	
	private Header[] getHeaders(HttpServletRequest request) {
		List<Header> headers = new ArrayList<Header>();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			if (HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(headerName)) {
				continue;
			}
			String headerValue = request.getHeader(headerName);
			Header header = new BasicHeader(headerName, headerValue);
			headers.add(header);
		}
		return (Header[]) headers.toArray(new Header[headers.size()]);
	}

}
