package shop.projectp.springboot.config.auth.dto;

import lombok.Getter;
import shop.projectp.springboot.domain.user.User;

import java.io.Serializable;


// 직렬화를 해주기 위한 클래스
@Getter
public class SessionUser implements Serializable {

    private String name, email, picture;

    // 인증된 사용자 정보 사용
    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
