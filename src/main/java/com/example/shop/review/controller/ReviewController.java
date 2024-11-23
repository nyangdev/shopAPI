package com.example.shop.review.controller;

import com.example.shop.review.dto.ReviewDTO;
import com.example.shop.review.dto.ReviewPageRequestDTO;
import com.example.shop.review.exception.ReviewExceptions;
import com.example.shop.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("")
    public ResponseEntity<ReviewDTO> register(@RequestBody @Validated ReviewDTO reviewDTO,
                                              Principal principal) {
        log.info("register: " + reviewDTO);

        if(!principal.getName().equals(reviewDTO.getReviewer())) {
            throw ReviewExceptions.REVIEWER_MISMATCH.get();
        }

        return ResponseEntity.ok(reviewService.register(reviewDTO));
    }

    // 리뷰 조회
    @GetMapping("/{rno}")
    public ResponseEntity<ReviewDTO> read(@PathVariable("rno") Long rno) {

        log.info("read: " + rno);

        return ResponseEntity.ok(reviewService.read(rno));
    }

    // 리뷰 삭제
    // 리뷰를 작성한 사용자와 현재 로그인한 사용자가 일치하는지 확인 필요함
    @DeleteMapping("/{rno}")
    public ResponseEntity<Map<String,String>> remove(@PathVariable("rno") Long rno,
                                                     Authentication authentication) {

        log.info("remove: " + rno);

        String currentUser = authentication.getName();

        log.info("currentUser: " + currentUser);

        ReviewDTO reviewDTO = reviewService.read(rno);

        if(!currentUser.equals(reviewDTO.getReviewer())) {
            throw ReviewExceptions.REVIEWER_MISMATCH.get();
        }

        reviewService.remove(rno);

        return ResponseEntity.ok(Map.of("message", "success"));
    }

    // 리뷰 수정
    @PutMapping("/{rno}")
    public ResponseEntity<ReviewDTO> modify(@PathVariable("rno") Long rno,
                                            @RequestBody ReviewDTO reviewDTO,
                                            Authentication authentication) {
        log.info("modify: " + rno);

        if(!rno.equals(reviewDTO.getRno())) {
            throw ReviewExceptions.REVIEW_NOT_MATCHED.get();
        }

        return ResponseEntity.ok().body(reviewService.modify(reviewDTO));
    }

    // 리뷰의 목록 처리
    @GetMapping("/{pno}/list")
    public ResponseEntity<?> list(@PathVariable("pno") Long pno,
                                  @Validated ReviewPageRequestDTO pageRequestDTO) {

        pageRequestDTO.setPno(pno);

        return ResponseEntity.ok(reviewService.getList(pageRequestDTO));
    }
}
