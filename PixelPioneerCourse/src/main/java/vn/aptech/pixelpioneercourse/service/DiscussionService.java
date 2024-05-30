package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.entities.Discussion;

import java.util.List;

public interface DiscussionService {
    List<Discussion> findAll();
    Discussion findById(int id);
    Discussion save(DiscussionCreateDto discussionCreateDto);
    void deleteById(int id);
    
}
