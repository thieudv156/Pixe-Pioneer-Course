package vn.aptech.pixelpioneercourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.ReviewCreateDto;
import vn.aptech.pixelpioneercourse.entities.Review;
import vn.aptech.pixelpioneercourse.repository.ReviewRepository;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> findByCourseId(Integer id) {
        return reviewRepository.findByCourseId(id);
    }

    @Override
    public List<Review> findByUserId(Integer id) {
        return reviewRepository.findByUserId(id);
    }
    
    @Override
    public Review findById(Integer rid) {
    	return reviewRepository.findById(rid).orElseThrow(() -> new RuntimeException("Review not found"));
    }

    @Override
    public Double average(List<Review> rv) {
        Double sum = 0.0;
        for (Review review : rv) {
            sum += review.getRating();
        }
        return sum / rv.size();
    }

    @Override
    public Review create(ReviewCreateDto dto) {
        Review rv = new Review();
        rv.setCourse(courseService.findById(dto.getCourseId()));
        rv.setUser(userService.findById(dto.getUserId()));
        rv.setRating(dto.getRating());
        rv.setContent(dto.getContent());
        rv.setCreatedAt(dto.getCreatedAt());
        return reviewRepository.save(rv);
    }

    @Override
    public Review update(Integer id, ReviewCreateDto dto) {
        Review rv = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        rv.setContent(dto.getContent());
        rv.setRating(dto.getRating());
        return reviewRepository.save(rv);
    }

    @Override
    public void delete(Integer id) {
        reviewRepository.deleteById(id);
    }
}