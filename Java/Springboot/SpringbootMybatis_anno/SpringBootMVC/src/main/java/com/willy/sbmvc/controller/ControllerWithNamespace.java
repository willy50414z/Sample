package com.willy.sbmvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/classPath")
public class ControllerWithNamespace {
	@RequestMapping("/methodPath")
    public String method() {
        return "mapping url is /classPath/methodPath";
    }
}