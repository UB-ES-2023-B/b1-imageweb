package com.example.b1esimageweb.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.service.SearchService;

@RestController
@RequestMapping(path="/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService){
        this.searchService = searchService;
    }
    
    @GetMapping(path="/getSearchResults/{searchCriteria}")
    public ResponseEntity<Iterable<User>> getUsersSearchResults(@PathVariable("searchCriteria") String searchCriteria){
        Iterable<User> users = searchService.getUsersSearchResults(searchCriteria);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
