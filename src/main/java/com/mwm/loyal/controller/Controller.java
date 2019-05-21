package com.mwm.loyal.controller;

import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller
public class Controller {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/loyal/", method = RequestMethod.GET)
    public String scan() {
        return "scan";
    }
}
