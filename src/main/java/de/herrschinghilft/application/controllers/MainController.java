package de.herrschinghilft.application.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Value("${spring.application.name}")
    String appName;


    @RequestMapping("/")
    public ResponseEntity<String> index() {
        log.info("Received a request on the index");
        return ResponseEntity.ok(appName + " is running.");
    }

    // TODO: do swagger documentation and link it here
}
