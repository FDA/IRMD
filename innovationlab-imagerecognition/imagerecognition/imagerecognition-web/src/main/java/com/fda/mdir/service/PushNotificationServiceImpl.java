/**
 * 
 */
package com.fda.mdir.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fda.mdir.constants.LABConstants;
import com.fda.mdir.init.AppContextListener;

/**
 * @author jjvirani
 *
 */
@Service
public class PushNotificationServiceImpl implements PushNotificationService {

	private static final String SENDER_APPLICATION_NAME = "OITI_WEB";
	/* (non-Javadoc)
	 * @see com.aurotech.service.PushNotificationService#register(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String register(String userName, String registrationToken,
			String devicePlatform) {
		
		String responseText = null;
		
		LABConstants narmsConstants = (LABConstants)AppContextListener.getSpringContext().getBean("labConstants");
        String serverUrl = (String)narmsConstants.getCONSTANTS().get("PUSH_NOTIFICATION_SERVER");
        
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(serverUrl+"/push/register");
		httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
		
		Map<String, Object> jsonBodyMap = new LinkedHashMap<String, Object>();
		
		jsonBodyMap.put("userName", userName);
		jsonBodyMap.put("registrationToken", registrationToken);
		jsonBodyMap.put("devicePlatform", devicePlatform);
		jsonBodyMap.put("senderApplicationName", SENDER_APPLICATION_NAME);
		
		
		CloseableHttpResponse response = null;
			
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			
			StringEntity entity = new StringEntity(objectMapper.writeValueAsString(jsonBodyMap));
			 
			httpPost.setEntity(entity);
			
			response = httpclient.execute(httpPost);
			
			responseText = EntityUtils.toString(response.getEntity());
			
			response.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		   
		return responseText;
	}

	/* (non-Javadoc)
	 * @see com.aurotech.service.PushNotificationService#notify(java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public String sendNotification(String title, String message, List<String> recipients,
			String environment) {
		
		String responseText = null;
		
		LABConstants narmsConstants = (LABConstants)AppContextListener.getSpringContext().getBean("labConstants");
        String serverUrl = (String)narmsConstants.getCONSTANTS().get("PUSH_NOTIFICATION_SERVER");
        
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(serverUrl+"/push/notify");
		httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
		
		
		Map<String, Object> jsonNotificationMap = new LinkedHashMap<String, Object>();
		jsonNotificationMap.put("body", message);
		jsonNotificationMap.put("title", title);
		
		Map<String, Object> jsonBodyMap = new LinkedHashMap<String, Object>();
		jsonBodyMap.put("environment", environment);
		jsonBodyMap.put("senderApplicationName", SENDER_APPLICATION_NAME);
		jsonBodyMap.put("recipients", recipients);
		jsonBodyMap.put("notification", jsonNotificationMap);
		
		
		CloseableHttpResponse response = null;
			
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			
			StringEntity entity = new StringEntity(objectMapper.writeValueAsString(jsonBodyMap));
			 
			httpPost.setEntity(entity);
			
			response = httpclient.execute(httpPost);
			
			responseText = EntityUtils.toString(response.getEntity());
			
			response.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		   
		return responseText;
	}

}
