package tranlong5252.objects;

import java.util.Date;

public class Customer {

	private final String username;
	private String name;
	private String phone;
	private String gender;
	private Date dob;
	private boolean isSuspended;

	public Customer(String username, String name, String phone, String gender, Date dob, boolean isSuspended) {
		this.username = username;
		this.name = name;
		this.phone = phone;
		this.gender = gender;
		this.dob = dob;
		this.isSuspended = isSuspended;
	}

	public String username() {
		return username;
	}

	public String name() {
		return name;
	}

	public String phone() {
		return phone;
	}

	public String gender() {
		return gender;
	}

	public boolean isSuspended() {
		return isSuspended;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setSuspended(boolean suspended) {
		isSuspended = suspended;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}
}
