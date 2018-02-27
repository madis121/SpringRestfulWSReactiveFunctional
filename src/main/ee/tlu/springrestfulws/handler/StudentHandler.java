package ee.tlu.springrestfulws.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import ee.tlu.springrestfulws.dto.Student;
import ee.tlu.springrestfulws.service.impl.StudentRepository;
import reactor.core.publisher.Mono;

public class StudentHandler {

	private StudentRepository studentRepository;
	
	public StudentHandler(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	public Mono<ServerResponse> getAllStudents(ServerRequest request) {
		long schoolId = Long.valueOf(request.pathVariable("schoolId"));
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(studentRepository.all(schoolId), Student.class);
	}

	public Mono<ServerResponse> getStudent(ServerRequest request) {
		long schoolId = Long.valueOf(request.pathVariable("schoolId"));
		long studentId = Long.valueOf(request.pathVariable("studentId"));
		Mono<Student> student = studentRepository.findById(schoolId, studentId);
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();
		return student.flatMap(value -> ServerResponse.ok().contentType(APPLICATION_JSON).body(student, Student.class))
				.switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> insertStudent(ServerRequest request) {
		long schoolId = Long.valueOf(request.pathVariable("schoolId"));
		Mono<Student> student = request.bodyToMono(Student.class);
		Mono<Void> result = studentRepository.save(schoolId, student);
		return ServerResponse.ok().build(result);
	}

	public Mono<ServerResponse> updateStudent(ServerRequest request) {
		long schoolId = Long.valueOf(request.pathVariable("schoolId"));
		long studentId = Long.valueOf(request.pathVariable("studentId"));
		Mono<Student> student = request.bodyToMono(Student.class);
		Mono<Student> result = studentRepository.update(schoolId, studentId, student);
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();
		return result.flatMap(value -> ServerResponse.ok().contentType(APPLICATION_JSON).body(result, Student.class))
				.switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> removeStudent(ServerRequest request) {
		long schoolId = Long.valueOf(request.pathVariable("schoolId"));
		long studentId = Long.valueOf(request.pathVariable("studentId"));
		Mono<Void> result = studentRepository.delete(schoolId, studentId);
		return ServerResponse.ok().build(result);
	}

}
