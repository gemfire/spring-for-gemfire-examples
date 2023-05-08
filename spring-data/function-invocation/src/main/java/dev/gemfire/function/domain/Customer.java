/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function.domain;

import org.springframework.data.gemfire.mapping.annotation.Region;

/**
 * A customer used for examples.
 */
@Region(name = "Customers")
public class Customer {

    private Long id;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", emailAddress=" + emailAddress +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    private EmailAddress emailAddress;
    private String firstName;
    private String lastName;


    public Customer(Long id, EmailAddress emailAddress, String firstName, String lastName) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public Customer() {
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
}
