package study.datajpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    @WPermission(ApiServiceType.BROADCAST)
    public String hello(@RequestHeader(value = "apiKey") String apiKey) {
        System.out.println("[apikey] : " + apiKey);
        return "hello";
    }

    @GetMapping("/init")
    public String init(@RequestParam(value = "title") String title,
                       @RequestParam(value = "summary") String summary) {
        System.out.println("[title] : " + title);
        System.out.println("[summary] : " + summary);

        return "init page";
    }
}
