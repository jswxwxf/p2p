package com.hp.p2p.out.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;



public class BackupFrontendServlet extends HttpServlet {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BackupFrontendServlet.class);
	
	private static final String META_DATA_CHECK = "metaDataCheck";
	
	private HttpClient client;
	
	static final int TIMEOUT = 20000;//Set time out  
    static final int SO_TIMEOUT = 60000;//Set transaction time out  
    
    private String backupMetaIntUrl = "";
    private String storageIntUrl = "";
    private String storageAuthIntUrl = "";
    private String keyEscrowIntUrl = "";
	
	@Override
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		//addHealthCheck(new RootCertHealthCheck("palm_webos_root_ca"));
		Context env;
        try {
            env = (Context)new InitialContext().lookup("java:comp/env");
//            backupMetaIntUrl = (String) env.lookup(CommonConstants.BACKUP_META_HOST);
//            storageIntUrl = (String) env.lookup(CommonConstants.STORAGE_HOST);
//            storageAuthIntUrl = (String) env.lookup(CommonConstants.STORAGE_AUTH_HOST);
//            keyEscrowIntUrl = (String) env.lookup(CommonConstants.KEY_ESCROW_HOST);
        } catch (NamingException e) {
            throw new ServletException(e);
        }
		
		if (client==null){
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(
			         new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
			schemeRegistry.register(
			         new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

			PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
			// Increase max total connection to 800
			cm.setMaxTotal(800);
			// Increase default max connection per route to 400
			cm.setDefaultMaxPerRoute(400);
			
			HttpParams params = new BasicHttpParams();  
	        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,TIMEOUT);  
	        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			
	        client = new DefaultHttpClient(cm);
		}
	}
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String newUrl = getNewUrl(req);
	    if (META_DATA_CHECK.equals(newUrl)){
//	        handleMetaRequest(req, resp);
	        return;
	    }
//        logger.debug("GET will be redirected to {}.", newUrl);
		HttpGet getRequest = new HttpGet(newUrl);
		getRequest.setHeaders(getHeaders(req));
		InputStream inStream = null;
		try{
			HttpResponse response = client.execute(getRequest);
			inStream = response.getEntity().getContent();
			resp.setStatus(response.getStatusLine().getStatusCode());
//			IOUtils.copy(inStream,resp.getOutputStream());
		}catch(IOException ioe){
			getRequest.abort();
			throw ioe;
		}finally {
//			IoUtil.closeIgnoreError(inStream);
		}
	}
	
	@Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String newUrl = getNewUrl(req);
//	    logger.debug("PUT will be redirected to {}.", newUrl);
		HttpPut putRequest = new HttpPut(newUrl);
        
		putRequest.setHeaders(getHeaders(req));
		InputStreamEntity entity = new InputStreamEntity(req.getInputStream(), req.getContentLength());
		putRequest.setEntity(entity);
		InputStream inStream = null;
		try{
			HttpResponse response = client.execute(putRequest);
			resp.setStatus(response.getStatusLine().getStatusCode());
			inStream = response.getEntity().getContent();
//			IOUtils.copy(inStream,resp.getOutputStream());
		}catch(IOException ioe){
			putRequest.abort();
			throw ioe;
		}finally {
//			IoUtil.closeIgnoreError(inStream);
		}
	}
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String newUrl = getNewUrl(req);
//        logger.debug("POST will be redirected to {}.", newUrl);
	    HttpPost postRequest = new HttpPost(newUrl);
		postRequest.setHeaders(getHeaders(req));
		InputStreamEntity entity = new InputStreamEntity(req.getInputStream(), req.getContentLength());
		postRequest.setEntity(entity);
		
		InputStream inStream = null;
		try{
			HttpResponse response = client.execute(postRequest);
			resp.setStatus(response.getStatusLine().getStatusCode());
			inStream = response.getEntity().getContent();
//			IOUtils.copy(inStream,resp.getOutputStream());
		}catch(IOException ioe){
			postRequest.abort();
			throw ioe;
		}finally {
//			IoUtil.closeIgnoreError(inStream);
		}
	}
	
	@Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String newUrl=getNewUrl(req);
//        logger.debug("HEAD will be redirected to {}.", newUrl);
		HttpHead headRequest = new HttpHead(newUrl);
		headRequest.setHeaders(getHeaders(req));
		InputStream inStream = null;
		try{
			HttpResponse response = client.execute(headRequest);
			resp.setStatus(response.getStatusLine().getStatusCode());
			inStream = response.getEntity().getContent();
//			IOUtils.copy(inStream,resp.getOutputStream());
		}catch(IOException ioe){
			headRequest.abort();
			throw ioe;
		}finally {
//			IoUtil.closeIgnoreError(inStream);
		}
	}
	
	@Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newUrl=getNewUrl(req);
//        logger.debug("DELETE will be redirected to {}.", newUrl);
        HttpDelete deleteRequest = new HttpDelete(newUrl);
        deleteRequest.setHeaders(getHeaders(req));
        InputStream inStream = null;
        try{
            HttpResponse response = client.execute(deleteRequest);
            resp.setStatus(response.getStatusLine().getStatusCode());
            inStream = response.getEntity().getContent();
//            IOUtils.copy(inStream,resp.getOutputStream());
        }catch(IOException ioe){
            deleteRequest.abort();
            throw ioe;
        }finally {
//            IoUtil.closeIgnoreError(inStream);
        }
    }
	
    private String getNewUrl(HttpServletRequest req) throws ServletException {
        StringBuffer newUrl = new StringBuffer();
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();// /storage
        System.out.println(req.getServletPath());
        System.out.println(req.getContextPath());
        System.out.println(req.getPathInfo());
        System.out.println(req.getRequestURI());
        String newHost = ""; // new host
//        if (path.contains(HEALTH_CHECK_PATH) || path.contains(ABOUT_PATH)) {
//            return META_DATA_CHECK;
//        } else if (path.contains("backupmeta")) {
//            newHost = backupMetaIntUrl;
//        } else if (path.contains("storageauth")) {
//            newHost = storageAuthIntUrl;
//        } else if (path.contains("storage")) {
//            newHost = storageIntUrl;
//        } else if (path.contains("keyescrow")) {
//            newHost = keyEscrowIntUrl;
//        } else {
//            logger.debug("Error : Unknown request url");
//            throw new ServletException("Unknown request url");
//        }
        if (newHost.isEmpty()) {
            logger.error("Cannot get redirected host");
            throw new ServletException("Cannot get redirected host");
        }
		newUrl.append(newHost);
		newUrl.append(path);
		String queryString = req.getQueryString();
		if (queryString != null && !"".equals(queryString)) {
			newUrl.append("?");
			newUrl.append(req.getQueryString());
		}
		
		return newUrl.toString();
	}
	
	private Header[] getHeaders(HttpServletRequest req){
		List<Header> headers = new ArrayList<Header>();
		Enumeration headerNames = req.getHeaderNames();
		while(headerNames.hasMoreElements()){
			String headerName = (String)headerNames.nextElement();
			if(HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(headerName)){
			    continue;
			}
			String headerValue = req.getHeader(headerName);
			Header header = new BasicHeader(headerName, headerValue);
			headers.add(header);
		}
		return (Header[]) headers.toArray(new Header[headers.size()]);
	}
	
	@Override
	public void destroy(){
		super.destroy();
		if (client!=null){
			client.getConnectionManager().shutdown();
			logger.debug("ConnectionManager has been destoried");
		}
	}
}
