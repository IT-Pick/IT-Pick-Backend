package store.itpick.backend.controller;

import io.netty.handler.timeout.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.itpick.backend.common.response.BaseResponse;
import store.itpick.backend.model.RelatedResource;
import store.itpick.backend.util.Selenium;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/rank")
public class RankController {

    @Autowired
    private Selenium selenium;

    @GetMapping("/zum")
    public List<RelatedResource> getRankFromZum()  {
        String url = "https://zum.com/";

        try {

        }catch (TimeoutException e){

        }

        return selenium.useDriverForZum(url);
    }

    @GetMapping("/namu")
    public String getRankFromNamuwiki() {
        String url = "https://namu.wiki/";

        return selenium.useDriverForNamuwiki(url);
    }

    @GetMapping("/signal")
    public List<RelatedResource> getRankFromSignal() {
        String url = "https://www.signal.bz/";

        return selenium.useDriverForSignal(url);
    }

    @GetMapping("/mnate")
    public List<RelatedResource> getRankFromMnate() {
        String url = "https://m.nate.com/";

        return selenium.useDriverForMnate(url);
    }

//    @GetMapping("/nate")
//    public String getRankFromNate() {
//        String url = "https://nate.com/";
//
//        return selenium.useDriverForMnate(url);
//    }
}
