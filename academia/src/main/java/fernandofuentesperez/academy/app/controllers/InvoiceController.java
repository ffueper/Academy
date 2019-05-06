package fernandofuentesperez.academy.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fernandofuentesperez.academy.app.models.entities.Client;
import fernandofuentesperez.academy.app.models.services.ClientService;

@Controller
@SessionAttributes("invoice")
public class InvoiceController {

	@Autowired
	private ClientService clientService;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping("/invoiceList/{idClient}")
	public String ver(@PathVariable(value = "idClient") Long idClient, Model model, RedirectAttributes flash) {

		Client client = clientService.findOne(idClient);
		if (client == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/clientList";
		}
		
		model.addAttribute("client", client);
		model.addAttribute("title", "Facturas del cliente: " + client.getName() + " " + client.getSurname());
		return "invoiceList";
	}
	
	
}
