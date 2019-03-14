package com.security.acldoc.bean;

//import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

//@Audited
//@XmlRootElement(name = "Book")
@Entity
public class Book extends AbstractSecuredEntity {
	private static final long serialVersionUID = 1L;


	private String bookName;
	
	private String description;

	private String author;

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "Book [bookName=" + bookName + ", description=" + description
				+ ", author=" + author + "]";
	}

}
