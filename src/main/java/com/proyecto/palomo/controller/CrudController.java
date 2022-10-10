package com.proyecto.palomo.controller;

import com.proyecto.palomo.service.ICrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class CrudController <Request, Response>{
     protected abstract ICrudService<Request, Response> service();

    @ApiResponse(code = 201, message = "Elemento registrado exitosamente")
    @ApiOperation(value = "Registra un nuevo elemento")
    @PostMapping
    public ResponseEntity<Response> register(@RequestBody Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service().create(request));
    }

    @ApiResponse(code = 200, message = "OK")
    @ApiOperation(value = "Obtén una lista con todos los elementos registrados")
    @GetMapping
    public ResponseEntity<List<Response>> getAll() {
        return ResponseEntity.ok(service().getAll());
    }


    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Elemento no encontrado")
    })
    @ApiOperation(value = "Obtén un elemento existente por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Response> get(@PathVariable("id") long id) {
        return ResponseEntity.of(service().get(id));
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "Elemento actualizado exitosamente"),
            @ApiResponse(code = 404, message = "Elemento no encontrado")
    })
    @ApiOperation(value = "Actualiza un elemento existente por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable("id") long id, @RequestBody Request request) {
        return ResponseEntity.of(service().update(id, request));
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "Elemento eliminado exitosamente"),
            @ApiResponse(code = 404, message = "Elemento no encontrado")
    })
    @ApiOperation(value = "Elimina un elemento existente por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") long id) {
        return service().delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
