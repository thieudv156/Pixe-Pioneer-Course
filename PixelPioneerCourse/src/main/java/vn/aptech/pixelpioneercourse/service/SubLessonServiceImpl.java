package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.dto.SubLessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.Image;
import vn.aptech.pixelpioneercourse.entities.SubLesson;
import vn.aptech.pixelpioneercourse.repository.SubLessonRepository;

import java.util.List;
import java.util.Objects;

@Service
public class SubLessonServiceImpl implements SubLessonService {

    final private SubLessonRepository subLessonRepository;
    final private ModelMapper modelMapper;
    final private ImageService imageService;
    final private LessonService lessonService;

    public SubLessonServiceImpl(SubLessonRepository subLessonRepository, ModelMapper modelMapper, ImageService imageService, LessonService lessonService) {
        this.subLessonRepository = subLessonRepository;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
        this.lessonService = lessonService;
    }

    public SubLesson toSubLesson(SubLessonCreateDto dto) {
        return modelMapper.map(dto, SubLesson.class);
    }

    public List<SubLesson> findAllSubLessonsByLessonId(Integer lessonId) {
        try {
            return subLessonRepository.findByLessonId(lessonId);
        } catch (Exception e) {
            throw new RuntimeException("List of SubLessons is null", e);
        }
    }
    
    public List<SubLesson> findAllSubLessons() {
    	try {
    		return subLessonRepository.findAll();
    	} catch (Exception e) {
    		throw new RuntimeException("Empty list");
    	}
    }

    public SubLesson findById(Integer id) {
        try {
            return subLessonRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("SubLesson is null", e);
        }
    }

    public SubLesson save(SubLessonCreateDto dto) {
        try {
            SubLesson subLesson = toSubLesson(dto);
            if (dto.getImage() != null && dto.getLessonId() > 0) {
                Image img = imageService.uploadImageToFileSystem(dto.getImage());
                subLesson.setFrontPageImage(img);
                subLesson.setLesson(lessonService.findById(dto.getLessonId()));
                return subLessonRepository.save(subLesson);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("SubLesson is null", e);
        }
    }

    public boolean delete(Integer id) {
        try {
            subLessonRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("SubLesson is null", e);
        }
    }

    public SubLesson update(Integer id, SubLessonCreateDto dto) {
        try {
            SubLesson subLesson = subLessonRepository.findById(id).orElse(null);
            if (subLesson == null) {
                throw new RuntimeException("SubLesson is null");
            }
            subLesson.setTitle(dto.getTitle());
            subLesson.setContent(dto.getContent());
            if (dto.getImage() != null) {
                String oldImageName = subLesson.getFrontPageImage().getImageName();
                String newImageName = dto.getImage().getOriginalFilename();

                if (!Objects.equals(newImageName, oldImageName)) {
                    Image img = imageService.uploadImageToFileSystem(dto.getImage());
                    subLesson.setFrontPageImage(img);
                }
            }
            return subLessonRepository.save(subLesson);
        } catch (Exception e) {
            throw new RuntimeException("SubLesson is null", e);
        }
    }

    public SubLesson completeSubLesson(Integer id) {
        try {
            SubLesson subLesson = subLessonRepository.findById(id).orElse(null);
            if (subLesson == null) {
                throw new RuntimeException("SubLesson is null");
            }
            subLesson.setCompleteStatus(true);
            return subLessonRepository.save(subLesson);
        } catch (Exception e) {
            throw new RuntimeException("SubLesson is null", e);
        }
    }
}