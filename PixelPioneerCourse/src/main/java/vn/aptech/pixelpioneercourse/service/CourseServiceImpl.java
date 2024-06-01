//package vn.aptech.pixelpioneercourse.service;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//import vn.aptech.pixelpioneercourse.dto.CourseCreateDto;
//import vn.aptech.pixelpioneercourse.entities.Category;
//import vn.aptech.pixelpioneercourse.entities.Course;
//import vn.aptech.pixelpioneercourse.entities.User;
//import vn.aptech.pixelpioneercourse.repository.CourseRepository;
//
//import java.util.List;
//
//@Service
//public class CourseServiceImpl implements CourseService{
//
//    private final CourseRepository courseRepository;
//    private final ModelMapper mapper;
//    private final CategoryService categoryService;
//    private final UserService userService;
//
//    public CourseServiceImpl(CourseRepository courseRepository, ModelMapper mapper, CategoryService categoryService, UserService userService) {
//        this.courseRepository = courseRepository;
//        this.mapper = mapper;
//        this.categoryService = categoryService;
//        this.userService = userService;
//    }
//
//
//
//    //from CourseCreateDto to Course
//    private Course toCourse(CourseCreateDto dto){
//        return mapper.map(dto, Course.class);
//    }
//
//    //Find all courses
//    public List<Course> findAll(){
//        try{
//            return courseRepository.findAll();
//        }
//        catch (Exception e){
//            throw new RuntimeException("List of Course is null");
//        }
//   }
//
//   //Find course by id
//    public Course findById(int id){
//        try{
//            return courseRepository.findById(id).orElseThrow(()-> new RuntimeException("Course not found!"));
//        }
//        catch (Exception e){
//            throw new RuntimeException("Course is null");
//        }
//    }
//
//    //Save course
//    public Course save(CourseCreateDto dto){
//        if (dto == null) {
//            throw new RuntimeException("Course is null");
//        }
//        try {
//            Course course = toCourse(dto);
//            return courseRepository.save(course);
//        } catch (Exception e) {
//            throw new RuntimeException("Error saving course: " + e.getMessage());
//        }
//    }
//
//    //Update course
//    public boolean update(int id, CourseCreateDto dto){
//        if(dto == null){
//            throw new RuntimeException("Course is null");
//        }
//        try{
//            Course existedCourse = courseRepository.findById(id).orElseThrow(()-> new RuntimeException("Course not found!"));
//            existedCourse.setTitle(dto.getTitle());
//            existedCourse.setPrice(dto.getPrice());
//            existedCourse.setCategory(categoryService.findById(dto.getCategoryId()));
//            existedCourse.setUser(userService.findById(dto.getInstructorId()));
//            courseRepository.save(existedCourse);
//            return true;
//        }
//        catch (Exception e){
//            throw new RuntimeException("Course is null");
//        }
//
//    }
//
//    //Delete course
//    public boolean delete(int id){
//        try{
//            courseRepository.deleteById(id);
//            return true;
//        }
//        catch (Exception e){
//            throw new RuntimeException("Course is null");
//        }
//    }
//
//    //Find course by categoryId
//    public List<Course> findByCategoryId(int categoryId){
//        try{
//            Category category = categoryService.findById(categoryId);
//            return courseRepository.findByCategory(category);
//        }
//        catch (Exception e){
//            throw new RuntimeException("Course is null");
//        }
//    }
//
//    //Find course by instructorId
//    public List<Course> findByUserInstructorId(int instructorId){
//        try{
//            User instructor = userService.findById(instructorId);
//            return courseRepository.findByUser(instructor);
//        }
//        catch (Exception e){
//            throw new RuntimeException("Course is null");
//        }
//    }
//
//    //Find course by title
//    public List<Course> findByTitle(String title){
//        try{
//            return courseRepository.findByTitleContainingIgnoreCase(title);
//        }
//        catch (Exception e){
//            throw new RuntimeException("Course name not found!");
//        }
//    }
//
//
//
//}
