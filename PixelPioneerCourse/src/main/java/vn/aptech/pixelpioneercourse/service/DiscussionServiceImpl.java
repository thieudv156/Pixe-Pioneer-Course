package vn.aptech.pixelpioneercourse.service;

import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.entities.Discussion;
import vn.aptech.pixelpioneercourse.repository.DiscussionRepository;
import vn.aptech.pixelpioneercourse.until.SensitiveWordFilter;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class DiscussionServiceImpl implements DiscussionService {

    private final DiscussionRepository discussionRepository;
    
    private DiscussionServiceImpl(DiscussionRepository discussionRepository) {
        this.discussionRepository = discussionRepository;
    }

    @Override
    public List<Discussion> findAll() {
        return discussionRepository.findAll();
    }

    @Override
    public Discussion findById(Integer id) {
        return discussionRepository.findById(id).orElseThrow(()-> new RuntimeException("Discussion not found"));
    }

    @Override
    public Discussion createDiscussion(DiscussionCreateDto discussionCreateDto) {
        Discussion discussion = new Discussion();
        discussion.setContent(SensitiveWordFilter.filterSensitiveWords(discussionCreateDto.getContent()));
        discussion.setCreatedAt(LocalDateTime.now());
        return discussionRepository.save(discussion);
    }


    @Override
    public Discussion updateDiscussion(Integer id, DiscussionCreateDto discussionDetails) {
        Discussion existingDiscussion = discussionRepository.findById(id).orElseThrow(()-> new RuntimeException("Discussion not found"));
        if(existingDiscussion == null) {
            return null;
        }
        existingDiscussion.setContent(SensitiveWordFilter.filterSensitiveWords(discussionDetails.getContent()));
        return discussionRepository.save(existingDiscussion);
    }

    @Override
    public void deleteById(Integer id) {
        discussionRepository.deleteById(id);
    }

    @Override
    public List<Discussion> findBySubLessonId(Integer subLessonId) {
        return discussionRepository.findDiscussionBySubLessonId(subLessonId);
    }
}
