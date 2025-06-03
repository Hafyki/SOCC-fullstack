package com.project.socc.util;

import com.project.socc.controllers.UserController;
import com.project.socc.entities.User;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers(0,10,"name","asc")).withRel("users"),
                linkTo(methodOn(UserController.class).postUser()).withRel("post"),
                linkTo(methodOn(UserController.class).patchUser(user.getId(), null)).withRel("patch")
        );
    }
}
