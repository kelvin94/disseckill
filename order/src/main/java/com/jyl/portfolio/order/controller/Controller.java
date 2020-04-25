package com.jyl.portfolio.order.controller;

import com.jyl.portfolio.commons.Util;
import com.jyl.portfolio.commons.apiParameter.SeckillParameter;
import com.jyl.portfolio.order.service.SeckillService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class Controller {
    private static Logger logger = LogManager.getLogger(Controller.class.getSimpleName());

    @Autowired
    private SeckillService service;

    @PostMapping(value = "/swag")
    public String buyOffer(@Valid @RequestBody SeckillParameter param) throws Exception {
        return service.executeSeckill(param).toString();
    }

    @DeleteMapping(value = "/clear")
    public ResponseEntity clearAll() throws Exception {
        service.clear();
        return ResponseEntity.status(HttpStatus.OK)
            .body("Done");
    }

    @GetMapping(value="/order")
    public String testing() {
        logger.info("controller picks up the request");
//        Util.testing();
        return service.exportSeckillUrl(1L).toString();
    }
}
