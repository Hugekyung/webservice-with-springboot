package shop.projectp.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Entity클래스를 작성했다면 기본적인 CRUD가 가능하도록 Jpa에서 제공하는 JpaRepository 인터페이스를 만들어야 한다.
// JpaRepository를 상속받을 때는 사용될 Entity 클래스와 ID 값이 들어가게 된다. 즉, JpaRepository<T, ID> 가 된다.
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email); // email 정보로 기존에 가입한 정보가 있는지 확인한다.
    Optional<User> findByName(String name);

}
