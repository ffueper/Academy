package fernandofuentesperez.academy.app.controllers;

import java.security.Principal;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fernandofuentesperez.academy.app.models.entities.Role;
import fernandofuentesperez.academy.app.models.entities.User;

import fernandofuentesperez.academy.app.models.services.JpaUserDetailsService;

@Controller
@SessionAttributes("user")
public class LoginController {

	@Autowired
	private JpaUserDetailsService jpaUserDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model, Principal principal,
			RedirectAttributes flash) {

		if (principal != null) {
			flash.addFlashAttribute("info", "Ya ha inciado sesión anteriormente");
			return "redirect:/";
		}

		if (error != null) {
			model.addAttribute("error",
					"Error en el login: Nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo!");
		}

		if (logout != null) {
			model.addAttribute("success", "Ha cerrado sesión con éxito!");
		}

		return "login";
	}
	
	@RequestMapping(value = "/userRegister", method = RequestMethod.GET)
	public String createUser(Map<String, Object> model) {

		User user = new User();
		
		model.put("user", user);
		model.put("titulo", "Únete y mejora tu inglés");
		model.put("boton", "Suscríbete");

		return "userRegister";
	}

	
	@RequestMapping(value = "/userRegister", method = RequestMethod.POST)
	public String saveUser(@Valid User user, BindingResult result, Model model,
			RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Únete y mejora tu inglés");
			model.addAttribute("boton", "Suscríbete");
			return "userRegister";
		}

		String msflash = (user.getId() != null) ? "Cuenta modificada con éxito." : "Suscríto con éxito.";
		String pwd = user.getPassword();
		String encryptPwd = passwordEncoder.encode(pwd);
		user.setPassword(encryptPwd);
		user.setEnabled(true);
		Role role = new Role();
		role.setAuthority("ROLE_USER");
		
		jpaUserDetailsService.save(role);
		user.addRole(role);
		
		jpaUserDetailsService.save(user);
		
		status.setComplete(); // Elimina el objeto usuario de la sesión
		flash.addFlashAttribute("success", msflash);
		return "redirect:/home";
	}

}
