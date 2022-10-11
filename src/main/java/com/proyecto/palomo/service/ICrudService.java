package com.proyecto.palomo.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICrudService <Request,Response>{
    JpaRepository<?,Long> repository();
    Response create (Request request) throws Exception;
    List<Response> getAll();
    Optional<Response> get(Long id);
    Optional<Response> update(long id, Request request);
    default  boolean delete(long id){
        try {
            repository().deleteById(id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
