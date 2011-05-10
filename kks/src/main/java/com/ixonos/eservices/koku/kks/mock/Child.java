package com.ixonos.eservices.koku.kks.mock;

public class Child {

	private String firstName;
	private String secondName;
	private String lastName;
	private String socialSecurityNumber;

	public Child() {
		this.firstName = "";
		this.secondName = "";
		this.lastName = "";
	}

	public Child(String firstName, String secondName, String lastName) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

}
