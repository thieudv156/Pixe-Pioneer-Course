package vn.aptech.pixelpioneercourse.controller.app.course;

import java.util.List;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.dto.SubLessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.Discussion;
import vn.aptech.pixelpioneercourse.entities.SubLesson;
import vn.aptech.pixelpioneercourse.service.DiscussionService;

@Controller
@RequestMapping("/app/sub-lesson")
public class AppSublessonController {

	private final DiscussionService discussionService;
	@Value("${api.base.url}")
	private String apiBaseUrl;
	private String subLessonApiUrl;
	private final ModelMapper modelMapper;

	public AppSublessonController(DiscussionService discussionService, ModelMapper modelMapper) {
		this.discussionService = discussionService;
        this.modelMapper = modelMapper;
    }
	@PostConstruct
	public void init() {
		subLessonApiUrl = apiBaseUrl + "/sub-lesson";
	}

	@GetMapping("/discussions")
	public List<Discussion> getRelatedDiscussionForSublessonPages(SubLesson sublesson, Model model) {
		List<Discussion> related = discussionService.findBySubLessonId(sublesson.getId());
		model.addAttribute("discussions", related);
		return related;
	}

	@GetMapping("/discussions/{id}")
	public Discussion getRelatedDiscussionWithID(@PathVariable("id") int id, Model model) {
		Discussion d = discussionService.findById(id);
		model.addAttribute("discussionWithID", d);
		return d;
	}

	/*
	 * Use RedirectAttributes to send error or success messages to users
	 */

	@PostMapping("/discussions/create")
	public String postDiscussion(@RequestBody DiscussionCreateDto dto, RedirectAttributes ra) {
		discussionService.createDiscussion(dto);
		return "redirect:/app/sublesson";
	}

	@PostMapping("/discussions/update/{id}")
	public String editDiscussion(@PathVariable("id") int id, @RequestBody DiscussionCreateDto dto, RedirectAttributes ra) {
		try {
//			Discussion d = discussionService.findByDate(dto.getCreatedAt());
			discussionService.updateDiscussion(id, dto);
			return "redirect:/app/sublesson";
		} catch (Exception e) {
			return "redirect:/app/sublesson";
		}
	}

	@PostMapping("/discussion/delete/{id}")
	public String deleteDiscussion(@PathVariable("id") int id, RedirectAttributes ra) {
		try {
			discussionService.deleteById(id);
			return "redirect:/app/sublesson";
		} catch (Exception e) {
			return "redirect:/app/sublesson";
		}
	}

	@GetMapping("/instructor/view/{subLessonId}")
	public String showSubLessonById(Model model, @PathVariable("subLessonId") Integer subLessonId) {
		RestTemplate restTemplate = new RestTemplate();
		Optional<SubLesson> subLesson = Optional.ofNullable(restTemplate.getForObject(subLessonApiUrl + "/" + subLessonId, SubLesson.class));
		SubLessonCreateDto subLessonCreateDto = modelMapper.map(subLesson.get(), SubLessonCreateDto.class);
		model.addAttribute("subLessonCreateDto", subLessonCreateDto);
		model.addAttribute("lesson", subLesson.get().getLesson());
		return "app/instructor_view/sub-lesson/sub-lesson-detail";
	}

	@PostMapping("/instructor/{subLessonId}/update")
	public String updateSubLesson(@ModelAttribute SubLessonCreateDto subLessonCreateDto,
			@PathVariable Long subLessonId, RedirectAttributes redirectAttributes) {
		try {
			// Create HttpEntity with the SubLessonCreateDto
			HttpEntity<SubLessonCreateDto> requestEntity = new HttpEntity<>(subLessonCreateDto);

			// Make the API call to update the sub-lesson
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<SubLesson> response = restTemplate.exchange(
					subLessonApiUrl + "/" + subLessonId + "/update",
					HttpMethod.PUT,
					requestEntity,
					SubLesson.class
			);

			SubLesson updatedSubLesson = response.getBody();

			if (updatedSubLesson == null) {
				redirectAttributes.addFlashAttribute("errorMessage", "Sub-lesson not found!");
				return "redirect:/app/sub-lesson/instructor/view/" + subLessonId;
			}

			redirectAttributes.addFlashAttribute("successMessage", "Sub-lesson updated successfully!");
			return "redirect:/app/lesson/instructor/view/" + updatedSubLesson.getLesson().getId();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
