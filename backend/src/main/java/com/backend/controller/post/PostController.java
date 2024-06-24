package com.backend.controller.post;

import com.backend.domain.place.Place;
import com.backend.domain.post.Post;
import com.backend.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 추가 | 작성 Controller
    @PostMapping("add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> postAdd(Post post, Authentication authentication) {
        if (postService.validate(post)) {
            Integer result = postService.add(post, authentication);
            return ResponseEntity.ok().body(result);
        } else {

            return ResponseEntity.badRequest().build();
        }
    }

    // 게시글 조회 Controller
    @GetMapping("{postId}")
    public ResponseEntity postRead(@PathVariable Integer postId, Authentication authentication) {
        Map<String, Object> result = postService.get(postId, authentication);
        if (result.get("post") == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(result);
    }

    // 게시글 목록 Controller
    @GetMapping("list")
    public Map<String, Object> postList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(value = "type", required = false) String searchType,
            @RequestParam(value = "keyword", defaultValue = "") String searchKeyword) {
        return postService.list(page, searchType, searchKeyword);
    }

    // 게시글 MD추천 목록 Controller
    @GetMapping("mdList")
    public Map<String, Object> postListMd(Map<String, Object> post) {
        return postService.mdlist(post);
    }


    // 게시글 Top 3 인기글 목록 Controller
    @GetMapping("list/postListOfBest")
    public List<Post> postListOfBest() {
        return postService.postListOfBest();
    }

    // 게시글에서 선택한 장소 목록 Controller
    @GetMapping("{postId}/place")
    public List<Place> postPlace(@PathVariable Integer postId) {
        return postService.placeList(postId);
    }

    // 내가 좋아요한 게시글 목록 Controller
    @GetMapping("likeList")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getLikeList(Authentication authentication, @RequestParam(defaultValue = "1") Integer page, @RequestParam(value = "type", required = false) String searchType,
                                                           @RequestParam(value = "keyword", defaultValue = "") String searchKeyword) {
        Integer memberId = Integer.valueOf(authentication.getName());
        System.out.println("searchKeyword = " + searchKeyword);
        Map<String, Object> likedPosts = postService.getLikeAllList(memberId, page, searchType, searchKeyword);
        return ResponseEntity.ok(likedPosts);
    }

    // 게시글 삭제 Controller
    @DeleteMapping("{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity postDelete(@PathVariable Integer postId, Authentication authentication) {
        if (postService.hasMemberIdAccess(postId, authentication)) {
            postService.remove(postId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // 게시글 수정 Controller
    @PutMapping("edit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity postEdit(Post post,
                                   Authentication authentication) {
        if (!postService.hasMemberIdAccess(post.getPostId(), authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (postService.validate(post)) {
            postService.edit(post);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 게시글 좋아요 Controller
    @PutMapping("like")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> postLike(@RequestBody Map<String, Object> like, Authentication authentication) {
        return postService.postLike(like, authentication);
    }

    // home mdpick list
    @GetMapping("mdPickList")
    public Map<String, Object> postMdPickList(Map<String, Object> post) {
        return postService.mdPickList(post);
    }

    // mdPick push Controller
    @PostMapping("{postId}/push")
    public ResponseEntity postMdPickPush(Integer postId, @RequestParam(value = "file", required = false)
    MultipartFile file) throws IOException {
        System.out.println("postid = " + postId);
        postService.mdPickPush(postId, file);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{postId}/pop")
    public ResponseEntity postMdPickPop(@PathVariable Integer postId) {
        postService.mdPickPop(postId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("{postId}/getMdPick")
    public ResponseEntity getMdPick(@PathVariable Integer postId) {
        String getMdPick = postService.getMdPick(postId);
        if (getMdPick != null) {
            return ResponseEntity.ok(getMdPick);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
