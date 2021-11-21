package c195.model;

import java.util.Objects;

public class Contact {
    private long contactID;
    private String contactName;
    private String email;

    public long getContactID() {
        return contactID;
    }

    public void setContactID(long contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return contactID == contact.contactID && Objects.equals(contactName, contact.contactName) && Objects.equals(email, contact.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactID, contactName, email);
    }

    @Override
    public String toString() {
        return contactName;
    }
}
