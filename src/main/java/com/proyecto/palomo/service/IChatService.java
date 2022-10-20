package com.proyecto.palomo.service;

import com.proyecto.palomo.model.Chat;

import java.util.List;

public interface IChatService {
    public Chat get(Long id);
    public Chat createSimple(Chat chat) throws Exception;
    public Chat createGroup(Chat chat);
    public Chat getSimpleByUsers(Long userId, Long secondUserId);
    public List<Chat> getAllChatsByUserId(Long id, Integer page);
    public Chat addUserToChat(Long userId, Long chatId);
    public void deleted(Long id);

    public boolean isExist(Long id);
    public boolean isExistUserInChat(Long userId, Long chatId);
}
