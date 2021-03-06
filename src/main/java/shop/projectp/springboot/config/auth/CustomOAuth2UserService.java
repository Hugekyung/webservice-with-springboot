package shop.projectp.springboot.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import shop.projectp.springboot.config.auth.dto.OAuthAttributes;
import shop.projectp.springboot.config.auth.dto.SessionUser;
import shop.projectp.springboot.domain.user.User;
import shop.projectp.springboot.domain.user.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;


// OAuthAttributes dto 기반으로 가입 및 정보 수정, 세션 저장 등의 기능을 수행한다.
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    // 구글에서 받은 userRequest 데이터에 대한 후처리 함수.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        
        // 현재 로그인 진행 중인 서비스를 구분하는 코드
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        
        // oauth2 로그인 진행 시 키가 되는 필드 값
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();



        // attribute를 담은 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        // SessionUser: 세션에 사용자 정보를 저장하기 위한 dto 클래스
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 원래 findByEmail을 통해 유저 정보를 저장하거나 업데이트 해야하는데,
    // 깃헙의 경우, 엑세스토큰으로 한번더 요청을 보내야 이메일을 반환받을 수 있으므로, 여기서는 모든 소셜 로그인 api가 공통으로 반환하는 이름 정보로 처리한다.
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByName(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity()); // 테이블 생성

        return userRepository.save(user);
    }

}
