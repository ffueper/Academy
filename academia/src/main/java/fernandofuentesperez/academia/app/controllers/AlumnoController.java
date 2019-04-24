package fernandofuentesperez.academia.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fernandofuentesperez.academia.app.models.dao.AlumnoDao;
import fernandofuentesperez.academia.app.models.entities.Alumno;

@Controller
public class AlumnoController {

	@Autowired
	@Qualifier("alumnoDaoJPA")
	private AlumnoDao alumnoDao;
	
	@RequestMapping(value="/alumnos", method=RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de alumnos");
		model.addAttribute("alumnos", alumnoDao.findAll());
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
	public String guardar(Alumno alumno) {
		alumnoDao.save(alumno);
		return "redirect:alumnos";
	}
	
	
	
	
	
}
