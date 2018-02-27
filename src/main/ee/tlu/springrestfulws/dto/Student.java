package ee.tlu.springrestfulws.dto;

public class Student {

	private long id;

	private String givenName;

	private String surname;

	public Student() {

	}

	public Student(long id, String givenName, String surname) {
		this.id = id;
		this.givenName = givenName;
		this.surname = surname;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", givenName=" + givenName + ", surname=" + surname + "]";
	}

}
