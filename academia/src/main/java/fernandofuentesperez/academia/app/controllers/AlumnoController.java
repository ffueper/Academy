package fernandofuentesperez.academia.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fernandofuentesperez.academia.app.models.entities.Alumno;
import fernandofuentesperez.academia.app.models.services.AlumnoService;
import fernandofuentesperez.academia.app.util.paginator.PageRender;

@Controller
@SessionAttributes("alumno")
public class AlumnoController {

	@Autowired
	private AlumnoService alumnoService;
	
	//Envía una lista de alumnos a alumnos.html
	/*@RequestMapping(value="/alumnoss", method=RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de alumnos");
		model.addAttribute("alumnos", alumnoService.findAll());
		return "alumnos";
	}
	*/
	
	//Envía un Page con 5 alumnos usando PageRequest
	@RequestMapping(value="/alumnos", method=RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 4); //Spring Boot 2
		
		Page<Alumno> alumnos = alumnoService.findAll(pageRequest);
		
		PageRender<Alumno> pageRender = new PageRender<Alumno>("/alumnos", alumnos);
		model.addAttribute("titulo", "Listado de alumnos");
		model.addAttribute("alumnos", alumnos);
		model.addAttribute("page", pageRender);
		return "alumnos";
		
	}
	
	
	//Envía un alumno sin datos al formulario
	@RequestMapping(value="/formAlumno", method=RequestMethod.GET)
	public String crear(Map<String,Object> model) {
		
		Alumno alumno = new Alumno();
		
		model.put("alumno", alumno);
		model.put("titulo", "Formulario de Alumno");
		
		return "formAlumno";
	}
	
	//Guarda un alumno en el sistema
	@RequestMapping(value="/formAlumno", method=RequestMethod.POST)
	public String guardar(@Valid Alumno alumno, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Alumno");
			return "formAlumno";
		}
		String msflash = (alumno.getId() != null) ? "Alumno editado con éxito." : "Alumno creado con éxito.";
		alumnoService.save(alumno);
		status.setComplete(); //Elimina el objeto alumno de la sesión
		flash.addFlashAttribute("success", msflash);
		return "redirect:alumnos";
	}
	
	//Busca los datos del alumno y los envía al formulario
	@RequestMapping(value="/formAlumno/{id}")
	public String editar (@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Alumno alumno = null;
		
		if(id>0){
			alumno = alumnoService.findOne(id);
			if(alumno==null) {
				flash.addFlashAttribute("error", "El ID del alumno no existe en la BBDD!");
				return "redirect:/alumnos";
			}
		}else{
			flash.addFlashAttribute("error", "El ID del alumno no puede ser menor que 1");
			return "redirect:/alumnos";
		}
		
		model.put("alumno", alumno);
		model.put("titulo", "Editar Alumno");
		
		return "formAlumno";
	}
	
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		if(id > 0) {
			alumnoService.delete(id);
			flash.addFlashAttribute("success", "Alumno eliminado con éxito.");
		}
		return "redirect:/alumnos";
	}
	
	
	
}
