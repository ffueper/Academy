package fernandofuentesperez.academy.app.controllers;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fernandofuentesperez.academy.app.models.entities.Client;
import fernandofuentesperez.academy.app.models.services.ClientService;
import fernandofuentesperez.academy.app.util.paginator.PageRender;

@Controller
@SessionAttributes("client")
public class ClientController {

	@Autowired
	private ClientService clientService;

	
	@GetMapping(value = "/clientProfile/{id}")
	public String seeClient(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash, HttpServletRequest request) {

		Client client = clientService.findOne(id);
		
		if(!hasRole("ROLE_ADMIN")) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			Client userNameClient = clientService.findOneByUserName(auth.getName());
			
			if (userNameClient != client) {
				return "redirect:/error_403";
			}
		}
		/* OTRA FORMA DE COMPROBAR EL ROL DEL USUARIO
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		if(securityContext.isUserInRole("ADMIN")) {} 
		*/
		
		if (client == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/home";
		}

		model.put("client", client);
		model.put("titulo", "Cliente :  " + client.getName() + " " + client.getSurname());
		return "clientProfile";
	}
	
	
	@GetMapping(value = "/clientProfile")
	public String countClient(Map<String, Object> model, RedirectAttributes flash) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Client client = clientService.findOneByUserName(auth.getName());
		
		if (client == null) {
			flash.addFlashAttribute("error", "No existen datos de cliente todavía.");
			return "redirect:/clientForm";
		}

		model.put("client", client);
		model.put("titulo", "Cliente :  " + client.getName() + " " + client.getSurname());
		return "clientProfile";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/clientList", method = RequestMethod.GET)
	public String listClient(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4); // Spring Boot 2

		Page<Client> clients = clientService.findAll(pageRequest);

		PageRender<Client> pageRender = new PageRender<Client>("/clientList", clients);
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clients", clients);
		model.addAttribute("page", pageRender);
		return "clientList";

	}

	
	@RequestMapping(value = "/clientForm", method = RequestMethod.GET)
	public String createClient(Map<String, Object> model) {

		Client client = new Client();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		client.setUserName(auth.getName());
		model.put("client", client);
		model.put("titulo", "Crear Cliente");

		return "clientForm";
	}

	
	@RequestMapping(value = "/clientForm", method = RequestMethod.POST)
	public String saveClient(@Valid Client client, BindingResult result, Model model,
			RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "clientForm";
		}

		String msflash = (client.getId() != null) ? "Cliente editado con éxito." : "Cliente creado con éxito.";
		clientService.save(client);
		status.setComplete(); // Elimina el objeto cliente de la sesión
		
		if(client.getUserName()!="admin") {
			return "redirect:clientProfile";
		}
		
		flash.addFlashAttribute("success", msflash);
		
		return "redirect:clientList";
	}

	
	@RequestMapping(value = "/clientForm/{id}")
	public String editClient(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Client client = null;

		if (id > 0) {
			client = clientService.findOne(id);
			if (client == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD!");
				return "redirect:/clientList";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser menor que 1");
			return "redirect:/clientList";
		}

		model.put("client", client);
		model.put("titulo", "Editar Cliente");

		return "clientForm";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/deleteClient/{id}")
	public String deleteClient(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {

			clientService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito.");

		}
		return "redirect:/clientList";
	}
	
	private boolean hasRole(String role) {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context == null) {
			return false;
		}
		
		Authentication auth = context.getAuthentication();
		
		if(auth == null) {
			return false;
		}
		
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.contains(new SimpleGrantedAuthority(role));
	}

}
