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
    public ResponseEntity<ApiResponse> addScheduleJob(@Valid @RequestBody JobRequest jobRequest){
        quartzService.addScheduleJob(jobRequest);
        return new ResponseEntity<>(new ApiResponse(true,"Success"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/job/update", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse> updateScheduleJob(@Valid @RequestBody JobRequest jobRequest) {
        //quartzService.updateScheduleJob(jobRequest);
        if(quartzService.isJobExists(jobRequest)){
            quartzService.updateScheduleJob(jobRequest);
        }else{
            return new ResponseEntity<>(new ApiResponse(false,"Job dose not exist."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true,"Success"), HttpStatus.OK);
    }

    @RequestMapping(value = "/job/delete", method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponse> deleteScheduleJob(@Valid @RequestBody JobRequest jobRequest) {
        if(quartzService.isJobExists(jobRequest)) {
            quartzService.deleteScheduleJob(jobRequest);
        }else{
            return new ResponseEntity<>(new ApiResponse(false,"Job dose not exist."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true,"Success"), HttpStatus.OK);
    }

    @RequestMapping(value = "/job/state", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse> stateJob(@Valid @RequestBody JobRequest jobRequest) throws Exception {
        quartzService.isJobRunning(jobRequest);

        return new ResponseEntity<>(new ApiResponse(true,"Success"), HttpStatus.OK);
    }
}

