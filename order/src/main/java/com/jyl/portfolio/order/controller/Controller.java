package com.jyl.portfolio.order.controller;

import com.jyl.portfolio.commons.Util;
import com.jyl.portfolio.order.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    private SeckillService service;
    @GetMapping(value="/order")
    public String testing() {
        System.out.println("controller picks up the request");
        Util.testing();
        return service.findAll().toString();
    }
}
