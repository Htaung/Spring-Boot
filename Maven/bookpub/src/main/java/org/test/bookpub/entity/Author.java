package org.test.bookpub.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Author {
	@Id
	@GeneratedValue
	private Long id;
	private String firstName;
	private String lastName;
	
	@OneToMany(mappedBy = "author")
	@JsonIgnore
	private List<Book> books;

	protected Author() {
	}

	public Author(Long id, String firstName, String lastName, List<Book> books) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.books = books;
	}
	
	
	public Author(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

}
