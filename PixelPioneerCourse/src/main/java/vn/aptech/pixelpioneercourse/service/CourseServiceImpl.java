package vn.aptech.pixelpioneercourse.service;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public Course createNewCourse(Integer userId)
    {
        try{
            Course newCourse = new Course();
            newCourse.setTitle("New Course");
            newCourse.setDescription("Description");
            newCourse.setInstructor(userService.findById(userId));
            return courseRepository.save(newCourse);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create new course!: "+ e.getMessage());
        }
    }


    //Update course
    public Course update(Integer id, CourseCreateDto dto, MultipartFile image){
        if(dto == null){
            throw new RuntimeException("Course is null");
        }
        try{
            Course existedCourse = courseRepository.findById(id).orElseThrow(()-> new RuntimeException("Course not found!"));
            existedCourse.setTitle(dto.getTitle());
            existedCourse.setPrice(dto.getPrice());
            existedCourse.setDescription(dto.getDescription());
            existedCourse.setCategory(categoryService.findById(dto.getCategoryId()));
            if (image != null) {
                String newImageName = image.getOriginalFilename();
                Image frontPageImage = existedCourse.getFrontPageImage();

                if (frontPageImage == null || !frontPageImage.getImageName().equals(newImageName)) {
                    Image uploadedImage = imageService.uploadImageToFileSystem(image);
                    existedCourse.setFrontPageImage(uploadedImage);
                }
            }

            return courseRepository.save(existedCourse);
        }
        catch (Exception e){
            throw new RuntimeException("Course is null "+e.getMessage());
        }
    }

    //Delete course
    @Transactional
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
