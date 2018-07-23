package com.crud.tasks.config;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticWebPageController {

    @RequestMapping("/")
    public String index(ModelMap model) {

        model.addAttribute("variable", "My Thymeleaf variable");
        model.addAttribute("one", 1);
        model.addAttribute("two", 2);

        return "index";
    }
}
