package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.entities.Discussion;
import vn.aptech.pixelpioneercourse.repository.DiscussionRepository;

import java.util.List;
@Service
public class DiscussionServiceImpl implements DiscussionService{
    private final DiscussionRepository discussionRepository;
    private final ModelMapper mapper;
    
    public DiscussionServiceImpl(DiscussionRepository discussionRepository, ModelMapper mapper){
        this.discussionRepository = discussionRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Discussion> findAll() {
        return discussionRepository.findAll();
    }

    @Override
    public Discussion findById(int id) {
        return discussionRepository.findById(id).orElseThrow(() -> new RuntimeException("Discussion not found"));
    }

    @Override
    public Discussion save(DiscussionCreateDto discussionCreateDto) {
        return discussionRepository.save(toDiscussion(discussionCreateDto));
    }


    @Override
    public void deleteById(int id) {
        discussionRepository.deleteById(id);
    }
    
    
    //convert dto -> entity
    private Discussion toDiscussion(DiscussionCreateDto dto){
        return mapper.map(dto, Discussion.class);
    }
    
}
