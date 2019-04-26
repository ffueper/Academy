package fernandofuentesperez.academia.app.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fernandofuentesperez.academia.app.models.entities.Alumno;
import fernandofuentesperez.academia.app.models.repository.AlumnoDao;

//CLASE SERVICE COMO UNICO PUNTO DE ACCESO A DIFERENTES CLASES DAO

@Service
public class AlumnoServiceImpl implements AlumnoService {

	@Autowired
	private AlumnoDao alumnoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Alumno> findAll() {
		return (List<Alumno>) alumnoDao.findAll();
	}

	@Override
	@Transactional
	public void save(Alumno alumno) {
		alumnoDao.save(alumno);
	}

	@Override
	@Transactional(readOnly=true)
	public Alumno findOne(Long id) {
		return alumnoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		alumnoDao.deleteById(id);
	}

	
	
	
	
}
