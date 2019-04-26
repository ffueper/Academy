package fernandofuentesperez.academia.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;


import fernandofuentesperez.academia.app.models.entities.Alumno;
import fernandofuentesperez.academia.app.models.services.AlumnoService;

@Controller
@SessionAttributes("alumno")
public class AlumnoController {

	@Autowired
	private AlumnoService alumnoService;
	
	@RequestMapping(value="/alumnos", method=RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de alumnos");
		model.addAttribute("alumnos", alumnoService.findAll());
		return "alumnos";
	}
	
	@RequestMapping(value="/formAlumno", method=RequestMethod.GET)
	public String crear(Map<String,Object> model) {
		
		Alumno alumno = new Alumno();
		
		model.put("alumno", alumno);
		model.put("titulo", "Formulario de Alumno");
		
		return "formAlumno";
	}
	
	@RequestMapping(value="/formAlumno", method=RequestMethod.POST)
	public String guardar(@Valid Alumno alumno, BindingResult result, Model model, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Alumno");
			return "formAlumno";
		}
		alumnoService.save(alumno);
		status.setComplete(); //Elimina el objeto alumno de la sesión
		return "redirect:alumnos";
	}
	
	@RequestMapping(value="/formAlumno/{id}")
	public String editar (@PathVariable(value="id") Long id, Map<String, Object> model) {
		Alumno alumno = null;
		
		if(id>0){
			alumno = alumnoService.findOne(id);
		}else{
			return "redirect:/alumnos";
		}
		
		model.put("alumno", alumno);
		model.put("titulo", "Editar Alumno");
		
		return "formAlumno";
	}
	
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if(id > 0) {
			alumnoService.delete(id);
		}
		return "redirect:/alumnos";
	}
	
	
	
}
