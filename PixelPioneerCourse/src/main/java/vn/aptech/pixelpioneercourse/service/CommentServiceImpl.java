package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.ReviewCreateDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.Review;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.CourseRepository;
import vn.aptech.pixelpioneercourse.repository.UserRepository;
import vn.aptech.pixelpioneercourse.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public Review createComment(ReviewCreateDto reviewCreateDto) {
        Review review = mapper.map(reviewCreateDto, Review.class);

        Course course = courseRepository.findById(reviewCreateDto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        User user = userRepository.findById(reviewCreateDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        review.setCourse(course);
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    @Override
    public Optional<Review> getCommentById(int id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> getAllComments() {
        return reviewRepository.findAll();
    }

    @Override
    public Review updateComment(int id, ReviewCreateDto reviewCreateDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Course course = courseRepository.findById(reviewCreateDto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        User user = userRepository.findById(reviewCreateDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        review.setCourse(course);
        review.setUser(user);
        review.setRating(reviewCreateDto.getRating());
        review.setComment(reviewCreateDto.getComment());
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    @Override
    public void deleteComment(int id) {
        reviewRepository.deleteById(id);
    }
}
