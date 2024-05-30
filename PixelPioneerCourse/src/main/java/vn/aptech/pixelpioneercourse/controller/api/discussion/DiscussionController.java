package vn.aptech.pixelpioneercourse.controller.api.discussion;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.entities.Discussion;
import vn.aptech.pixelpioneercourse.service.DiscussionService;

import java.util.List;

@RestController
@RequestMapping("/api/discussions")
public class DiscussionController {
    private final DiscussionService discussionService;
    private final ModelMapper mapper;
    
    public DiscussionController(DiscussionService discussionService, ModelMapper mapper){
        this.discussionService = discussionService;
        this.mapper = mapper;
    }

    //convert entity -> dto
    private DiscussionCreateDto toDiscussionDto(Discussion discussion){
        return mapper.map(discussion, DiscussionCreateDto.class);
    }
    
    @GetMapping
    public ResponseEntity<List<Discussion>> findAll(){
        return ResponseEntity.ok(discussionService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Discussion> getDiscussionById(@PathVariable("id") int id){
        Discussion discussion = discussionService.findById(id);
        return discussion != null ? ResponseEntity.ok(discussion) : ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public Discussion createDiscussion(@RequestBody DiscussionCreateDto discussionCreateDto){
        return discussionService.save(discussionCreateDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Discussion> updateDiscussion(@PathVariable int id, @RequestBody DiscussionCreateDto discussionDetails) {
        Discussion discussion = discussionService.findById(id);
        if (discussion == null) {
            return ResponseEntity.notFound().build();
        }
        
        discussion.setContent(discussionDetails.getContent());
        final Discussion updatedDiscussion = discussionService.save(toDiscussionDto(discussion));
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
