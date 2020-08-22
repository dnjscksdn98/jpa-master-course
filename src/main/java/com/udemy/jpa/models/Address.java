package com.udemy.jpa.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Address {

    @Column(length = 50)
    private String country;

    @Column(length = 50)
    private String city;

    @Column(length = 5)
    private String zip;

    protected Address() {}

    private Address(String country, String city, String zip) {
        this.country = country;
        this.city = city;
        this.zip = zip;
    }

    public static Address of(String country, String city, String zip) {
        return new Address(country, city, zip);
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s", country, city, zip);
    }
}
