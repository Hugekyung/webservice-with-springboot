package shop.projectp.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // BaseTimeEntity를 상속할 경우 아래 필드들도 칼럼으로 인식하게 한다.
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate // Entity 생성, 저장 시의 시간이 자동저장된다.
    private LocalDateTime createdDate;

    @LastModifiedDate // Entity의 값을 변경할 때의 시간이 자동저장된다.
    private LocalDateTime modifiedDate;
}
