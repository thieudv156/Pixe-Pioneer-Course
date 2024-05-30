package vn.aptech.pixelpioneercourse.controller.api.discussion;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.entities.Discussion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.service.DiscussionService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/discussions")
public class DiscussionController {
    
    private final DiscussionService discussionService;

    public DiscussionController(DiscussionService discussionService) {
        this.discussionService = discussionService;
    }

    @GetMapping
    public ResponseEntity<List<Discussion>> getAllDiscussions() {
        List<Discussion> discussions = discussionService.findAll();
        return ResponseEntity.ok().body(discussions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Discussion> getDiscussionById(@PathVariable int id) {
        Discussion discussion = discussionService.findById(id);
        return (discussion == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(discussion);
    }
    
    @PostMapping
    public ResponseEntity<Discussion> createDiscussion(@Valid @RequestBody DiscussionCreateDto discussionCreateDto) {
        Discussion savedDiscussion = discussionService.createDiscussion(discussionCreateDto);
        return ResponseEntity.ok(savedDiscussion);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Discussion> updateDiscussion(@Valid @PathVariable int id, @RequestBody Discussion discussionDetails) {
        Discussion updatedDiscussion = discussionService.findById(id);
        if (updatedDiscussion == null) {
            return ResponseEntity.notFound().build();
        }
        updatedDiscussion.setContent(discussionDetails.getContent());
        updatedDiscussion.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(updatedDiscussion);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscussion(@PathVariable int id) {
        Discussion discussion = discussionService.findById(id);
        if (discussion == null) {
            return ResponseEntity.notFound().build();
        }
        discussionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
