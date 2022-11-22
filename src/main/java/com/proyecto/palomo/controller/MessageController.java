package com.proyecto.palomo.controller;

import com.proyecto.palomo.dto.message.MessageResponse;
import com.proyecto.palomo.mapper.MessageMapper;
import com.proyecto.palomo.model.Chat;
import com.proyecto.palomo.model.User;
import com.proyecto.palomo.security.jwt.JwtUtils;
import com.proyecto.palomo.service.impl.ChatService;
import com.proyecto.palomo.service.impl.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MessageController {
    private final MessageService service;
    private final MessageMapper messageMapper;
    private final JwtUtils jwtUtils;
    private final ChatService chatService;

    @GetMapping("/chat/{chatId}/message")
    public ResponseEntity<List<MessageResponse>> getMessages(@RequestHeader(name = "Authorization") String token, @PathVariable("chatId") Long chatId, @RequestParam("page") Integer page){
        Chat chat;
        try {
            chat = chatService.get(chatId);
            User u = chat.getUsers().stream().filter(user -> user.getUserId().equals(getIdByToken(token))).findAny().orElseThrow(() -> {return new AccessDeniedException("");});
        }catch(Exception e){
            throw new EntityNotFoundException();
        }
        List<MessageResponse> messages = service.getByPage(page, chatId).stream()
                .map(messageMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(messages);
    }

    private long getIdByToken(String token) {
        return Long.parseLong(jwtUtils.getUserIdFromJwtToken(token.substring(7)).trim());
    }
}
