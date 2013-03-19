/**
 * 
 */
package com.hp.p2p.out.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

/**
 * @author wangxif
 *
 */
@Component
public class PodOutTemplate {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	@Autowired
	private PodRequestFactory requestFactory;
	
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

	private Header[] getHeaders(HttpServletRequest req) {
		List<Header> headers = new ArrayList<Header>();
		Enumeration<?> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			if (HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(headerName)) {
				continue;
			}
			String headerValue = req.getHeader(headerName);
			Header header = new BasicHeader(headerName, headerValue);
			headers.add(header);
		}
		return (Header[]) headers.toArray(new Header[headers.size()]);
	}
	
	/**
	 * @return the requestFactory
	 */
	public PodRequestFactory getRequestFactory() {
		return requestFactory;
	}

	public void forwardRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
		String url = getUrl(servletRequest);
		if (url == null) {
			return;
		}
		HttpMethod method = HttpMethod.valueOf(servletRequest.getMethod());
		switch (method) {
		case GET:
			this.forwardGet(url, servletRequest, servletResponse);
			break;
		case POST:
			this.forwardPost(url, servletRequest, servletResponse);
			break;
		case PUT:
			this.forwardPut(url, servletRequest, servletResponse);
			break;
		case DELETE:
			this.forwardDelete(url, servletRequest, servletResponse);
			break;
		case HEAD:
			this.forwardHead(url, servletRequest, servletResponse);
		default:
			break;
		}
	}
	
	public void forwardGet(String url, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
		HttpGet getRequest = (HttpGet) getRequestFactory().createRequest(HttpMethod.GET, url);
		// copy request headers
		getRequest.setHeaders(getHeaders(servletRequest));
		InputStream inStream = null;
		try {
			// forward request
			HttpResponse response = requestFactory.getClient().execute(getRequest);
			HttpEntity responseEntity = response.getEntity();
			// copy response headers
			Header header = responseEntity.getContentEncoding();
			if (header != null) {
				servletResponse.setHeader(responseEntity.getContentEncoding().getName(), responseEntity.getContentEncoding().getValue());
			}
			servletResponse.setContentType(responseEntity.getContentType().getValue());
			servletResponse.setContentLength((int) responseEntity.getContentLength());
			// copy response body
			inStream = response.getEntity().getContent();
			servletResponse.setStatus(response.getStatusLine().getStatusCode());
			IOUtils.copy(inStream, servletResponse.getOutputStream());
		} catch (IOException ioe) {
			getRequest.abort();
			throw ioe;
		} finally {
			IOUtils.closeQuietly(inStream);
		}
	}
	
	public void forwardPost(String url, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
		HttpPost postRequest = (HttpPost) getRequestFactory().createRequest(HttpMethod.POST, url);
		postRequest.setHeaders(getHeaders(servletRequest));
		InputStreamEntity entity = new InputStreamEntity(servletRequest.getInputStream(), servletRequest.getContentLength());
		postRequest.setEntity(entity);
		InputStream inStream = null;
		try {
			HttpResponse response = requestFactory.getClient().execute(postRequest);
			HttpEntity responseEntity = response.getEntity();
			Header header = responseEntity.getContentEncoding();
			if (header != null) {
				servletResponse.setHeader(responseEntity.getContentEncoding().getName(), responseEntity.getContentEncoding().getValue());
			}
			servletResponse.setContentType(responseEntity.getContentType().getValue());
			servletResponse.setContentLength((int) responseEntity.getContentLength());
			servletResponse.setStatus(response.getStatusLine().getStatusCode());
			inStream = response.getEntity().getContent();
			IOUtils.copy(inStream, servletResponse.getOutputStream());
		} catch (IOException ioe) {
			postRequest.abort();
			throw ioe;
		} finally {
			IOUtils.closeQuietly(inStream);
		}
	}
	
	public void forwardPut(String url, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
		HttpPut putRequest = (HttpPut) getRequestFactory().createRequest(HttpMethod.PUT, url);
		putRequest.setHeaders(getHeaders(servletRequest));
		InputStreamEntity entity = new InputStreamEntity(servletRequest.getInputStream(), servletRequest.getContentLength());
		putRequest.setEntity(entity);
		InputStream inStream = null;
		try {
			HttpResponse response = requestFactory.getClient().execute(putRequest);
			HttpEntity responseEntity = response.getEntity();
//			Header header = responseEntity.getContentEncoding();
//			if (header != null) {
//				servletResponse.setHeader(responseEntity.getContentEncoding().getName(), responseEntity.getContentEncoding().getValue());
//			}
//			servletResponse.setContentType(responseEntity.getContentType().getValue());
//			servletResponse.setContentLength((int) responseEntity.getContentLength());
			servletResponse.setStatus(response.getStatusLine().getStatusCode());
			inStream = response.getEntity().getContent();
			IOUtils.copy(inStream, servletResponse.getOutputStream());
		} catch (IOException ioe) {
			putRequest.abort();
			throw ioe;
		} finally {
			IOUtils.closeQuietly(inStream);
		}
	}
	
	public void forwardDelete(String url, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
		HttpDelete deleteRequest = (HttpDelete) getRequestFactory().createRequest(HttpMethod.DELETE, url);
		deleteRequest.setHeaders(getHeaders(servletRequest));
		InputStream inStream = null;
		try {
			HttpResponse response = requestFactory.getClient().execute(deleteRequest);
//			HttpEntity responseEntity = response.getEntity();
//			Header header = responseEntity.getContentEncoding();
//			if (header != null) {
//				servletResponse.setHeader(responseEntity.getContentEncoding().getName(), responseEntity.getContentEncoding().getValue());
//			}
//			servletResponse.setContentType(responseEntity.getContentType().getValue());
//			servletResponse.setContentLength((int) responseEntity.getContentLength());
			servletResponse.setStatus(response.getStatusLine().getStatusCode());
			inStream = response.getEntity().getContent();
			IOUtils.copy(inStream, servletResponse.getOutputStream());
		} catch (IOException ioe) {
			deleteRequest.abort();
			throw ioe;
		} finally {
			IOUtils.closeQuietly(inStream);
		}
	}
	
	public void forwardHead(String url, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
		HttpHead headRequest = (HttpHead) getRequestFactory().createRequest(HttpMethod.HEAD, url);
		headRequest.setHeaders(getHeaders(servletRequest));
		InputStream inStream = null;
		try {
			HttpResponse response = requestFactory.getClient().execute(headRequest);
			HttpEntity responseEntity = response.getEntity();
			Header header = responseEntity.getContentEncoding();
			if (header != null) {
				servletResponse.setHeader(responseEntity.getContentEncoding().getName(), responseEntity.getContentEncoding().getValue());
			}
			servletResponse.setContentType(responseEntity.getContentType().getValue());
			servletResponse.setContentLength((int) responseEntity.getContentLength());
			servletResponse.setStatus(response.getStatusLine().getStatusCode());
			inStream = response.getEntity().getContent();
			IOUtils.copy(inStream, servletResponse.getOutputStream());
		} catch (IOException ioe) {
			headRequest.abort();
			throw ioe;
		} finally {
			IOUtils.closeQuietly(inStream);
		}
	}
	
}
