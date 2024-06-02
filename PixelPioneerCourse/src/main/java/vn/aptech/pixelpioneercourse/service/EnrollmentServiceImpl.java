package vn.aptech.pixelpioneercourse.service;

import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.EnrollmentCreateDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.CourseRepository;
import vn.aptech.pixelpioneercourse.repository.EnrollmentRepository;
import vn.aptech.pixelpioneercourse.repository.UserRepository;

import java.util.List;
@Service
public class EnrollmentServiceImpl implements EnrollmentService{
    
    private final EnrollmentRepository enrollmentRepository;
    
    private final UserRepository userRepository;
    
    private final CourseRepository courseRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment findById(Integer id) {
        return enrollmentRepository.findById(id).orElseThrow(()-> new RuntimeException("Enrollment not found"));
    }

    @Override
    public Enrollment createEnrollment(EnrollmentCreateDto enrollmentCreateDto) {
        User user = userRepository.findById(enrollmentCreateDto.getUserId()).orElseThrow(()-> new RuntimeException("User not found"));
        Course course = courseRepository.findById(enrollmentCreateDto.getCourseId()).orElseThrow(()-> new RuntimeException("Course not found"));
        if(user == null || course == null) {
            return null;
        }
        
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(enrollmentCreateDto.getEnrolledAt());
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateEnrollment(Integer id, EnrollmentCreateDto enrollmentDetails) {
        Enrollment existingEnrollment = enrollmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Enrollment not found"));
        if(existingEnrollment == null) {
            return null;
        }
        User user = userRepository.findById(enrollmentDetails.getUserId()).orElseThrow(()-> new RuntimeException("User not found"));
        Course course = courseRepository.findById(enrollmentDetails.getCourseId()).orElseThrow(()-> new RuntimeException("Course not found"));
        existingEnrollment.setUser(user);
        existingEnrollment.setCourse(course);
        existingEnrollment.setEnrolledAt(enrollmentDetails.getEnrolledAt());
        return enrollmentRepository.save(existingEnrollment);
    }

    @Override
    public void deleteById(Integer id) {
        enrollmentRepository.deleteById(id);
    }
}
