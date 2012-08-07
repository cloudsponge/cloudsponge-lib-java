package com.cloudsponge.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represent a user contact. Contains the contact's name along with emails and
 * phone numbers.
 * 
 * @author andrenpaes
 */
public class Contact implements Serializable {

	private static final long serialVersionUID = -6114808751708283994L;

	private String firstName;

	private String lastName;

	private final List<Email> emailAddresses = new ArrayList<Email>();

	private final List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();

	public Contact() {
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

	public List<Email> getEmailAddresses() {
		return emailAddresses;
	}

	public void addEmail(Email email) {
		emailAddresses.add(email);
	}

	public List<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void addPhoneNumber(PhoneNumber phoneNumber) {
		phoneNumbers.add(phoneNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Contact) {
			Contact that = (Contact) obj;
			return new EqualsBuilder()
					.append(this.getFirstName(), that.getFirstName())
					.append(this.getLastName(), that.getLastName())
					.append(this.getPhoneNumbers(), that.getPhoneNumbers())
					.append(this.getEmailAddresses(), that.getEmailAddresses())
					.isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(321, 123).append(this.getFirstName())
				.append(this.getLastName()).append(this.getPhoneNumbers())
				.append(this.getEmailAddresses()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("first-name", this.getFirstName())
				.append("last-name", this.getLastName())
				.append("phones", this.getPhoneNumbers())
				.append("emails", this.getEmailAddresses()).toString();
	}

	public static class Email {
		private String emailAddress;

		private String type;

		public Email() {
		}

		public Email(String emailAddress) {
			this(emailAddress, null);
		}

		public Email(String emailAddress, String type) {
			this.emailAddress = emailAddress;
			this.type = type;
		}

		public String getEmailAddress() {
			return emailAddress;
		}

		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof Email) {
				final Email that = (Email) obj;
				return new EqualsBuilder()
						.append(this.getEmailAddress(), that.getEmailAddress())
						.append(this.getType(), that.getType()).isEquals();
			}
			return false;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(65465, 551)
					.append(this.getEmailAddress()).append(this.getType())
					.hashCode();
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
					.append("email", this.getEmailAddress())
					.append("type", this.getType()).toString();
		}
	}

	public static class PhoneNumber {
		private String number;

		private String phoneType;

		public PhoneNumber() {
		}

		public PhoneNumber(String number) {
			this(number, null);
		}

		public PhoneNumber(String number, String type) {
			this.number = number;
			this.phoneType = type;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getPhoneType() {
			return phoneType;
		}

		public void setPhoneType(String phoneType) {
			this.phoneType = phoneType;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof PhoneNumber) {
				final PhoneNumber that = (PhoneNumber) obj;
				return new EqualsBuilder()
						.append(this.getNumber(), that.getNumber())
						.append(this.getPhoneType(), that.getPhoneType())
						.isEquals();
			}
			return false;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(-65465, 1551).append(this.getNumber())
					.append(this.getPhoneType()).hashCode();
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
					.append("number", this.getNumber())
					.append("type", this.getPhoneType()).toString();
		}
	}
}
