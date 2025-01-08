package ru.yandex.practicum.filmorate.service.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.service.event.EventServiceImpl;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.List;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewStorage jdbcReviewStorage;
    private final UserServiceImpl userServiceImpl;
    private final FilmServiceImpl filmServiceImpl;
    private final EventServiceImpl eventServiceImpl;

    public ReviewServiceImpl(ReviewStorage jdbcReviewStorage, UserServiceImpl userServiceImpl, FilmServiceImpl filmServiceImpl, EventServiceImpl eventServiceImpl) {
        this.jdbcReviewStorage = jdbcReviewStorage;
        this.userServiceImpl = userServiceImpl;
        this.filmServiceImpl = filmServiceImpl;
        this.eventServiceImpl = eventServiceImpl;
    }

    @Override
    public Review createReview(Review review) {
        userServiceImpl.getUserById(review.getUserId());
        filmServiceImpl.getFilmById(review.getFilmId());
        review.setUseful(0);
        Review createReview = jdbcReviewStorage.createReview(review);
        eventServiceImpl.createEvent(review.getUserId(), EventType.REVIEW, Operation.ADD, review.getReviewId());
        return createReview;
    }

    @Override
    public Review updateReview(Review review) {
        Review oldReview = getReviewById(review.getReviewId());
        if (review.getUseful() == null) {
            review.setUseful(oldReview.getUseful());
        }
        jdbcReviewStorage.updateReview(review);
        eventServiceImpl.createEvent(review.getUserId(), EventType.REVIEW, Operation.UPDATE, review.getReviewId());
        return review;
    }

    @Override
    public void deleteReviewById(long reviewId) {
        Review review = getReviewById(reviewId);
        jdbcReviewStorage.deleteReviewById(reviewId);
        eventServiceImpl.createEvent(review.getUserId(), EventType.REVIEW, Operation.REMOVE, review.getReviewId());
    }

    @Override
    public Review getReviewById(long reviewId) {
        Review review = jdbcReviewStorage.getReviewById(reviewId);
        return review;
    }

    @Override
    public List<Review> getReviewsFilm(Long filmId, Long count) {
        if (filmId != null) {
            filmServiceImpl.getFilmById(filmId);
        }
        List<Review> reviews = jdbcReviewStorage.getReviewsFilm(filmId, count);
        return reviews;
    }

    @Override
    public void addLikeReview(long id, long userId) {
        userServiceImpl.getUserById(userId);
        getReviewById(id);
        jdbcReviewStorage.addLikeReview(id, userId);
    }

    @Override
    public void addDisLikeReview(long id, long userId) {
        userServiceImpl.getUserById(userId);
        getReviewById(id);
        jdbcReviewStorage.addDisLikeReview(id, userId);
    }

    @Override
    public void deleteLikeReview(long id, long userId) {
        userServiceImpl.getUserById(userId);
        getReviewById(id);
        jdbcReviewStorage.deleteLikeReview(id, userId);
    }

    @Override
    public void deleteDisLikeReview(long id, long userId) {
        userServiceImpl.getUserById(userId);
        getReviewById(id);
        jdbcReviewStorage.deleteDisLikeReview(id, userId);
    }
}
