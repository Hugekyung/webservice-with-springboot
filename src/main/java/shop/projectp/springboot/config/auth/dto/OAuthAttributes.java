package shop.projectp.springboot.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import shop.projectp.springboot.domain.user.Role;
import shop.projectp.springboot.domain.user.User;

import java.util.Map;

// 소셜 로그인 이후 가져온 사용자의 정보(이메일, 이름, 프로필 사진, 기타 정보)를 저장하는 dto
@Slf4j // log를 찍어보기 위한 어노테이션
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey, name, email, picture, registrationsId;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture, String registrationsId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.registrationsId = registrationsId;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        log.info("요청 :: "+registrationId);
        log.info("유저이름 :: "+userNameAttributeName);
        log.info("속성 :: "+attributes);
//        if("naver".equals(registrationId)) {
//            return ofNaver("id", attributes);
//        }
//        if("github".equals(registrationId)) {
//            return ofGithub(userNameAttributeName, attributes);
//        }
//        return ofGoogle(userNameAttributeName, attributes);
        switch (registrationId) {
            case "google":
                log.info(registrationId + "계정으로 로그인되었습니다.");
                return ofGoogle(userNameAttributeName, attributes);
            case "naver":
                log.info(registrationId + "계정으로 로그인되었습니다.");
                return ofNaver(userNameAttributeName, attributes);
            case "github":
                log.info(registrationId + "계정으로 로그인되었습니다.");
                return ofGithub(userNameAttributeName, attributes);
            default:
                throw new IllegalArgumentException("해당 로그인 정보를 찾을 수 없습니다.");
        }
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofGithub(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("login"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("avatar_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // Entity 생성 시점은 처음 "가입할 때" 이다. 그 시점의 기본 권한을 GUEST 로 준다.
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }

}
