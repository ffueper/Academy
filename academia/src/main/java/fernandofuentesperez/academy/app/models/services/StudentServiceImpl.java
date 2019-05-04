package fernandofuentesperez.academy.app.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fernandofuentesperez.academy.app.models.entities.Student;
import fernandofuentesperez.academy.app.models.repository.StudentDAO;

//CLASE SERVICE COMO UNICO PUNTO DE ACCESO A DIFERENTES CLASES DAO

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentDAO alumnoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Student> findAll() {
		return (List<Student>) alumnoDao.findAll();
	}
	
	@Override
	public Page<Student> findAll(Pageable pageable) {
		return alumnoDao.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Student alumno) {
		alumnoDao.save(alumno);
	}

	@Override
	@Transactional(readOnly=true)
	public Student findOne(Long id) {
		return alumnoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		alumnoDao.deleteById(id);
	}

	

	
	
	
	
}
