package com.proyecto.palomo.service.impl;


import com.proyecto.palomo.model.Message;
import com.proyecto.palomo.repository.IMessageRepository;
import com.proyecto.palomo.repository.IStatusRepository;
import com.proyecto.palomo.service.IMessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService implements IMessageService {

    private IMessageRepository messageRepository;

    private IStatusRepository statusRepository;

    @Override
    public Message create(Message message) throws Exception {
        Message m = new Message();
        m.setMessage(message.getMessage());
        m.setChat(message.getChat());
        m.setTimestamp(new Date());
        m.setStatus(statusRepository.findById(1L).orElseThrow(() -> new Exception("No se ha encontrado al estado del mensaje.")));
        //1-enviado
        return messageRepository.save(m);
    }

    @Override
    public List<Message> getByPage(Integer page, Long chatId) {
        return this.messageRepository.findByChat_ChatIdOrderByTimestampDesc(chatId, PageRequest.of(page,20))
                .stream().collect(Collectors.toList());
    }

    @Override
    public List<Message> getByDateToNext(Date date, Long chatId) {
        return this.messageRepository.findByChat_ChatIdAndTimestampAfterOrderByTimestamp(chatId,date);
    }

    @Override
    public Message get(Long id) throws Exception {
        return this.messageRepository.findById(id).orElseThrow(() -> new Exception("No se encontró al mensaje."));
    }

    @Override
    public void delete(Long id) {
        this.messageRepository.deleteById(id);
    }
}

