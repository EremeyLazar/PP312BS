package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService service;

    @GetMapping("")
    public String admin(Model model, Principal principal) {
        model.addAttribute("sayhitoadmin", principal.getName());
        List<User> resultList = service.getAll();
        model.addAttribute("userlist", resultList);
        return "admin";
    }

    //NEW USER!!!
    @GetMapping(value = "/newuser")
    public String newUser(Model model) {
        model.addAttribute("userreg", new User());
        return "index";
    }

    @PostMapping(value = "/newuser")
    public String createUser(@ModelAttribute("userreg") @Valid User user, BindingResult bindingResult) {
        if (service.isUserExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "User with that name already exists!");
            return "newuser";
        } else if (bindingResult.hasErrors()) {
            return "/newuser";
        }
        service.createUser(user);
        return "redirect:http://localhost:8080/admin";
    }

    //NEW ADMIN!!!
    @GetMapping(value = "/newadmin")
    public String newAdmin(Model model) {
        model.addAttribute("adminreg", new User());
        return "createadmin";
    }

    @PostMapping(value = "/newadmin")
    public String createAdmin(@ModelAttribute("adminreg") @Valid User admin, BindingResult bindingResult) {
        if (service.isUserExists(admin.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "User with that name already exists!");
            return "newadmin";
        } else if (bindingResult.hasErrors()) {
            return "/newadmin";
        }
        service.createAdmin(admin);
        return "redirect:http://localhost:8080/admin";
    }


    //    DELETE USER!!!
    @GetMapping(value = "/deleteuser")
    public String deleteUser(@RequestParam("id") int id) {
        service.deleteUser(id);
        return "redirect:deleted";
    }

    @GetMapping(value = "/deleted")
    public String deleted(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Requested user has been removed!!!");
        model.addAttribute("messages", messages);
        return "deleted";
    }

    //    UPDATE USER!!!
    @GetMapping(value = "/update")
    public String updateUser(ModelMap model, @RequestParam("id") int id) {
        model.addAttribute("upuser", service.getOne(id));
        return "update";
    }

    @PostMapping(value = "/update")
    public String update(@ModelAttribute("upuser") @Valid User updatedUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/update";
        }
        service.update(updatedUser, updatedUser.getId());
        return "redirect:/admin";
    }

}
