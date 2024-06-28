package vn.aptech.pixelpioneercourse.dto;

import lombok.Data;
import vn.aptech.pixelpioneercourse.entities.Course;

@Data
public class CourseView {
    private Course course;
    private Integer completeCount;
    private Integer enrollCount;
    private Double avgReview;
}
