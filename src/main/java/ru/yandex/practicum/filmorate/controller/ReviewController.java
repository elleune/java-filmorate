package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review createReview(@RequestBody @Valid Review review) {
        return reviewServiceImpl.createReview(review);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Review updateReview(@RequestBody Review review) {
        return reviewServiceImpl.updateReview(review);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReview(@PathVariable long id) {
        reviewServiceImpl.deleteReviewById(id);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Review getReview(@PathVariable long id) {
        return reviewServiceImpl.getReviewById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Review> getReviewsFilm(@RequestParam(required = false) Long filmId,
                                       @RequestParam(required = false) Long count) {
        return reviewServiceImpl.getReviewsFilm(filmId, count);
    }

    @PutMapping("{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLikeReview(@PathVariable long id, @PathVariable long userId) {
        reviewServiceImpl.addLikeReview(id, userId);
    }

    @PutMapping("{id}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addDisLikeReview(@PathVariable long id, @PathVariable long userId) {
        reviewServiceImpl.addDisLikeReview(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLikeReview(@PathVariable long id, @PathVariable long userId) {
        reviewServiceImpl.deleteLikeReview(id, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDisLikeReview(@PathVariable long id, @PathVariable long userId) {
        reviewServiceImpl.deleteDisLikeReview(id, userId);
    }
}
