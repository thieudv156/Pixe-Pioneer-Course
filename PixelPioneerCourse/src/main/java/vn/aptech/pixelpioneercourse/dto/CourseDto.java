package vn.aptech.pixelpioneercourse.dto;

import lombok.Data;

@Data
public class CourseDto {
    private Integer id;
    private String title;
    private String description;
    private Double price;
    private String imageUrl;
}
