package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.dto.CourseCreateDto;
import vn.aptech.pixelpioneercourse.entities.*;
import vn.aptech.pixelpioneercourse.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService{

    final private CourseRepository courseRepository;
    final private ModelMapper mapper;
    final private CategoryService categoryService;
    final private UserService userService;
    final private ImageService imageService;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ProgressService progressService;
    private final TestFormatRepository testFormatRepository;

    public CourseServiceImpl(CourseRepository courseRepository, ModelMapper mapper, CategoryService categoryService, UserService userService, ImageService imageService, UserRepository userRepository, EnrollmentRepository enrollmentRepository, ProgressService progressService1, TestFormatRepository testFormatRepository) {
        this.courseRepository = courseRepository;
        this.mapper = mapper;
        this.categoryService = categoryService;
        this.userService = userService;
        this.imageService = imageService;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.progressService = progressService1;
        this.testFormatRepository = testFormatRepository;
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

            // Check if the course has at least 3 lessons
            List<Lesson> lessons = course.getLessons();
            if (lessons.size() < 3) {
                throw new RuntimeException("Course must have at least 3 lessons to be published!");
            }

            // Check if each of the 3 lessons has at least 2 sublessons
            long lessonsWithAtLeastTwoSubLessons = lessons.stream()
                    .filter(lesson -> lesson.getSubLessons().size() >= 2)
                    .count();

            if (lessonsWithAtLeastTwoSubLessons < 3) {
                throw new RuntimeException("Each of the 3 lessons must have at least 2 sub-lessons to be published!");
            }

            // Publish the course
            course.setIsPublished(true);
            return courseRepository.save(course);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
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
            courseRepository.save(newCourse);
            TestFormat testFormat = new TestFormat();
            testFormat.setCourse(newCourse);
            testFormatRepository.save(testFormat);
            return newCourse;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create new course!: "+ e.getMessage());
        }
    }

    @Override
    public boolean canAccessCourse(Integer userId, Integer courseId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Enrollment enrollment = enrollmentRepository.findFirstByUserOrderByEnrolledAtDesc(user);
            if (enrollment != null) {
                if (enrollment.getSubscriptionEndDate() == null || enrollment.getSubscriptionEndDate().isAfter(LocalDateTime.now())) {
                    return true; // User has an active subscription
                }
            }
        }
        return false; // User does not have an active subscription
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

    public Boolean startCourse(Integer courseId, Integer userId) {
       List<Progress> processes = progressService.createProgressByCourseId(courseId, userId);
        return !processes.isEmpty();
    }

    public List<Course> getEnrolledCourses(Integer userId) {
        return courseRepository.findDistinctByLessons_SubLessons_Progresses_User_Id(userId);
    }

    public List<Course> findTop8ByCategoryOrderByCreatedAtDesc(Category category) {
        return courseRepository.findTop8ByCategoryOrderByCreatedAtDesc(category);
    }

}
