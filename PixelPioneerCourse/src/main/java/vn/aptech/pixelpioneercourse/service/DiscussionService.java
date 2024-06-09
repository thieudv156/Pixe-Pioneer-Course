package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.entities.Discussion;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscussionService {
    List<Discussion> findAll();
    Discussion findById(Integer id);
    Discussion createDiscussion(DiscussionCreateDto discussionCreateDto);
    Discussion updateDiscussion(Integer id, DiscussionCreateDto discussionDetails);
    void deleteById(Integer id);
    List<Discussion> findBySubLessonId(Integer subLessonId);
    Discussion findByDate(LocalDateTime createDate);
}
