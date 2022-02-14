package ua.goit.roles;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.goit.exception.BadResourceException;
import ua.goit.exception.ResourceAlreadyExistsException;

@Controller
//@PreAuthorize("hasAuthority('admin')")
@RequestMapping("/roles")
public class RoleController {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private RoleService service;
  @Autowired
  private RoleRepository roleRepository;

  private List<Role> init() {
    return service.getAllRoles();
  }

  @GetMapping
  public String getAll(Model model) {
    model.addAttribute("roles", init());
    return "role/roles";
  }

  @GetMapping("/add")
  public String showCreate(Model model) {
    Role role = new Role();
    model.addAttribute("add", true);
    model.addAttribute("role", role);
    return "role/role";
  }

  @PostMapping(value = "/add")
  public String addUser(Model model, @ModelAttribute("role") @Valid Role role,
                        BindingResult result) throws BadResourceException, ResourceAlreadyExistsException {
    Role roleFromDb = roleRepository.findRoleByName(role.getName());
    model.addAttribute("add", true);
    if (result.hasErrors() || roleFromDb != null) {
      if (roleFromDb != null) {
        model.addAttribute("errorUniqueRole", "This role is exists! Role must be unique!");
        return "role/role";
      }
        return "role/role";
    } else {
      service.save(role);
      return "redirect:/roles";
    }
  }

  @GetMapping("/{id}")
  public String showEdit(@PathVariable UUID id, Model model) throws ResourceAlreadyExistsException {
    Role role = service.getRole(id);
    model.addAttribute("role", service.getRole(id));
    return "role/role";
  }

  @PostMapping(value = {"/{id}"})
  public String updateRole(Model model, @PathVariable UUID id, @ModelAttribute("role") @Valid Role role,
      BindingResult result) {
    Role roleFromDb = roleRepository.findRoleByName(role.getName());
    try {
      if (result.hasErrors() || roleFromDb != null) {
        if (roleFromDb != null) {
          model.addAttribute("errorUniqueRole", "This role is exists! Role must be unique!");
          return "role/role";
        }
        return "role/role";
      } else {
        role.setId(id);
        service.update(role);
        return "redirect:/roles";
      }
    } catch (Exception ex) {
      String errorMessage = ex.getMessage();
      LOGGER.error(errorMessage);
      model.addAttribute("errorMessage", errorMessage);
      model.addAttribute("add", false);
      return "role/role";
    }
  }

  @GetMapping(value = {"/{id}/delete"})
  public String showDeleteRoleById(
      Model model, @PathVariable UUID id) {
    Role role = null;
    try {
      role = service.getRole(id);
    } catch (ResourceNotFoundException ex) {
      model.addAttribute("errorMessage", "Role not found");
    }
    model.addAttribute("allowDelete", true);
    model.addAttribute("role", role);
    return "role/role-delete";
  }

  @PostMapping(value = {"/{id}/delete"})
  public String deleteRoleById(
      Model model, @PathVariable UUID id) {
    try {
      service.deleteById(id);
      return "redirect:/roles";
    } catch (ResourceNotFoundException ex) {
      String errorMessage = ex.getMessage();
      LOGGER.error(errorMessage);
      model.addAttribute("errorMessage", errorMessage);
      return "role/role-delete";
    }
  }
}