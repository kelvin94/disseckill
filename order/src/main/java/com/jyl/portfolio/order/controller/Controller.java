package com.jyl.portfolio.order.controller;

import com.jyl.portfolio.commons.Util;
import com.jyl.portfolio.commons.apiParameter.SeckillParameter;
import com.jyl.portfolio.order.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class Controller {
    @Autowired
    private SeckillService service;

    @PostMapping(value = "/swag")
    public String buyOffer(@Valid @RequestBody SeckillParameter param) throws Exception {
        return service.executeSeckill(param).toString();
    }

    @GetMapping(value="/order")
    public String testing() {
        System.out.println("controller picks up the request");
//        Util.testing();
        return service.exportSeckillUrl(1L).toString();
    }
}
