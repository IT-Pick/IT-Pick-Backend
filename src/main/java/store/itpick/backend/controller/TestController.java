package store.itpick.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    @GetMapping("/*.ico")
    void pathMatch() {
        System.out.println("favicon.ico.");
    }
}
