package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.ReviewCreateDto;
import vn.aptech.pixelpioneercourse.entities.Review;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Review createComment(ReviewCreateDto reviewCreateDto);
    Optional<Review> getCommentById(int id);
    List<Review> getAllComments();
    Review updateComment(int id, ReviewCreateDto reviewCreateDto);
    void deleteComment(int id);
}
