package vn.aptech.pixelpioneercourse.service;

import org.springframework.http.ResponseEntity;
import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.entities.Discussion;

import java.util.List;

public interface DiscussionService {
    List<Discussion> findAll();
    Discussion findById(int id);
    Discussion createDiscussion(DiscussionCreateDto discussionCreateDto);
    Discussion updateDiscussion(int id, DiscussionCreateDto discussionDetails);
    void deleteById(int id);
    List<Discussion> findBySubLessonId(int subLessonId);
    
}
