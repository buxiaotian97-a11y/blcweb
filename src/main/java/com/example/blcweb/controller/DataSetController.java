package com.example.blcweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.blcweb.entity.UserEntity;
import com.example.blcweb.form.DataSetForm;
import com.example.blcweb.service.DataSetService;

import jakarta.servlet.http.HttpSession;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/dataset")
public class DataSetController {

	private final DataSetService dataSetService;

	public DataSetController(DataSetService dataSetService) {
		this.dataSetService = dataSetService;
	}

	@GetMapping("/settings")
	public String showForm(Model model, HttpSession session) {

		UserEntity loginUser = (UserEntity) session.getAttribute("loginUser");
		if (loginUser == null)
			return "redirect:/login";

		if (!model.containsAttribute("form")) {
			model.addAttribute("form", new DataSetForm(loginUser.getName(), loginUser.getDepartmentName(), false));
		}
		return "settings";
	}

	@PostMapping
	public String register(@Valid @ModelAttribute("form") DataSetForm form, BindingResult binding,
			RedirectAttributes ra, HttpSession session) {
		UserEntity loginUser = (UserEntity) session.getAttribute("loginUser");
		if (loginUser == null)
			return "redirect:/login";

		if (binding.hasErrors()) {
			return "settings";
		}

		try {
			dataSetService.save(loginUser.getId(), form.displayName(), form.departmentName());
		} catch (IllegalArgumentException e) {
			binding.rejectValue("displayName", "duplicate", e.getMessage());
			return "settings";
		}

		loginUser.setName(form.displayName());
		loginUser.setDepartmentName(form.departmentName());
		session.setAttribute("loginUser", loginUser);

		ra.addFlashAttribute("message", "登録しました！");
		return "redirect:/home";
	}
}