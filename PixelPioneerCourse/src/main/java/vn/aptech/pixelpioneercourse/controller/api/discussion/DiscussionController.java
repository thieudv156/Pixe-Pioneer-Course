package vn.aptech.pixelpioneercourse.controller.api.discussion;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.Discussion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.service.DiscussionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController("apiDiscussionController")
@RequestMapping("/api/discussions")
public class DiscussionController {

    private final DiscussionService discussionService;

    @Autowired
    private ModelMapper mapper;

    public DiscussionController(DiscussionService discussionService) {
        this.discussionService = discussionService;
    }

    @GetMapping
    public ResponseEntity<List<Discussion>> getAllDiscussions() {
        List<Discussion> discussions = discussionService.findAll();
        return ResponseEntity.ok().body(discussions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Discussion> getDiscussionById(@PathVariable Integer id) {
        Discussion discussion = discussionService.findById(id);
        return (discussion == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(discussion);
    }

    @GetMapping("/sublesson")
    public ResponseEntity<List<Discussion>> getDiscussionBySublessonId(@RequestParam("sLessonId") Integer sublessonId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(discussionService.findBySubLessonId(sublessonId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
//        try {
//            Optional<Course> result = Optional.ofNullable(courseService.findById(id));
//            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
//        }
//    }

    @PostMapping
    public ResponseEntity<Discussion> createDiscussion(@Valid @RequestBody DiscussionCreateDto discussionCreateDto) {
        System.out.println(discussionCreateDto);
        Discussion savedDiscussion = discussionService.createDiscussion(discussionCreateDto);
        return ResponseEntity.ok(savedDiscussion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Discussion> updateDiscussion(@Valid @PathVariable Integer id, String content) {
        Discussion updatedDiscussion = discussionService.findById(id);
        DiscussionCreateDto discussion = mapper.map(updatedDiscussion, DiscussionCreateDto.class);
        if (updatedDiscussion == null) {
            return ResponseEntity.notFound().build();
        }
        discussion.setContent(content);
        discussion.setCreatedAt(LocalDateTime.now());
        discussionService.updateDiscussion(id, discussion);
        return ResponseEntity.ok(updatedDiscussion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscussion(@PathVariable Integer id) {
        Discussion discussion = discussionService.findById(id);
        if (discussion == null) {
            return ResponseEntity.notFound().build();
        }
        discussionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
