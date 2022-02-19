package ua.goit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.goit.roles.RoleService;
import ua.goit.validation.OnCreate;
import ua.goit.validation.OnUpdate;

import java.util.List;
import java.util.UUID;

@Controller
@PreAuthorize("hasAuthority('admin')")
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String getUsers(Model model) {
        List<UserDto> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("countNotes", users == null ? 0 : users.size());
        model.addAttribute("allRoles", roleService.findAll());
        return "user/users";
    }

    @GetMapping("add")
    public String showAddUser(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("add", true);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        return "user/user";
    }

    @PostMapping(value = "add")
    public String addUser(Model model, @ModelAttribute("user") @Validated({OnCreate.class}) UserDto user,
                          BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("allRoles", roleService.findAll());
            return "user/user";
        }
        userService.create(user);
        return "redirect:/users";
    }

    @GetMapping("{id}")
    public String showEditUser(Model model, @PathVariable UUID id) {
        model.addAttribute("add", false);
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("user", userService.find(id));
        return "user/user";
    }

    @PostMapping(value = {"{userId}"})
    public String updateUser(Model model, @PathVariable UUID userId,
                             @ModelAttribute("user") @Validated({OnUpdate.class}) UserDto user, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("allRoles", roleService.findAll());
            model.addAttribute("user", user);
            return "user/user";
        }
        userService.update(userId, user);
        return "redirect:/users";
    }

    @GetMapping("remove_user/{id}")
    public String removeUser(@PathVariable(value = "id") UUID id) {
        userService.delete(id);
        return "redirect:/users";
    }
}