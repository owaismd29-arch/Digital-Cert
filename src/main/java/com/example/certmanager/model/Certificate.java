package com.example.certmanager.model;

import java.util.Objects;

public class Certificate {

	private String id;
	private String title;
	private String issuedTo;
	private String issuedBy;
	private int year;

	public Certificate() {
	}

	public Certificate(String id, String title, String issuedTo, String issuedBy, int year) {
		this.id = id;
		this.title = title;
		this.issuedTo = issuedTo;
		this.issuedBy = issuedBy;
		this.year = year;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIssuedTo() {
		return issuedTo;
	}

	public void setIssuedTo(String issuedTo) {
		this.issuedTo = issuedTo;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Certificate that = (Certificate) o;
		return year == that.year &&
				Objects.equals(id, that.id) &&
				Objects.equals(title, that.title) &&
				Objects.equals(issuedTo, that.issuedTo) &&
				Objects.equals(issuedBy, that.issuedBy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, issuedTo, issuedBy, year);
	}

	@Override
	public String toString() {
		return id + " | " + title + " | " + issuedTo + " | " + issuedBy + " | " + year;
	}
}
