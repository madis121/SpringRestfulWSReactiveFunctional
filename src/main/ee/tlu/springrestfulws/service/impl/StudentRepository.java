package ee.tlu.springrestfulws.service.impl;

import org.springframework.stereotype.Component;

import ee.tlu.springrestfulws.dto.School;
import ee.tlu.springrestfulws.dto.Student;
import ee.tlu.springrestfulws.exception.ResourceNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class StudentRepository {

	private SchoolRepository schoolRepository;
	
	public StudentRepository(SchoolRepository schoolRepository) {
		this.schoolRepository = schoolRepository;
	}

	public Flux<Student> all(Long schoolId) {
		School school = schoolRepository.getSchools().get(schoolId);
		if (school == null) {
			throw new ResourceNotFoundException("School with the id of " + schoolId + " was not found");
		}
		
		return Flux.fromIterable(school.getStudents().values());
	}

	public Mono<Student> findById(Long schoolId, Long studentId) {
		School school = schoolRepository.getSchools().get(schoolId);
		if (school == null) {
			throw new ResourceNotFoundException("School with the id of " + schoolId + " was not found");
		}
		
		return Mono.justOrEmpty(school.getStudents().get(studentId));
	}

	public Mono<Void> save(Long schoolId, Mono<Student> student) {
		School school = schoolRepository.getSchools().get(schoolId);
		if (school == null) {
			throw new ResourceNotFoundException("School with the id of " + schoolId + " was not found");
		}
		
		return student.doOnNext(s -> {
			long id = (long) school.getStudents().size() + 1;
			s.setId(id);
			school.getStudents().put(id, s);
		}).thenEmpty(Mono.empty());
	}

	public Mono<Student> update(Long schoolId, Long studentId, Mono<Student> student) {
		School school = schoolRepository.getSchools().get(schoolId);
		if (school == null) {
			throw new ResourceNotFoundException("School with the id of " + schoolId + " was not found");
		}
		
		Mono<Student> existing = Mono.justOrEmpty(school.getStudents().get(studentId));
		return existing.doOnNext(e -> {
			student.doOnNext(s -> {
				e.setGivenName(s.getGivenName());
				e.setSurname(s.getSurname());
			});
		}).switchIfEmpty(Mono.error(new ResourceNotFoundException("School with the id of " + schoolId
				+ " does not have an existing student with the id of " + studentId)));
	}

	public Mono<Void> delete(Long schoolId, Long studentId) {
		School school = schoolRepository.getSchools().get(schoolId);
		if (school == null) {
			throw new ResourceNotFoundException("School with the id of " + schoolId + " was not found");
		}
		
		school.getStudents().remove(studentId);
		return Mono.empty();
	}

}
