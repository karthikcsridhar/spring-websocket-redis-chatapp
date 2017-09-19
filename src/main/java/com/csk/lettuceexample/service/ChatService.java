package com.csk.lettuceexample.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.csk.lettuceexample.customimpl.LettuceRedisPubSubListener;

import io.lettuce.core.pubsub.RedisPubSubListener;

@Service
public class ChatService {
	
	Map<String, RedisPubSubListener<String, String>> sessionToPubSubListenerMap = new ConcurrentHashMap<String, RedisPubSubListener<String, String>>();
	
	@Autowired
	private LettuceRedisService redisService;
	
	public void publish(String channel, String message){
		redisService.publish(channel, message);
	}
	
	public void publish(String[] channels, String message){
		redisService.publish(channels, message);
	}
	
	public void subscribeUserToChannels(String[] channels, String sessionId, String subscriptionDestination, SimpMessagingTemplate simpMessagingTemplate){
		
		RedisPubSubListener<String, String> redisPubSubListener = new LettuceRedisPubSubListener(sessionId, subscriptionDestination, simpMessagingTemplate, channels);
		redisService.subscribe(redisPubSubListener, channels);
		sessionToPubSubListenerMap.put(sessionId, redisPubSubListener);
		
		
	}

	public void unsubscribeSession(String sessionId){
		
		RedisPubSubListener<String, String> pubSubListener = sessionToPubSubListenerMap.get(sessionId);
		
		if( pubSubListener!= null ){
			redisService.unsubscribe(pubSubListener);
			sessionToPubSubListenerMap.remove(pubSubListener);
		}
		
	}
}
