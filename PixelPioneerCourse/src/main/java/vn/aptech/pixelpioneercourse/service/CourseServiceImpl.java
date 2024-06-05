package vn.aptech.pixelpioneercourse.service;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.dto.CourseCreateDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.Image;
import vn.aptech.pixelpioneercourse.repository.CourseRepository;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService{

    final private CourseRepository courseRepository;
    final private ModelMapper mapper;
    final private CategoryService categoryService;
    final private UserService userService;
    final private ImageService imageService;

    public CourseServiceImpl(CourseRepository courseRepository, ModelMapper mapper, CategoryService categoryService, UserService userService, ImageService imageService) {
        this.courseRepository = courseRepository;
        this.mapper = mapper;
        this.categoryService = categoryService;
        this.userService = userService;
        this.imageService = imageService;
    }

    //from CourseCreateDto to Course
    private Course toCourse(CourseCreateDto dto){
        return mapper.map(dto, Course.class);
    }

    //Find all courses
    public List<Course> findAll(){
        try{
            return courseRepository.findAll();
        }
        catch (Exception e){
            throw new RuntimeException("List of Course is null");
        }
   }

    public List<Course> findAllPublishedCourses(){
        try{
            return courseRepository.findByIsPublishedIsTrue();
        }
        catch (Exception e){
            throw new RuntimeException("List of Course is null");
        }
    }

   public List<Course> findPublishedCoursesByInstructorId(Integer instructorId){
        try{
            return courseRepository.findByInstructorIdAndIsPublishedIsTrue(instructorId);
        }
        catch (Exception e){
            throw new RuntimeException("List of Course is null");
        }
   }

    public List<Course> findUnPublishedCoursesByInstructorId(Integer instructorId){
        try{
            return courseRepository.findByInstructorIdAndIsPublishedIsFalse(instructorId);
        }
        catch (Exception e){
            throw new RuntimeException("List of Course is null");
        }
    }

    public Course publishCourse(Integer courseId) {
        try {
            Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));
            course.setIsPublished(true);
            return courseRepository.save(course);
        } catch (Exception e) {
            throw new RuntimeException("Cannot published course!", e);
        }
    }

    public Course unpublishCourse(Integer courseId) {
        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found!"));
            course.setIsPublished(false);
            return courseRepository.save(course);
        } catch (Exception e) {
            throw new RuntimeException("Cannot unpublished course!", e);
        }
    }

   //Find course by id
    public Course findById(Integer id){
        try{
            return courseRepository.findById(id).orElseThrow(()-> new RuntimeException("Course not found!"));
        }
        catch (Exception e){
            throw new RuntimeException("Course is null");
        }
    }

    //Save course
    public Course save(CourseCreateDto dto, MultipartFile image){
        if (dto == null) {
            throw new RuntimeException("Course is null");
        }
        try {
            Course course = toCourse(dto);
            if (dto.getCategoryId() > 0 && dto.getInstructorId() > 0 && image!=null) {
                course.setCategory(categoryService.findById(dto.getCategoryId()));
                course.setInstructor(userService.findById(dto.getInstructorId()));
                Image imageName = imageService.uploadImageToFileSystem(image);
                course.setFrontPageImage(imageName);
                return courseRepository.save(course);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Cannot save course!"+e.getMessage());
        }
    }

    //Update course
    public boolean update(Integer id, CourseCreateDto dto, MultipartFile image){
        if(dto == null){
            throw new RuntimeException("Course is null");
        }
        try{
            Course existedCourse = courseRepository.findById(id).orElseThrow(()-> new RuntimeException("Course not found!"));
            existedCourse.setTitle(dto.getTitle());
            existedCourse.setPrice(dto.getPrice());
            if(image!=null)
            {
                // Get the old image name
                String oldImageName = existedCourse.getFrontPageImage().getImageName();

                // Get the new image name
                String newImageName = image.getOriginalFilename();

                // If the old image name is not equal to the new image name, upload and save the new image
                if (!oldImageName.equals(newImageName)) {
                    Image imageName = imageService.uploadImageToFileSystem(image);
                    existedCourse.setFrontPageImage(imageName);
                }
            }
            courseRepository.save(existedCourse);
            return true;
        }
        catch (Exception e){
            throw new RuntimeException("Course is null");
        }
    }

    //Delete course
    public boolean delete(Integer id){
        try{
            courseRepository.deleteById(id);
            return true;
        }
        catch (Exception e){
            throw new RuntimeException("Course is null");
        }
    }

    //Find course by categoryId
    public List<Course> findByCategoryId(Integer categoryId){
        try{
            return courseRepository.findByCategoryId(categoryId);
        }
        catch (Exception e){
            throw new RuntimeException("Course is null");
        }
    }

    //Find course by instructorId
    public List<Course> findByInstructorId(Integer instructorId){
        try{
            return courseRepository.findByInstructorId(instructorId);
        }
        catch (Exception e){
            throw new RuntimeException("Course is null");
        }
    }

    //Find course by title
    public List<Course> findByTitle(String title){
        try{
            return courseRepository.findByTitleContainingIgnoreCase(title);
        }
        catch (Exception e){
            throw new RuntimeException("Course name not found!");
        }
    }


}
