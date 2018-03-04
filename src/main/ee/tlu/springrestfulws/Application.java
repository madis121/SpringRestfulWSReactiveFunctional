package ee.tlu.springrestfulws;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServletHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;

import ee.tlu.springrestfulws.handler.SchoolHandler;
import ee.tlu.springrestfulws.handler.StudentHandler;
import ee.tlu.springrestfulws.service.impl.SchoolRepository;
import ee.tlu.springrestfulws.service.impl.StudentRepository;

public class Application {
	
	public static void main(String[] args) throws Exception {
		Application application = new Application();
		HttpHandler httpHandler = application.httpHandler();
		application.startTomcat(httpHandler);
		System.out.println("Press ENTER to exit.");
		System.in.read();
	}

	private HttpHandler httpHandler() {
		SchoolRepository schoolRepository = new SchoolRepository();
		StudentRepository studentRepository = new StudentRepository(schoolRepository);
		SchoolHandler schoolHandler = new SchoolHandler(schoolRepository);
		StudentHandler studentHandler = new StudentHandler(studentRepository);
	
		RouterFunction<?> route = 
			route(GET("/schools/{id}"), schoolHandler::getSchool)
			.andRoute(GET("/schools"), schoolHandler::getAllSchools)
			.andRoute(POST("/schools").and(contentType(APPLICATION_JSON)), schoolHandler::insertSchool)
			.andRoute(PUT("/schools/{id}").and(contentType(APPLICATION_JSON)), schoolHandler::updateSchool)
			.andRoute(DELETE("/schools/{id}"), schoolHandler::removeSchool)
			.andRoute(GET("/schools/{schoolId}/students/{studentId}"), studentHandler::getStudent)
			.andRoute(GET("/schools/{schoolId}/students"), studentHandler::getAllStudents)
			.andRoute(POST("/schools/{schoolId}/students").and(contentType(APPLICATION_JSON)), studentHandler::insertStudent)
			.andRoute(PUT("/schools/{schoolId}/students/{studentId}").and(contentType(APPLICATION_JSON)), studentHandler::updateStudent)
			.andRoute(DELETE("/schools/{schoolId}/students/{studentId}"), studentHandler::removeStudent);
		
		return toHttpHandler(route);
	}
	
	private void startTomcat(HttpHandler httpHandler) throws LifecycleException {
		Tomcat tomcat = new Tomcat();
		tomcat.setHostname("localhost");
		tomcat.setPort(8080);
		Context rootContext = tomcat.addContext("", System.getProperty("java.io.tmpdir"));
		ServletHttpHandlerAdapter servlet = new ServletHttpHandlerAdapter(httpHandler);
		Tomcat.addServlet(rootContext, "httpHandlerServlet", servlet);
		rootContext.addServletMappingDecoded("/", "httpHandlerServlet");
		tomcat.start();
	}
	
}
