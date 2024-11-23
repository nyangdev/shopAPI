package com.example.shop.review.service;

import com.example.shop.review.dto.ReviewDTO;
import com.example.shop.review.dto.ReviewPageRequestDTO;
import com.example.shop.review.entity.ReviewEntity;
import com.example.shop.review.exception.ReviewExceptions;
import com.example.shop.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewDTO register(ReviewDTO reviewDTO) {

        log.info("review register.........");

        try {
            ReviewEntity reviewEntity = reviewDTO.toEntity();

            reviewRepository.save(reviewEntity);

            return new ReviewDTO(reviewEntity);
        } catch (DataIntegrityViolationException e) {
            // 외래키 위반
            throw ReviewExceptions.REVIEW_PRODUCT_NOT_FOUND.get();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw ReviewExceptions.REVIEW_NOT_REGISTRED.get();
        }
    }

    // 리뷰 조회
    public ReviewDTO read(Long rno) {
        ReviewEntity reviewEntity = reviewRepository.findById(rno)
                .orElseThrow(ReviewExceptions.REVIEW_NOT_FOUND::get);

        return new ReviewDTO(reviewEntity);
    }

    // 리뷰 삭제
    public void remove(Long rno) {
        ReviewEntity reviewEntity = reviewRepository.findById(rno)
                // 존재하지 않는 리뷰일때 예외 발생
                .orElseThrow(ReviewExceptions.REVIEW_NOT_FOUND::get);

        try {
            reviewRepository.delete(reviewEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            // 삭제 과정에서의 예외
            throw ReviewExceptions.REVIEW_NOT_REMOVED.get();
        }
    }

    // 리뷰 수정
    public ReviewDTO modify(ReviewDTO reviewDTO) {

        ReviewEntity reviewEntity = reviewRepository.findById(reviewDTO.getRno())
                .orElseThrow(ReviewExceptions.REVIEW_NOT_FOUND::get);

        try {
            reviewEntity.changeReviewText(reviewDTO.getReviewText());
            reviewEntity.changeScore(reviewDTO.getScore());

            return new ReviewDTO(reviewEntity);
        } catch (Exception e) {

            log.error(e.getMessage());
            throw ReviewExceptions.REVIEW_NOT_MODIFIED.get();
        }
    }

    // 리뷰 목록 처리
    public Page<ReviewDTO> getList(ReviewPageRequestDTO reviewPageRequestDTO) {
        Long pno = reviewPageRequestDTO.getPno();
        Pageable pageable = reviewPageRequestDTO.getPageable(Sort.by("rno").descending());

        return reviewRepository.getListByPno(pno, pageable);
    }

}
