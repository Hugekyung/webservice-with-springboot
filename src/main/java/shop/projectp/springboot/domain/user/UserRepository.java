package shop.projectp.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email); // email 정보로 기존에 가입한 정보가 있는지 확인한다.
    Optional<User> findByName(String name);

}
