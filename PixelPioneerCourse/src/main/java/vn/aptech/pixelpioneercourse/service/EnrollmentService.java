package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.EnrollmentCreateDto;
import vn.aptech.pixelpioneercourse.entities.Enrollment;

import java.util.List;

public interface EnrollmentService {
    List<Enrollment> findAll();
    Enrollment findById(Integer id);
    Enrollment createEnrollment(EnrollmentCreateDto enrollmentCreateDto);
    Enrollment updateEnrollment(Integer id, EnrollmentCreateDto enrollmentDetails);
    void deleteById(Integer id);
}
