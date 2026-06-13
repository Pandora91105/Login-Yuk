/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loginyuk.backend.controller;

import com.loginyuk.backend.model.ChatMessage;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Callista A. Putri
 */
@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendToRoom(@Payload ChatMessage message) {

        System.out.println("Received:");
        System.out.println("sender = " + message.getSender());
        System.out.println("content = " + message.getContent());
        System.out.println("roomId = " + message.getRoomId());

        messagingTemplate.convertAndSend(
            "/topic/rooms/" + message.getRoomId(),
            message
        );
    }
    /*
    @MessageMapping("/chat.send")
    public void sendToRoom(@Payload ChatMessage message, Principal principal) {
        message.setSender(principal.getName());
        messagingTemplate.convertAndSend("/topic/rooms/" + message.getRoomId(), message);
    }
    */
    
    @MessageMapping("/chat.private.{targetUser}")
    public void sendPrivate(@DestinationVariable String targetUser,
                            @Payload ChatMessage message, Principal sender) {
        message.setSender(sender.getName());
        messagingTemplate.convertAndSendToUser(targetUser, "/queue/messages", message);
    }

}
