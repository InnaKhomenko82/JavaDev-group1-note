package ua.goit.users;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.goit.roles.RoleService;
import ua.goit.validation.deleteAdmin.NonAdminValidation;
import ua.goit.validation.unique.OnCreate;
import ua.goit.validation.unique.OnUpdate;

import javax.validation.ConstraintViolationException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("users")
@Validated
public class UserController {

    UserService userService;
    RoleService roleService;

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping
    public String getUsers(Model model) {
        List<UserDto> users = userService.findAll();
        model.addAttribute("errorConstraint", null);
        model.addAttribute("users", users);
        model.addAttribute("countNotes", users == null ? 0 : users.size());
        return "user/users";
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("add")
    public String showAddUser(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("add", true);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        return "user/user";
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("add")
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

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("{id}")
    public String showEditUser(Model model, @PathVariable UUID id) {
        model.addAttribute("add", false);
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("user", userService.find(id));
        return "user/user";
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("{userId}")
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

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("remove_user/{id}")
    public String removeUser(@PathVariable(value = "id") @NonAdminValidation(classService = UserService.class) UUID id) throws ConstraintViolationException {
        userService.delete(id);
        return "redirect:/users";
    }

    @GetMapping("/removeLogout")
    public String removeLogout(Principal principal, Model model) {
        if ("admin".equals(principal.getName().toLowerCase(Locale.ROOT))) {
            return "redirect:/note/list";
        }
        userService.delete(userService.findByName(principal.getName()).getId());
        return "redirect:/logout";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(
            Exception ex, Model model) {
        List<UserDto> users = userService.findAll();
        model.addAttribute("errorConstraint", ex.getMessage());
        model.addAttribute("users", users);
        model.addAttribute("countNotes", users == null ? 0 : users.size());
        return "user/users";
    }
}