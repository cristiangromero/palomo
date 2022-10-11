package com.proyecto.palomo.service;

import com.proyecto.palomo.model.Message;

import java.util.Date;
import java.util.List;

public interface IMessageService {

    public Message create(Message message);

    public List<Message> getByPage(Integer page, Long chatId);

    public List<Message> getByDateToNext(Date date, Long chatId);

    public Message get(Long id);

    public void delete(Long id);

}
