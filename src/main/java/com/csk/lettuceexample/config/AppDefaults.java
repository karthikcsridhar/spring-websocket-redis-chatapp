package com.csk.lettuceexample.config;

public class AppDefaults {
	
	public static final String REDIS_HOST = "localhost";
	public static final Integer REDIS_PORT = 6379;
	public static final String REDIS_CHAT_CHANNEL = "globalchat";
	
	public static final String CHAT_USER_SUBSCRIPTION_QUEUE = "/queue/reply";
	
	private AppDefaults(){}
}
