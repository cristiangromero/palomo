package com.proyecto.palomo.service.impl;

import com.proyecto.palomo.model.Chat;
import com.proyecto.palomo.model.User;
import com.proyecto.palomo.repository.IChatRespository;
import com.proyecto.palomo.repository.IUserRepository;
import com.proyecto.palomo.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService implements IChatService {

    private final IChatRespository chatRepository;
    private final IUserRepository userRepository;

    private static final String CHAT_SIMPLE_FORMAT = "@%s&%s";
    private static final String CHAT_GROUP_FORMAT = "#%s";

    private String formatSimpleNameChat(Long userId, Long secondUserId){
        return String.format(CHAT_SIMPLE_FORMAT, userId, secondUserId);
    }

    private String formatGroupNameChat(String name){
        return String.format(CHAT_GROUP_FORMAT, name);
    }

    @Override
    public Chat get(Long id) {
        return chatRepository.findById(id).orElse(null);
    }

    @Override
    public Chat createSimple(Chat chat) throws Exception {
        Chat nchat = new Chat();
        if(chat.getUsers().size() != 2)
            throw new Exception("Este chat debe tener 2 usuarios.");
        if(chat.getUsers().get(0).getUserId().equals(chat.getUsers().get(1).getUserId()))
            throw new Exception("No puedes agregar dos veces al mismo usuario.");

        User[] users = {chat.getUsers().get(0), chat.getUsers().get(1)};
        User userA = userRepository.findById(users[0].getUserId()).orElseThrow();
        User userB = userRepository.findById(users[1].getUserId()).orElseThrow();

        boolean isExistChat = userA.getChats().stream().anyMatch(chatA -> (
                chatA.getName().equals(formatSimpleNameChat(users[0].getUserId(),users[1].getUserId())) ||
                chatA.getName().equals(formatSimpleNameChat(users[1].getUserId(),users[0].getUserId()))));

        if (isExistChat) {
            throw new Exception("Este chat ya existe");
        }

        nchat.setName(formatSimpleNameChat(
                        users[0].getUserId(),
                        users[1].getUserId()));
        nchat = chatRepository.save(nchat);
        userA.addChat(nchat);
        userB.addChat(nchat);
        userRepository.save(userA);
        userRepository.save(userB);

        return nchat;
    }

    @Override
    public Chat createGroup(Chat chat) {
        Chat nchat = new Chat();

        nchat.setName(formatGroupNameChat(chat.getName()));

        List<User> users = chat.getUsers()
                .stream()
                .map(user -> userRepository.findById(user.getUserId()).orElseThrow())
                .collect(Collectors.toList());

        nchat.setUsers(users);
        nchat = chatRepository.save(nchat);

        Chat finalNchat = nchat;

        users.forEach(user -> user.addChat(finalNchat));

        userRepository.saveAll(users);

        return nchat;
    }


    @Override
    public Chat getSimpleByUsers(Long userId, Long secondUserId) {
        User user = userRepository.findById(userId).get();
        User userSecond = userRepository.findById(secondUserId).get();
        User[] users = {user, userSecond};
        Chat chat = user.getChats().stream().filter(chatA -> (
                chatA.getName().equals(formatSimpleNameChat(users[0].getUserId(),users[1].getUserId())) ||
                        chatA.getName().equals(formatSimpleNameChat(users[1].getUserId(),users[0].getUserId()))))
                .findAny().get();
        return chat;
    }

    @Override
    public List<Chat> getAllChatsByUserId(Long id, Integer page) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ArrayList<>();
        }
        return user.getChats();
    }

    @Override
    public Chat addUserToChat(Long userId, Long chatId) {
        User user = userRepository.findById(userId).orElseThrow();
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        String type = chat.getName().toLowerCase().substring(0,1); //prefix @ to simple and # to group
        if(!type.equals("#"))
            return null;
        chat.getUsers().add(user);
        return chatRepository.save(chat);
    }

    @Override
    public void deleted(Long id) {
        chatRepository.deleteById(id);
    }

    @Override
    public boolean isExist(Long id) {
        return chatRepository.existsById(id);
    }

    @Override
    public boolean isExistUserInChat(Long userId, Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        return chat.getUsers().stream()
                .anyMatch(user -> user.getUserId().equals(userId));
    }
}
