package model;

import java.io.Serializable;

public final class Customer implements Serializable {
	private final String name;
	private final String address;

	public Customer(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
}