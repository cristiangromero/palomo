package com.proyecto.palomo.service.impl;

import com.proyecto.palomo.model.Chat;
import com.proyecto.palomo.model.User;
import com.proyecto.palomo.repository.IChatRespository;
import com.proyecto.palomo.repository.IUserRepository;
import com.proyecto.palomo.service.IChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService implements IChatService {

    private IChatRespository chatRespository;
    private IUserRepository userRepository;

    private static String CHATSIMPLEFORMAT = "@%s&%s";
    private static String CHATGROUPFORMAT = "#%s";

    private String conformatSimpleNameChat(Long userId, Long sencondUserId){
        return String.format(CHATSIMPLEFORMAT, userId, sencondUserId);
    }

    private String conformatGroupNameChat(String name){
        return String.format(CHATGROUPFORMAT, name);
    }

    @Override
    public Chat get(Long id) {
        return chatRespository.findById(id).orElse(null);
    }

    @Override
    public Chat cretedSimple(Chat chat) {
        Chat nchat = new Chat();
        if(chat.getUsers().size() != 2)
            return null;
        if(chat.getUsers().get(0).getUserId().equals(chat.getUsers().get(1).getUserId()))
            return null;
        User[] users = {chat.getUsers().get(0), chat.getUsers().get(1)};
        User userA = userRepository.findById(users[0].getUserId()).orElseThrow();
        User userB = userRepository.findById(users[1].getUserId()).orElseThrow();
        Boolean isExistChat = userA.getChats().stream().anyMatch(chatA -> (
                chatA.getName().equals(conformatSimpleNameChat(users[0].getUserId(),users[1].getUserId())) ||
                chatA.getName().equals(conformatSimpleNameChat(users[1].getUserId(),users[0].getUserId()))));
        if (isExistChat)
            return null;
        nchat.setName(conformatSimpleNameChat(
                        users[0].getUserId(),
                        users[1].getUserId()));
        nchat = chatRespository.save(nchat);
        userA.addChat(nchat);
        userB.addChat(nchat);
        userRepository.save(userA);
        userRepository.save(userB);
        return nchat;
    }

    @Override
    public Chat cretedGroup(Chat chat) {
        Chat nchat = new Chat();
        List<User> users = chat.getUsers().stream().map(user -> userRepository.findById(user.getUserId()).orElseThrow()).collect(Collectors.toList());
        nchat.setName(conformatGroupNameChat(chat.getName()));
        nchat.setUsers(users);
        nchat = chatRespository.save(nchat);
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
                chatA.getName().equals(conformatSimpleNameChat(users[0].getUserId(),users[1].getUserId())) ||
                        chatA.getName().equals(conformatSimpleNameChat(users[1].getUserId(),users[0].getUserId()))))
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
        Chat chat = chatRespository.findById(chatId).orElseThrow();
        String type = chat.getName().toLowerCase().substring(0,1); //prefix @ to simple and # is group
        if(!type.equals("#"))
            return null;
        chat.getUsers().add(user);
        return chatRespository.save(chat);
    }

    @Override
    public void deleted(Long id) {
        chatRespository.deleteById(id);
    }

    @Override
    public boolean isExist(Long id) {
        return chatRespository.existsById(id);
    }

    @Override
    public boolean isExistUserInChat(Long userId, Long chatId) {
        Chat chat = chatRespository.findById(chatId).orElseThrow();
        return chat.getUsers().stream()
                .filter(user -> user.getUserId().equals(userId))
                .findAny()
                .isPresent();
    }
}
