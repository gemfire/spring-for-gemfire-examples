/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package com.vmware.gemfire.integration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.io.Serializable;

@Entity
@Region("orders")
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @jakarta.persistence.Id
    private final Long id;
    @Column(name = "product")
    private final String productName;

    @Column(name = "quantity")
    private final Integer quantity;

    @Column(name = "cost")
    private final String cost;

    @Column(name = "recipient")
    private final String recipientName;

    @Column(name = "address")
    private final String recipientAddress;

    public Order(Long id, String productName, Integer quantity, String cost, String recipientName, String recipientAddress) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.cost = cost;
        this.recipientName = recipientName;
        this.recipientAddress = recipientAddress;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getCost() {
        return cost;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", cost=" + cost +
                ", recipientName='" + recipientName + '\'' +
                ", recipientAddress='" + recipientAddress + '\'' +
                '}';
    }
}
