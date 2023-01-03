package com.springbootquartz.controller;


import com.springbootquartz.dto.ApiResponse;
import com.springbootquartz.dto.JobRequest;
import com.springbootquartz.service.QuartzService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QuartzController {

    private final QuartzService quartzService;

    @RequestMapping(value = "/job/add", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> addJob(@Valid @RequestBody JobRequest jobRequest) throws Exception {
        quartzService.addJob(jobRequest);
        return new ResponseEntity<>(new ApiResponse(true,"Success"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/job/update", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> updateJob(@Valid @RequestBody JobRequest jobRequest) throws Exception {

        if(quartzService.isJobExists(jobRequest)){
            quartzService.updateJob(jobRequest);
        }else{
            return new ResponseEntity<>(new ApiResponse(false,"Job dose not exist."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true,"Success"), HttpStatus.OK);
    }
}
