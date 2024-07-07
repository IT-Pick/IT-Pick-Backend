package store.itpick.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class TestController {
    @RequestMapping("/test")
    public String test() {
        return "test";
    }
}
