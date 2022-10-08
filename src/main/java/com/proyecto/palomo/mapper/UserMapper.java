package com.proyecto.palomo.mapper;

import com.proyecto.palomo.dto.user.UserRequest;
import com.proyecto.palomo.dto.user.UserResponse;
import com.proyecto.palomo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", source = "userName")
    @Mapping(target = "id", source = "userId")
    UserResponse toResponse(User entity);

    List<UserResponse> toResponses(List<User> entities);

    @Mapping(target = "userStatus", ignore = true)
    @Mapping(target = "userName", source = "username")
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "chats", ignore = true)
    User toEntity(UserRequest request);

}
