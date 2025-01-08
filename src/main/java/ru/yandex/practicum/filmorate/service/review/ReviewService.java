package ru.yandex.practicum.filmorate.service.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {

    Review createReview(Review review);

    Review updateReview(Review review);

    void deleteReviewById(long reviewId);

    Review getReviewById(long reviewId);

    List<Review> getReviewsFilm(Long filmId, Long count);

    void addLikeReview(long id, long userId);

    void addDisLikeReview(long id, long userId);

    void deleteLikeReview(long id, long userId);

    void deleteDisLikeReview(long id, long userId);
}
