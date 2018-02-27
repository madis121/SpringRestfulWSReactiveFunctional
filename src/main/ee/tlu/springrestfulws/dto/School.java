package ee.tlu.springrestfulws.dto;

import java.util.HashMap;
import java.util.Map;

public class School {

	private long id;

	private String name;

	private Map<Long, Student> students;

	public School() {

	}

	public School(long id, String name) {
		this.id = id;
		this.name = name;
		this.students = new HashMap<>();
	}

	public School(long id, String name, Map<Long, Student> students) {
		this.id = id;
		this.name = name;
		this.students = students;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Long, Student> getStudents() {
		return students;
	}

	public void setStudents(Map<Long, Student> students) {
		this.students = students;
	}

	@Override
	public String toString() {
		return "School [id=" + id + ", name=" + name + ", students=" + students + "]";
	}

}
