package org.dhis2.ehealthMobile.io.models.useraccount;

public class UserAccount {

	public static class UserAccountBuilder {
		private String id;
		private String username;
		private String firstName;
		private String surname;
		private String email;
		private String phoneNumber;
		private String gender;
		private UserAccountSettings settings;

		public UserAccountBuilder setId(String id) {
			this.id = id;
			return this;
		}

		public UserAccountBuilder setUsername(String username) {
			this.username = username;
			return this;
		}

		public UserAccountBuilder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public UserAccountBuilder setSurname(String surname) {
			this.surname = surname;
			return this;
		}

		public UserAccountBuilder setEmail(String email) {
			this.email = email;
			return this;
		}

		public UserAccountBuilder setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}

		public UserAccountBuilder setGender(String gender) {
			this.gender = gender;
			return this;
		}

		public UserAccountBuilder setSettings(UserAccountSettings settings) {
			this.settings = settings;
			return this;
		}

		public UserAccount createUserAccount() {
			return new UserAccount(id, username, firstName, surname, email, phoneNumber, gender, settings);
		}
	}

	public String id;
	public String username;
	public String firstName;
	public String surname;
	public String email;
	public String phoneNumber;
	public String gender;
	public UserAccountSettings settings;

	public UserAccount(String id, String username, String firstName, String surname, String email, String phoneNumber, String gender, UserAccountSettings settings) {
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.surname = surname;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.settings = settings;
	}
}
