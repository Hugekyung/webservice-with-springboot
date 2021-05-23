package shop.projectp.springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.projectp.springboot.config.auth.LoginUser;
import shop.projectp.springboot.config.auth.dto.OAuthAttributes;
import shop.projectp.springboot.config.auth.dto.SessionUser;
import shop.projectp.springboot.service.posts.PostsService;
import shop.projectp.springboot.web.dto.PostsResponseDto;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    // private final HttpSession httpSession; // @LoginUser 어노테이션으로 인해 필요 없는 코드

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());

        // SessionUser user = (SessionUser) httpSession.getAttribute("user");
        // @LoginUser 어노테이션 추가를 통해 httpSession 에서 유저정보를 가져오는 반복적인 코드를 제거한다.

        if (user != null) {
            model.addAttribute("socialName", user.getName());

            // 잘 등록되었는지 log 찍어보기
            System.out.println("socialName 이 등록되었습니다." + user.getName() + "님");
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save"; // 머스테치 파일의 이름으로 파일 경로를 자동 생성한다.
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }


}
