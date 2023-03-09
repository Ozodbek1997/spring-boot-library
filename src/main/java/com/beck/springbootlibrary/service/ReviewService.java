package com.beck.springbootlibrary.service;


import com.beck.springbootlibrary.entity.Review;
import com.beck.springbootlibrary.repo.BookRepository;
import com.beck.springbootlibrary.repo.ReviewRepository;
import com.beck.springbootlibrary.requestmodels.ReviewRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail,reviewRequest.getBookId());

        if (validateReview != null){
            throw new Exception("Review already created!");
        }
        Review review = new Review();
        review.setBookId(reviewRequest.getBookId());
        review.setUserEmail(userEmail);
        review.setRating(reviewRequest.getRating());
            if (reviewRequest.getReviewDescription().isPresent()) {
                review.setDescription(reviewRequest.getReviewDescription().get());
            }
        review.setDate(Date.valueOf(LocalDate.now()));

        reviewRepository.save(review);


    }

    public Boolean userReviewListed(String userEmail,Long bookId){
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail,bookId);
        if (validateReview != null){
            return true;
        }
        return false;
    }


}
