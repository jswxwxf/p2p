/**
 * 
 */
package com.hp.p2p.in.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

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
	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private PodInTemplate podTemplate;

	/* (non-Javadoc)
	 * @see org.springframework.web.HttpRequestHandler#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void handleRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
		podTemplate.receiveRequest(servletRequest, servletResponse);
	}

}
