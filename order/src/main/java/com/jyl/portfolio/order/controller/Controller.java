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

@CrossOrigin
@RestController
public class Controller {
    private static Logger logger = LogManager.getLogger(Controller.class.getSimpleName());

    @Autowired
    private SeckillService service;

    @GetMapping(value = "/swag")
    public ResponseEntity findAllSwags() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.findAll_swags());
    }

    @GetMapping(value = "/order")
    public ResponseEntity findAllOrders() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.findAll_orders());
    }

    @PostMapping(value = "/swag")
    public ResponseEntity buyOffer(@Valid @RequestBody SeckillParameter param) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.executeSeckill(param));
    }

    @DeleteMapping(value = "/clear")
    public ResponseEntity clearAll() throws Exception {
        service.clear();
        return ResponseEntity.status(HttpStatus.OK)
            .body("Done");
    }

    @GetMapping(value="/export/url/{swag_id}")
    public ResponseEntity exportSeckillUrl(@PathVariable Long swag_id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.exportSeckillUrl(swag_id));
    }
}
