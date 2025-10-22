package com.example.blcweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.blcweb.form.DataSetForm;
import com.example.blcweb.service.DataSetService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/dataset")
public class DataSetController {

    private final DataSetService dataSetService;

    public DataSetController(DataSetService dataSetService) {
        this.dataSetService = dataSetService;
    }

    @GetMapping("/settings")
    public String showForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new DataSetForm("", "", false));
        }
        model.addAttribute("list", dataSetService.findAll());
        return "settings";
    }

    @PostMapping
    public String register(
        @Valid @ModelAttribute("form") DataSetForm form,
        BindingResult binding,
        RedirectAttributes ra,
        Model model
    ) {
        if (binding.hasErrors()) {
            model.addAttribute("list", dataSetService.findAll());
            return "settings";
        }

        dataSetService.save(form.displayName(), form.departmentName());
        ra.addFlashAttribute("message", "登録しました！");
        return "redirect:/dataset/settings";
    }
}
