package com.cloudsponge.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class UserContacts extends CloudSpongeResponse {

	private static final long serialVersionUID = -5450084963732739033L;

	private Contact owner;

	private final List<Contact> contacts = new ArrayList<Contact>();

	public Contact getOwner() {
		return owner;
	}

	public void setOwner(Contact owner) {
		this.owner = owner;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void addContact(Contact contact) {
		contacts.add(contact);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof UserContacts && super.equals(obj)) {
			final UserContacts that = (UserContacts) obj;
			return new EqualsBuilder().append(this.getOwner(), that.getOwner())
					.isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(987, 6543).append(this.getImportId())
				.append(this.getOwner()).toHashCode();
	}

	@Override
	protected ToStringBuilder toStringBuilder() {
		return super.toStringBuilder().append("owner", this.getOwner());
	}
}
