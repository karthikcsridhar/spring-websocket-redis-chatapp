package com.csk.lettuceexample.api;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csk.lettuceexample.model.ChatMessage;
import com.csk.lettuceexample.model.Message;
import com.csk.lettuceexample.model.OutputMessage;
import com.csk.lettuceexample.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ChatController {

	@Autowired
	ChatService chatService;

	@MessageMapping("/chat")
	@SendTo("/topic/messages")
	public OutputMessage send(Message message) throws Exception {

		return new OutputMessage(message.getFrom(), message.getText(), new Timestamp(System.currentTimeMillis()));

	}

	@RequestMapping(value = "/sendMessageToUser", method = RequestMethod.POST)
	@ResponseBody
	public ChatMessage sayHelloAgain(@RequestBody final ChatMessage message) {

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String messageToJsonString = objectMapper.writeValueAsString(message);

			chatService.publish(message.getToChannels(), messageToJsonString);

			return message;

		} catch (JsonProcessingException e) {

			e.printStackTrace();
			return message;

		}
	}
}
