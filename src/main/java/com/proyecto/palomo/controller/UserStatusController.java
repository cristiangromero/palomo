package com.proyecto.palomo.controller;

import com.proyecto.palomo.dto.userstatus.UserStatusRequest;
import com.proyecto.palomo.dto.userstatus.UserStatusResponse;
import com.proyecto.palomo.service.ICrudService;
import com.proyecto.palomo.service.IUserStatausService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userstatus")
@CrossOrigin(origins = "*")
public class UserStatusController extends CrudController<UserStatusRequest, UserStatusResponse>{

    @Autowired
    private IUserStatausService userStatusService;

    @Override
    protected ICrudService<UserStatusRequest, UserStatusResponse> service() {
        return userStatusService;
    }
}
