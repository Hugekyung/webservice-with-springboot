package shop.projectp.springboot.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index"; // 머스테치 파일의 이름으로 파일 경로를 자동 생성한다.
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }
}
