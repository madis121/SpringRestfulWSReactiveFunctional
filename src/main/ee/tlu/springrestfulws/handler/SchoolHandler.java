package ee.tlu.springrestfulws.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import ee.tlu.springrestfulws.dto.School;
import ee.tlu.springrestfulws.service.impl.SchoolRepository;
import reactor.core.publisher.Mono;

public class SchoolHandler {

	private SchoolRepository schoolRepository;
	
	public SchoolHandler(SchoolRepository schoolRepository) {
		this.schoolRepository = schoolRepository;
	}

	public Mono<ServerResponse> getAllSchools(ServerRequest request) {
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(schoolRepository.all(), School.class);
	}

	public Mono<ServerResponse> getSchool(ServerRequest request) {
		long id = Long.valueOf(request.pathVariable("id"));
		Mono<School> school = schoolRepository.findById(id);
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();
		return school.flatMap(value -> ServerResponse.ok().contentType(APPLICATION_JSON).body(school, School.class))
				.switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> insertSchool(ServerRequest request) {
		Mono<School> school = request.bodyToMono(School.class);
		Mono<Void> result = schoolRepository.save(school);
		return ServerResponse.ok().build(result);
	}

	public Mono<ServerResponse> updateSchool(ServerRequest request) {
		long id = Long.valueOf(request.pathVariable("id"));
		Mono<School> school = request.bodyToMono(School.class);
		Mono<School> result = schoolRepository.update(id, school);
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();
		return result.flatMap(value -> ServerResponse.ok().contentType(APPLICATION_JSON).body(result, School.class))
				.switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> removeSchool(ServerRequest request) {
		long id = Long.valueOf(request.pathVariable("id"));
		Mono<Void> result = schoolRepository.delete(id);
		return ServerResponse.ok().build(result);
	}

}
