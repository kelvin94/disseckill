package com.jyl.portfolio.mq.controller;

import com.jyl.portfolio.commons.Util;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping(value="/mq")
    public String testing() {
        return Util.testing();
    }
}
