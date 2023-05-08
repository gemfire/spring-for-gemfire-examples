/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function.domain;


/**
 * Value object to represent email addresses.
 */
public class EmailAddress {
    private String value;

    @Override
    public String toString() {
        return "EmailAddress{" +
                "value='" + value + '\'' +
                '}';
    }

    public EmailAddress() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public EmailAddress(String value) {
        this.value = value;
    }
}
