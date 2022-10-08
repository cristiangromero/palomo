package com.proyecto.palomo.repository;

import com.proyecto.palomo.model.Message;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
}
