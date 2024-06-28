package vn.aptech.pixelpioneercourse.controller.app.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.pixelpioneercourse.dto.CourseCreateDto;
import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.entities.*;
import vn.aptech.pixelpioneercourse.service.CourseService;
import vn.aptech.pixelpioneercourse.service.DiscussionService;
import vn.aptech.pixelpioneercourse.service.ProgressService;
import vn.aptech.pixelpioneercourse.service.ReviewService;
import vn.aptech.pixelpioneercourse.service.SubLessonService;
import vn.aptech.pixelpioneercourse.service.SubLessonServiceImpl;
import vn.aptech.pixelpioneercourse.service.UserService;
import vn.aptech.pixelpioneercourse.until.SensitiveWordFilter;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller("clientCourseController")
@RequestMapping("/app/course")
public class AppCourseController {
    @Value("${api.base.url}")
    private String apiBaseUrl;

    private String imageApiUrl;
    private String courseApiUrl;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final ProgressService progressService;
    private final CourseService courseService;
    private final SubLessonService subLessonService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final DiscussionService discussionService;

    @Autowired
    public AppCourseController(ModelMapper modelMapper, UserService uService, ReviewService reService, ObjectMapper objectMapper, ProgressService progressService, CourseService courseService, SubLessonService subLessonService, DiscussionService dService) {
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.progressService = progressService;
        this.courseService = courseService;
        this.subLessonService = subLessonService;
        reviewService = reService;
        userService = uService;
        discussionService = dService;
    }

    @PostConstruct
    public void init() {

        courseApiUrl = apiBaseUrl + "/course";
        imageApiUrl = apiBaseUrl + "/image";
    }


    @GetMapping("/")
    public String index(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 12; // Number of courses per page
        RestTemplate restTemplate = new RestTemplate();
        Course[] courseArray = restTemplate.getForObject(courseApiUrl, Course[].class);
        List<Course> courseList = Arrays.asList(courseArray);
        Category[] categories = restTemplate.getForObject(courseApiUrl + "/categories", Category[].class);
        List<Category> categoryList = Arrays.asList(categories);

        int totalCourses = courseList.size();
        int totalPages = (int) Math.ceil((double) totalCourses / pageSize);

        // Ensure the page number is within the valid range
        if (page < 1) {
            page = 1;
        } else if (page > totalPages) {
            page = totalPages;
        }

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalCourses);

        // Ensure start index is not negative
        if (start < 0) {
            start = 0;
        }

        List<Course> courses = courseList.subList(start, end);
        model.addAttribute("categories", categoryList);
        model.addAttribute("courses", courses);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("imageApiUrl", imageApiUrl);
        model.addAttribute("totalCourses", totalCourses);
        return "app/user_view/course/index";
    }


    @GetMapping("/category")
    public String sortByCategory(Model model,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "category", required = false) String category) {
        int pageSize = 12; // Number of courses per page
        RestTemplate restTemplate = new RestTemplate();
        Course[] courseArray = restTemplate.getForObject(courseApiUrl, Course[].class);
        List<Course> courseList = Arrays.asList(courseArray);
        Category[] categories = restTemplate.getForObject(courseApiUrl + "/categories", Category[].class);
        List<Category> categoryList = Arrays.asList(categories);

        // Filter courses based on the category parameter
        if (category != null && !category.isEmpty()) {
            courseArray = restTemplate.getForObject(courseApiUrl + "/category/" + category, Course[].class);
            courseList = Arrays.asList(courseArray);
        }

        int totalCourses = courseList.size();
        int totalPages = (int) Math.ceil((double) totalCourses / pageSize);

        // Ensure the page number is within the valid range
        if (page < 1) {
            page = 1;
        } else if (page > totalPages) {
            page = totalPages;
        }

        int start = (page - 1) * pageSize;
        if (start < 0) {
            start = 0;
        }
        int end = Math.min(start + pageSize, totalCourses);
        List<Course> courses = courseList.subList(start, end);
        model.addAttribute("categories", categoryList);
        model.addAttribute("courses", courses);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("imageApiUrl", imageApiUrl);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("selectedCategory", category);
        return "app/user_view/course/index";
    }

    @GetMapping("/view/{id}")
    public String showCourseByIdAndLessonId(Model model,
                                            @PathVariable("id") Integer id,
                                            @RequestParam(value = "lessonOrder", defaultValue = "1") Integer lessonOrder,
                                            @RequestParam(value = "subLessonId", required = false) Integer subLessonId,
                                            @SessionAttribute("userId") Integer userId) {
        // Fetch course and lessons
        RestTemplate restTemplate = new RestTemplate();
        Optional<Course> course = Optional.ofNullable(restTemplate.getForObject(courseApiUrl + "/" + id, Course.class));
        if (course.isEmpty()) {
            return "redirect:/app/course";
        }
        List<Lesson> lessons = course.get().getLessons();
        HashMap<Integer, SubLesson> subLessonHashMap = new HashMap<>();
        for (Lesson lesson : lessons) {
            for (SubLesson subLesson : lesson.getSubLessons()) {
                subLessonHashMap.put(subLesson.getId(), subLesson);
            }
        }

        // Fetch discussions
        List<Discussion> discussions;
        if (subLessonId == null) {
            SubLesson currentSubLesson = progressService.getCurrentSubLessonByCourseId(id, userId);
            discussions = discussionService.findBySubLessonId(currentSubLesson.getId());
            model.addAttribute("currentSubLesson", currentSubLesson);
            model.addAttribute("currentLesson", currentSubLesson.getLesson());
        } else {
            Lesson currentLesson = lessons.stream()
                    .filter(lesson -> lesson.getOrderNumber().equals(lessonOrder))
                    .findFirst()
                    .orElse(null);
            SubLesson currentSubLesson = currentLesson.getSubLessons().stream()
                    .filter(subLesson -> subLesson.getId().equals(subLessonId))
                    .findFirst()
                    .orElse(null);
            discussions = discussionService.findBySubLessonId(currentSubLesson.getId());
            model.addAttribute("currentLesson", currentLesson);
            model.addAttribute("currentSubLesson", currentSubLesson);
        }

        // Organize discussions into a map
        Map<Integer, List<Discussion>> discussionMap = new HashMap<>();
        for (Discussion discussion : discussions) {
            Integer parentId = (discussion.getParent() == null) ? null : discussion.getParent().getId();
            discussionMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(discussion);
        }

        model.addAttribute("discussionMap", discussionMap);
        model.addAttribute("discussions", discussionMap.get(null)); // Top-level discussions
        model.addAttribute("currentProgress", progressService.getCurrentProgressByCourseId(id, userId));
        model.addAttribute("subLessonHashMap", subLessonHashMap);
        model.addAttribute("lessons", lessons);
        model.addAttribute("course", course.get());
        model.addAttribute("pageTitle", "Course detail");
        return "app/user_view/course/course-view";
    }
    
    @PostMapping("/view/comment")
    public String createDiscussion(@RequestParam("sublessonId") String sublessonId, 
                                   @RequestParam("userId") String userId, 
                                   @RequestParam("courseId") String courseId, 
                                   @RequestParam("content") String content, 
                                   RedirectAttributes ra) {
        try {
            if (!SensitiveWordFilter.sensitiveWordsChecker(content)) {
                ra.addFlashAttribute("ErrorCondition", true);
                ra.addFlashAttribute("ErrorError", "Bad words detected.");
                return "redirect:/app/course/view/" + URLEncoder.encode(courseId, "UTF-8") + 
                       "?subLessonId=" + URLEncoder.encode(sublessonId, "UTF-8") + 
                       "&lessonOrder=" + URLEncoder.encode(subLessonService.findById(Integer.parseInt(sublessonId)).getOrderNumber().toString(), "UTF-8");
            }
            DiscussionCreateDto newDiscussion = new DiscussionCreateDto();
            newDiscussion.setUserId(Integer.parseInt(userId));
            newDiscussion.setContent(content);
            newDiscussion.setParentId(null);
            newDiscussion.setSubLessonId(Integer.parseInt(sublessonId));
            newDiscussion.setCreatedAt(LocalDateTime.now());
            discussionService.createDiscussion(newDiscussion);
            return "redirect:/app/course/view/" + URLEncoder.encode(courseId, "UTF-8") + 
                   "?subLessonId=" + URLEncoder.encode(sublessonId, "UTF-8") + 
                   "&lessonOrder=" + URLEncoder.encode(subLessonService.findById(Integer.parseInt(sublessonId)).getOrderNumber().toString(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/app/error/500";
        }
    }
    
    @PostMapping("/view/replyComment")
    public String replyComment(@RequestParam("sublessonId") String sublessonId, @RequestParam("courseId") String courseId, @RequestParam("userId") String userId, @RequestParam("discussionId") String discussionId, @RequestParam("content") String content, RedirectAttributes ra) {
    	try {
    		if (!SensitiveWordFilter.sensitiveWordsChecker(content)) {
    			ra.addFlashAttribute("ErrorCondition",true);
    			ra.addFlashAttribute("ErrorError", "Bad words detected.");
    			return "redirect:/app/course/view/" + URLEncoder.encode(courseId, "UTF-8") + 
    	                   "?subLessonId=" + URLEncoder.encode(sublessonId, "UTF-8") + 
    	                   "&lessonOrder=" + URLEncoder.encode(subLessonService.findById(Integer.parseInt(sublessonId)).getOrderNumber().toString(), "UTF-8");
    		}
    		DiscussionCreateDto newDiscussion = new DiscussionCreateDto();
            newDiscussion.setUserId(Integer.parseInt(userId));
            newDiscussion.setContent(content);
            newDiscussion.setParentId(Integer.parseInt(discussionId));
            newDiscussion.setSubLessonId(Integer.parseInt(sublessonId));
            newDiscussion.setCreatedAt(LocalDateTime.now());
            discussionService.createDiscussion(newDiscussion);
            return "redirect:/app/course/view/" + URLEncoder.encode(courseId, "UTF-8") + 
	                   "?subLessonId=" + URLEncoder.encode(sublessonId, "UTF-8") + 
	                   "&lessonOrder=" + URLEncoder.encode(subLessonService.findById(Integer.parseInt(sublessonId)).getOrderNumber().toString(), "UTF-8");
    	} catch (Exception e) {
    		e.printStackTrace();
    		return "redirect:/app/error/500";
    	}
    }
    
    @GetMapping("/view/deleteComment")
    public String deleteDiscussion(@RequestParam("discussionId") String discussionId, @RequestParam("courseId") String courseId, @RequestParam("sublessonId") String sublessonId) {
    	try {
    		Discussion existedDiscussion = discussionService.findById(Integer.parseInt(discussionId));
    		if (existedDiscussion.getParent() != null) {
    			existedDiscussion.setParent(null);
    		}
    		discussionService.deleteById(existedDiscussion.getId());
    		return "redirect:/app/course/view/" + URLEncoder.encode(courseId, "UTF-8") + 
                    "?subLessonId=" + URLEncoder.encode(sublessonId, "UTF-8") + 
                    "&lessonOrder=" + URLEncoder.encode(subLessonService.findById(Integer.parseInt(sublessonId)).getOrderNumber().toString(), "UTF-8");
    	} catch (Exception e) {
    		e.printStackTrace();
    		return "redirect:/app/error/500";
    	}
    }

    @GetMapping("/instructor/courses/{instructorId}")
    public String showCourseByInstructorId(Model model, @PathVariable("instructorId") Integer instructorId) {
        RestTemplate restTemplate = new RestTemplate();
        Course[] courseArray = restTemplate.getForObject(courseApiUrl + "/instructor/" + instructorId, Course[].class);
        List<Course> courseList = Arrays.asList(courseArray);
        model.addAttribute("courses", courseList);
        model.addAttribute("imageApiUrl", imageApiUrl);
        model.addAttribute("pageTitle", "My Courses");
        return "app/instructor_view/course/course-dashboard";
    }


    @GetMapping("/instructor/view/{id}")
    public String showCourseById(Model model, @PathVariable("id") Integer id, @SessionAttribute("userId") Integer userId) {
        RestTemplate restTemplate = new RestTemplate();
        Optional<Course> course = Optional.ofNullable(restTemplate.getForObject(courseApiUrl + "/" + id, Course.class));
        if (course.isEmpty()) {
            return "redirect:/app/course/instructor/courses/" + userId;
        }
        Optional<Category[]> categories = Optional.ofNullable(restTemplate.getForObject(apiBaseUrl + "/category", Category[].class));
        List<Category> categoryList = Arrays.asList(categories.get());
        CourseCreateDto courseCreateDto = modelMapper.map(course.get(), CourseCreateDto.class);
        if (course.get().getFrontPageImage() == null) {
            model.addAttribute("oldImageUrl", imageApiUrl + "/default.jpg");
        } else {
            model.addAttribute("oldImageUrl", imageApiUrl + "/" + course.get().getFrontPageImage().getImageName());
        }
        if ((course.get().getCategory()) != null) {
            model.addAttribute("categoryId", course.get().getCategory().getId());
        }
        List<Lesson> lessons = course.get().getLessons();
        HashMap<Integer, SubLesson> subLessonHashMap = new HashMap<>();
        for (Lesson lesson : lessons) {
            for (SubLesson subLesson : lesson.getSubLessons()) {
                subLessonHashMap.put(subLesson.getId(), subLesson);
            }
        }
        model.addAttribute("isPublished", course.get().getIsPublished());
        model.addAttribute("subLessonHashMap", subLessonHashMap);
        model.addAttribute("courseCreateDto", courseCreateDto);
        model.addAttribute("courseId", course.get().getId());
        model.addAttribute("categories", categoryList);
        model.addAttribute("pageTitle", "Course detail");
        model.addAttribute("lessons", lessons);
        return "app/instructor_view/course/course-detail";
    }

    @PostMapping("/instructor/{id}/update")
    public String updateCourse(@ModelAttribute CourseCreateDto courseCreateDto,
                               @RequestParam(value = "image", required = false) MultipartFile image,
                               @PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes,
                               @SessionAttribute("userId") Integer userId) {
        try {
            // Convert CourseCreateDto to JSON string
            String courseData = objectMapper.writeValueAsString(courseCreateDto);
            System.out.println(courseData);

            // Create a MultiValueMap to hold the parts
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("courseData", courseData);

            // Handle file upload
            if (!image.isEmpty()) {
                body.add("image", image.getResource());
            }

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Create HttpEntity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Make the API call to update the course
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Course> response = restTemplate.exchange(courseApiUrl + "/" + id + "/update", HttpMethod.PUT, requestEntity, Course.class);
            Course updatedCourse = response.getBody();

            if (updatedCourse == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Course not found!");
                return "redirect:/app/course/instructor/view/" + id;  // Redirect back to the course update form
            }
            redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully!");
            return "redirect:/app/course/instructor/view/" + id;  // Redirect to the course detail page
        } catch (Exception e) {
            // Add error message
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/app/course/instructor/view/" + id;  // Redirect back to the course update form
        }
    }

    @GetMapping("/instructor/create-course")
    public String createCourse(RedirectAttributes redirectAttributes, @SessionAttribute("userId") Integer userId) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            Course course = restTemplate.getForObject(courseApiUrl + "/create-course/" + userId, Course.class);
            if (course == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error creating course");
                return "redirect:/app/course/instructor/" + userId;
            }
            redirectAttributes.addFlashAttribute("successMessage", "Course created successfully");
            return "redirect:/app/course/instructor/view/" + course.getId();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/instructor/{id}/delete")
    public String deleteCourse(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, @SessionAttribute("userId") Integer userId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(courseApiUrl + "/" + id + "/delete");
            redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully");
            return "redirect:/app/course/instructor/courses/" + userId;
        } catch (RestClientException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting course: " + e.getMessage());
            return "redirect:/app/course/instructor/view/" + id;
        }
    }

    @GetMapping("/instructor/{courseId}/publish")
    public String publishCourse(@PathVariable("courseId") Integer courseId, RedirectAttributes redirectAttributes, @SessionAttribute("userId") Integer userId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(courseApiUrl + "/" + courseId + "/publish", null);
            redirectAttributes.addFlashAttribute("successMessage", "Course published successfully");
            return "redirect:/app/course/instructor/courses/" + userId;
        } catch (RestClientException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error publishing course: " + e.getMessage());
            return "redirect:/app/course/instructor/view/" + courseId;
        }
    }

    @GetMapping("/instructor/{courseId}/unpublish")
    public String unpublishCourse(@PathVariable("courseId") Integer courseId, RedirectAttributes redirectAttributes) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(courseApiUrl + "/" + courseId + "/unpublish", null);
            redirectAttributes.addFlashAttribute("successMessage", "Course unpublished successfully");
            return "redirect:/app/course/instructor/view/" + courseId;
        } catch (RestClientException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error unpublishing course: " + e.getMessage());
            return "redirect:/app/course/instructor/view/" + courseId;
        }
    }

    @GetMapping("/preview/{courseId}")
    public String previewCourse(@PathVariable("courseId") Integer courseId, RedirectAttributes redirectAttributes, Model model, @SessionAttribute("userId") Integer userId) {
        try {
            Optional<Course> course = Optional.ofNullable(courseService.findById(courseId));
            if (course.isEmpty()) {
                return "redirect:/app/course";
            }
            List<Lesson> lessons = course.get().getLessons();
            // Limit to the first 4 lessons
            List<Lesson> limitedLessons = lessons.stream().limit(4).collect(Collectors.toList());
            Double progress = progressService.getCurrentProgressByCourseId(courseId, userId);

            HashMap<Integer, SubLesson> subLessonHashMap = new HashMap<>();
            for (Lesson lesson : limitedLessons) {
                for (SubLesson subLesson : lesson.getSubLessons()) {
                    subLessonHashMap.put(subLesson.getId(), subLesson);
                }
            }

            List<Review> listReviewRelatedToCourse = reviewService.findByCourseId(courseId);
            Double averageRating = reviewService.average(listReviewRelatedToCourse);
            Integer five_count = 0;
            Integer four_count = 0;
            Integer three_count = 0;
            Integer two_count = 0;
            Integer one_count = 0;
            for (Review r : listReviewRelatedToCourse) {
                if (r.getRating() == 5) {
                    five_count++;
                } else if (r.getRating() == 4) {
                    four_count++;
                } else if (r.getRating() == 3) {
                    three_count++;
                } else if (r.getRating() == 2) {
                    two_count++;
                } else {
                    one_count++;
                }
            }
            
            int totalReviews = listReviewRelatedToCourse.size();
            double fivePercentage = (totalReviews > 0) ? (five_count * 100.0 / totalReviews) : 0;
            double fourPercentage = (totalReviews > 0) ? (four_count * 100.0 / totalReviews) : 0;
            double threePercentage = (totalReviews > 0) ? (three_count * 100.0 / totalReviews) : 0;
            double twoPercentage = (totalReviews > 0) ? (two_count * 100.0 / totalReviews) : 0;
            double onePercentage = (totalReviews > 0) ? (one_count * 100.0 / totalReviews) : 0;

            model.addAttribute("subLessonHashMap", subLessonHashMap);
            model.addAttribute("lessons", limitedLessons);
            model.addAttribute("course", course.get());
            model.addAttribute("pageTitle", "Course detail");
            model.addAttribute("currentProgress", progress);
            model.addAttribute("reviews", listReviewRelatedToCourse);
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("fiveCount", five_count);
            model.addAttribute("fourCount", four_count);
            model.addAttribute("threeCount", three_count);
            model.addAttribute("twoCount", two_count);
            model.addAttribute("oneCount", one_count);
            model.addAttribute("fivePercentage", fivePercentage);
            model.addAttribute("fourPercentage", fourPercentage);
            model.addAttribute("threePercentage", threePercentage);
            model.addAttribute("twoPercentage", twoPercentage);
            model.addAttribute("onePercentage", onePercentage);

            return "app/user_view/course/course-preview";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            e.printStackTrace();
            return "redirect:/app/error/500";
        }
    }

    @GetMapping("/{courseId}/start-course")
    public String startCourse(@PathVariable("courseId") Integer courseId, @SessionAttribute("userId") Integer userId, RedirectAttributes redirectAttributes) {
        try {
            if (!courseService.startCourse(courseId, userId)) {
                return "redirect:/app/course/";
            }
            return "redirect:/app/course/view/" + courseId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/app/error/500";
        }
    }

    @GetMapping("/my-courses")
    public String showMyCourses(Model model, @RequestParam(value = "page", defaultValue = "1") int page, @SessionAttribute("userId") Integer userId, RedirectAttributes redirectAttributes) {
        try {
            int pageSize = 12; // Number of courses per page
            RestTemplate restTemplate = new RestTemplate();
            List<Course> courseList = courseService.getEnrolledCourses(userId);
            Category[] categories = restTemplate.getForObject(courseApiUrl + "/categories", Category[].class);
            List<Category> categoryList = Arrays.asList(categories);

            int totalCourses = courseList.size();
            int totalPages = (int) Math.ceil((double) totalCourses / pageSize);

            // Ensure the page number is within the valid range
            if (page < 1) {
                page = 1;
            } else if (page > totalPages) {
                page = totalPages;
            }

            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, totalCourses);

            // Ensure start index is not negative
            if (start < 0) {
                start = 0;
            }

            List<Course> courses = courseList.subList(start, end);

            // Create a map to hold course progress
            Map<Integer, Double> courseProgressMap = new HashMap<>();
            for (Course course : courseList) {
                // Assuming getCourseProgress is a method that retrieves the progress for a course
                Double progress = progressService.getCurrentProgressByCourseId(course.getId(), userId);
                courseProgressMap.put(course.getId(), progress);
            }

            model.addAttribute("categories", categoryList);
            model.addAttribute("courses", courses);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("imageApiUrl", imageApiUrl);
            model.addAttribute("totalCourses", totalCourses);
            model.addAttribute("courseProgressMap", courseProgressMap);
            return "app/user_view/course/my-courses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "app/error/500";
        }
    }

    @GetMapping("my-courses/category")
    public String sortByCategoryMyCourses(Model model,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "category", required = false) Integer category,
                                          @SessionAttribute("userId") Integer userId,
                                          RedirectAttributes redirectAttributes) {
        try {
            int pageSize = 12; // Number of courses per page
            RestTemplate restTemplate = new RestTemplate();
            List<Course> courseList = courseService.getEnrolledCourses(userId);
            Category[] categories = restTemplate.getForObject(courseApiUrl + "/categories", Category[].class);
            List<Category> categoryList = Arrays.asList(categories);

            // Filter courses based on the category parameter
            if (category != null) {
                courseList = courseList.stream()
                        .filter(course -> course.getCategory().getId().equals(category))
                        .collect(Collectors.toList());
            }

            int totalCourses = courseList.size();
            int totalPages = (int) Math.ceil((double) totalCourses / pageSize);

            if (page < 1) {
                page = 1;
            } else if (page > totalPages) {
                page = totalPages;
            }

            int start = (page - 1) * pageSize;
            if (start < 0) {
                start = 0;
            }
            int end = Math.min(start + pageSize, totalCourses);
            List<Course> courses = courseList.subList(start, end);
            Map<Integer, Double> courseProgressMap = new HashMap<>();
            for (Course course : courseList) {
                // Assuming getCourseProgress is a method that retrieves the progress for a course
                Double progress = progressService.getCurrentProgressByCourseId(course.getId(), userId);
                courseProgressMap.put(course.getId(), progress);
            }
            model.addAttribute("courseProgressMap", courseProgressMap);
            model.addAttribute("categories", categoryList);
            model.addAttribute("courses", courses);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("imageApiUrl", imageApiUrl);
            model.addAttribute("totalCourses", totalCourses);
            model.addAttribute("selectedCategory", category);
            return "app/user_view/course/my-courses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/app/error/500";
        }
    }

    @GetMapping("/sub-lesson/{subLessonId}/finish-sub-lesson")
    public String finishSubLesson(@PathVariable("subLessonId") Integer subLessonId, @SessionAttribute("userId") Integer userId, RedirectAttributes redirectAttributes) {

        try {
            SubLesson subLesson = subLessonService.finishSubLesson(subLessonId, userId);
            if (subLesson == null) {
                return "redirect:/app/course/my-courses";
            }
            return "redirect:/app/course/view/" + subLesson.getLesson().getCourse().getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/app/error/500";
        }
    }
}