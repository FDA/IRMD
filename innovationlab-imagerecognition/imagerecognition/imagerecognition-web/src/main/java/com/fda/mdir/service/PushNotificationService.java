package com.fda.mdir.service;

import java.util.List;

public interface PushNotificationService {
	
	String register(String userName, String registrationToken, String devicePlatform);
	
	String sendNotification(String title, String message, List<String> recipients, String environment);

}
