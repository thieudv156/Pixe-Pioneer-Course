//package vn.aptech.pixelpioneercourse.controller.app.admin;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import vn.aptech.pixelpioneercourse.entities.Discussion;
//import vn.aptech.pixelpioneercourse.service.DiscussionService;
//
//@Controller
//@RequestMapping("/app/admin/discussion")
//public class AdminDiscussionController {
//	@Autowired
//	private DiscussionService discussionService;
//	
//	@GetMapping
//	public String discussionPage(Model model) {
//		try {
//			List<Discussion> discussions = discussionService.findAll();
//			System.out.println(discussions);
//			model.addAttribute("discussions", discussions);
//			return "app/admin_view/discussion/general";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "redirect:/app/error/500";
//		}
//	}
//}
//
