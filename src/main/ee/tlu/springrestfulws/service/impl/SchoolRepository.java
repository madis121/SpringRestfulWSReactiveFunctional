package ee.tlu.springrestfulws.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import ee.tlu.springrestfulws.dto.School;
import ee.tlu.springrestfulws.dto.Student;
import ee.tlu.springrestfulws.exception.ResourceNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SchoolRepository {

	private static Map<Long, School> schools = new HashMap<>();
	
	static {
		Map<Long, Student> s1 = new HashMap<>();
		s1.put((long) (s1.size() + 1), new Student(s1.size() + 1, "Juku", "Juurikas"));
		s1.put((long) (s1.size() + 1), new Student(s1.size() + 1, "Mari", "Maasikas"));
		Map<Long, Student> s2 = new HashMap<>();
		s2.put((long) (s2.size() + 1), new Student(s2.size() + 1, "Mart", "Murakas"));
		s2.put((long) (s2.size() + 1), new Student(s2.size() + 1, "Teele", "Tamme"));
		schools.put((long) (schools.size() + 1), new School(schools.size() + 1, "Gustav Adolfi Gümnaasium", s1));
		schools.put((long) (schools.size() + 1), new School(schools.size() + 1, "Kadrioru Saksa Gümnaasium", s2));
	}

	public Flux<School> all() {
		return Flux.fromIterable(schools.values());
	}

	public Mono<School> findById(Long id) {
		return Mono.justOrEmpty(schools.get(id));
	}

	public Mono<Void> save(Mono<School> school) {
		return school.doOnNext(value -> {
			long id = schools.size() + 1;
			value.setId(id);
			schools.put(id, value);
		}).thenEmpty(Mono.empty());
	}
	
	public Mono<School> update(Long id, Mono<School> school) {
		Mono<School> existing = Mono.justOrEmpty(schools.get(id));
		return existing.doOnNext(e -> {
			school.doOnNext(s -> {
				e.setName(s.getName());
				e.setStudents(s.getStudents());
			});
		}).switchIfEmpty(Mono.error(new ResourceNotFoundException("School with the id of " + id + " does not exist")));
	}
	
	public Mono<Void> delete(Long id) {
		schools.remove(id);
		return Mono.empty();
	}

	public Map<Long, School> getSchools() {
		return schools;
	}

}
