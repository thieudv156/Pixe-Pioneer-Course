package vn.aptech.pixelpioneercourse.controller.api.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.dto.ReviewCreateDto;
import vn.aptech.pixelpioneercourse.entities.Review;
import vn.aptech.pixelpioneercourse.service.ReviewService;
import vn.aptech.pixelpioneercourse.until.SensitiveWordFilter;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
	@Autowired
	private ReviewService reviewService;
	
	@GetMapping("/findall")
	public ResponseEntity<List<Review>> getAllReviews() {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.findAll());
	}
	
	@GetMapping("/course")
	public ResponseEntity<List<Review>> getReviewsByCourseId(@RequestParam("courseId") Integer courseId){
		try {
			return ResponseEntity.status(HttpStatus.OK).body(reviewService.findByCourseId(courseId));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<Review>> getReviewsByUserId(@RequestParam("userId") Integer userId){
		try {
			return ResponseEntity.status(HttpStatus.OK).body(reviewService.findByUserId(userId));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@GetMapping("/average")
	public ResponseEntity<Double> getAverageRatings(@RequestParam("courseid") Integer id) {
		List<Review> rv = null;
		try {
			rv = reviewService.findByCourseId(id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0.0);
		}
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.average(rv));
	}
	
	@PostMapping("/uploadReview")
	public ResponseEntity<?> uploadReview(@RequestBody ReviewCreateDto dto) {
		try {
			if (!SensitiveWordFilter.sensitiveWordsChecker(dto.getContent())) {
	    		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
	    	}
			return ResponseEntity.status(HttpStatus.OK).body(reviewService.create(dto));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/editReview")
	public ResponseEntity<?> editReview(@RequestParam("reviewId") Integer id, @RequestBody ReviewCreateDto dto) {
		try {
			if (!SensitiveWordFilter.sensitiveWordsChecker(dto.getContent())) {
	    		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
	    	}
			return ResponseEntity.status(HttpStatus.OK).body(reviewService.update(id, dto));
		} catch (Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@DeleteMapping("/deleteReview")
	public ResponseEntity<?> deleteReview(@RequestParam("reviewId") Integer id) {
		try {
			reviewService.delete(id);
			return ResponseEntity.status(HttpStatus.OK).body("Delete review successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
