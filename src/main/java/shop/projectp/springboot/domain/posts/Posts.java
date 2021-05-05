package shop.projectp.springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.projectp.springboot.domain.BaseTimeEntity;

import javax.persistence.*;

@Getter // Getter만 만드는 이유는 불필요하거나 의도치 않은 데이터 세팅을 막기 위함이다.
@NoArgsConstructor // 기본 생성자 자동 추가
@Entity // 테이블과 해당 클래스를 연결한다.
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 생성시마다 id값이 증가한다.
    private Long id;

    // Column을 선언하지 않아도 기본적으로 Column이 되지만 옵션을 변경하고자 하는 경우에 사용한다.
    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
