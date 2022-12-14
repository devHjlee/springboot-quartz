package com.springbootquartz.controller;


import com.springbootquartz.dto.JobRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class QuartzController {

    @RequestMapping(value = "/job/add", method = RequestMethod.POST)
    public ResponseEntity<?> addJob(@RequestBody JobRequest jobRequest){

        return new ResponseEntity<>("AAA", HttpStatus.OK);
    }
}
