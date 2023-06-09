package com.example.MonitoringInternetShop.Controllers;

import com.example.MonitoringInternetShop.Models.User;
import com.example.MonitoringInternetShop.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("user-management")
public class UserManagementController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String showUsers(Model model, HttpSession session) {
        User loggedInUser = userService.getLoggedInUser(session).orElseThrow();
        List<User> users = userService.getAllUsers();
        users.remove(loggedInUser);
        model.addAttribute("users", users);
        return "user-management";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id).orElseThrow();
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User user) {
        userService.updateUserPassword(id, user.getPassword());
        userService.updateUserStatus(id, user.getStatus());
        userService.updateUser(id, user);
        return "redirect:/user-management";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/user-management";
    }
}
