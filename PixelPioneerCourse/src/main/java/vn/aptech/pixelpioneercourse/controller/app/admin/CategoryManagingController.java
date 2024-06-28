package vn.aptech.pixelpioneercourse.controller.app.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.dto.CategoryCreateDto;
import vn.aptech.pixelpioneercourse.entities.Category;
import vn.aptech.pixelpioneercourse.service.CategoryService;
import vn.aptech.pixelpioneercourse.service.CourseService;

@Controller
@RequestMapping("/app/admin/categories")
public class CategoryManagingController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CourseService courseService;
	
	@GetMapping
	public String categoryAdminIndex(Model model, HttpSession session) {
		try {
			if (session.getAttribute("isAdmin") != null) {
				List<Category> categories = categoryService.findAll();
				List<Category> relatedToCourse = new ArrayList<>();
				List<Category> notRelatedToCourse = new ArrayList<>();
				
				for (Category category: categories) {
					if (!courseService.findByCategoryId(category.getId()).isEmpty()) {
						relatedToCourse.add(category);
					} else {
						notRelatedToCourse.add(category);
					}
				}
				
				model.addAttribute("relatedToCourseCategories", relatedToCourse);
				model.addAttribute("notRelatedToCourseCategories", notRelatedToCourse);
				return "app/admin_view/category/general";
			} else {
				return "redirect:/";
			}
		} catch (Exception e) {
			return "redirect:/app/error/500";
		}
	}
	
	@PostMapping
	public String searchCategories(@RequestParam("query") String query, Model model) {
		try {
			List<Category> searchedCategories = categoryService.findByQuery(query);
			model.addAttribute("searchedCategories", searchedCategories);
			return "app/admin_view/category/general";
					//searchedCategories
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/app/error/500";
		}
	}
	
	@GetMapping("/create")
	public String createCategoryPage(HttpSession session) {
		try {
			if (session.getAttribute("isAdmin") != null) {
				return "app/admin_view/category/create";
			} else {
				return "redirect:/";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/app/error/500";
		}
	}
	
	@PostMapping("/create")
	public String createCategory(@RequestParam("categoryname") String categoryName, RedirectAttributes ra) {
		try {
			Category checkCateg = categoryService.findByName(categoryName.toUpperCase());
			if (checkCateg == null) {
				CategoryCreateDto newDto = new CategoryCreateDto();
				newDto.setName(categoryName.toUpperCase());
				if (categoryService.save(newDto)) {
					ra.addFlashAttribute("SuccessCondition", true);
					ra.addFlashAttribute("SuccessSuccess", "Create a new category successfully");
					return "redirect:/app/admin/categories/create";
				} else {
					ra.addFlashAttribute("ErrorCondition", true);
					ra.addFlashAttribute("ErrorError", "Cannot create a new category");
					return "redirect:/app/admin/categories/create";
				}
			}
			else {
				ra.addFlashAttribute("ErrorCondition", true);
				ra.addFlashAttribute("ErrorError", "Category has existed, please try another one.");
				return "redirect:/app/admin/categories/create";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/app/admin/categories";
		}
	}
	
	@GetMapping("/rename")
	public String updatePage(@RequestParam("categoryId") String categoryId, Model model, HttpSession session) {
		try {
			if (session.getAttribute("isAdmin") != null) {
				Integer cid = Integer.parseInt(categoryId);
				Category category = categoryService.findById(cid);
				model.addAttribute("category", category);
				return "app/admin_view/category/update";
			} else {
				return "redirect:/";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/app/error/500";
		}
	}
	
	@PostMapping("/rename")
	public String updateCategory(@RequestParam("categoryId") String categoryId, @RequestParam("categoryName") String categoryName, RedirectAttributes ra) {
		try {
			Integer cid = Integer.parseInt(categoryId);
			Category checkCateg = categoryService.findById(cid);
			String oldName = checkCateg.getName();
			if (checkCateg.getName().equals(categoryName)) {
				ra.addFlashAttribute("ErrorCondition",true);
				ra.addFlashAttribute("ErrorError", "Please apply other changes to category name");
				return "redirect:/app/admin/categories";
			} else {
				CategoryCreateDto ccdto = new CategoryCreateDto();
				ccdto.setName(categoryName);
				if (categoryService.update(cid, ccdto)) {
					ra.addFlashAttribute("SuccessCondition",true);
					ra.addFlashAttribute("SuccessSuccess", "Rename category "+oldName+" into category "+categoryName+" successfully");
					return "redirect:/app/admin/categories";
				} else {
					ra.addFlashAttribute("ErrorCondition",true);
					ra.addFlashAttribute("ErrorError", "Unable to rename category "+oldName);
					return "redirect:/app/admin/categories";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/app/error/500";
		}
	}
	
	@PostMapping("/delete")
	public String deleteCategory(@RequestParam("categoryId") String categoryId, RedirectAttributes ra) {
		try {
			Integer caid = Integer.parseInt(categoryId);
			if (categoryService.delete(caid)) {
				ra.addFlashAttribute("SuccessCondition", true);
				ra.addFlashAttribute("SuccessSuccess", "Delete category successfully");
				return "redirect:/app/admin/categories";
			} else {
				ra.addFlashAttribute("ErrorCondition", true);
				ra.addFlashAttribute("ErrorError", "Unable to delete category");
				return "redirect:/app/admin/categories";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/app/admin/categories";
		}
	}
}
