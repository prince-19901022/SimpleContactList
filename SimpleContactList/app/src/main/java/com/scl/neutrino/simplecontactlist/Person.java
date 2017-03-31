package com.scl.neutrino.simplecontactlist;

/**
 * Created by Nutrino on 3/31/2017.
 */

public class Person {

    private String personName;
    private String contact;

    public Person(String personName, String contact) {
        this.personName = personName;
        this.contact = contact;
    }

    public String getPersonName() {
        return personName;
    }

    public String getContact() {
        return contact;
    }
}
