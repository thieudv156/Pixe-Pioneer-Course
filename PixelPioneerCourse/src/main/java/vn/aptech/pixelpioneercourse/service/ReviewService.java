package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.ReviewCreateDto;
import vn.aptech.pixelpioneercourse.entities.Review;

import java.util.List;

public interface ReviewService {
    List<Review> findAll();
    List<Review> findByCourseId(Integer id);
    List<Review> findByUserId(Integer id);
    Double average(List<Review> rv);
    Review create(ReviewCreateDto dto);
    Review update(Integer id, ReviewCreateDto dto);
    void delete(Integer id);
}