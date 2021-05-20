package shop.projectp.springboot.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import shop.projectp.springboot.domain.user.Role;
import shop.projectp.springboot.domain.user.User;

import java.util.Map;

// 구글 로그인 이후 가져온 사용자의 정보(이메일, 이름, 프로필 사진, 기타 정보)를 저장하는 dto
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey, name, email, picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
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

    // Entity 생성 시점은 처음 "가입할 때" 이다. 그 시점의 기본 권한을 GUEST로 준다.
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }

}
