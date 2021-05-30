package shop.projectp.springboot.service.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.projectp.springboot.domain.posts.Posts;
import shop.projectp.springboot.domain.posts.PostsRepository;
import shop.projectp.springboot.web.dto.PostsListResponseDto;
import shop.projectp.springboot.web.dto.PostsResponseDto;
import shop.projectp.springboot.web.dto.PostsSaveRequestDto;
import shop.projectp.springboot.web.dto.PostsUpdateRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true) // 등록. 수정. 삭제 기능이 없는 순수 조회만 가능할 때 사용하면 조회 속도가 개선된다.
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(posts -> new PostsListResponseDto(posts))
                .collect(Collectors.toList());
        // 1. stream(): 컬랙션은 .stream()를 사용하여 Stream타입의 객체로 바꿔줄 수 있다.
        // 2. map(): map()은 Function<T, R> 함수적 인터페이스(람다식)를 매개변수로 받아 Stream의 데이터를 하나씩 람다식으로 처리해서 다시 Stream객체에 담는다.
        // 3. collect(): collect()는 스트림의 데이터를 모아 새로운 객체를 만들어 리턴한다. 상단의 코드에선 Collectors.toList()를 사용해서 List 객체를 만들어 리턴한다.
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        postsRepository.delete(posts); // JpaRepository에서 제공하는 delete 메소드를 사용한다. 존재하는 포스트인지 확인 후 제거한다.
    }
}