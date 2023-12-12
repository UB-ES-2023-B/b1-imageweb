package com.example.b1esimageweb.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.MuroService;
import com.example.b1esimageweb.web.dto.MuroDto;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;

@RestController
public class MuroController {

    private MuroService muroService;

    public MuroController(MuroService muroService) {
        this.muroService = muroService;
    }

    @GetMapping("/getMuro")
    public ResponseEntity<Object> getPostsByUser(){
        try {
            Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User currentUser = null;
            if(obj instanceof User){
                currentUser = (User) obj;
            }
            if(currentUser != null) {
                Iterable<MuroDto> muroDto =  muroService.getMuro(currentUser);
                return new ResponseEntity<>(muroDto, HttpStatus.OK);
            }else{
                throw new UserNotFoundException("User was not found");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }    
}
