/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.gemfire.function.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Orders object used in the examples
 */
@Region("Orders")
public class Order {
	@Override
	public String toString() {
		return "Order{" +
				"id=" + id +
				", customerId=" + customerId +
				", billingAddress=" + billingAddress +
				", shippingAddress=" + shippingAddress +
				", lineItems=" + lineItems +
				'}';
	}

	@Id
	private Long id;
	private Long customerId;
	private Address billingAddress;
	private Address shippingAddress;
	private List<LineItem> lineItems = new ArrayList<>();

	public Order(Long orderId, Long customerId, Address address) {
		this.id = orderId;
		this.customerId = customerId;
		this.billingAddress = address;
		this.shippingAddress = address;
	}

	/**
	 * Returns the total of the [Order].
	 *
	 * @return
	 */

	public BigDecimal calcTotal() {
		if (lineItems.size() == 0) {
			return BigDecimal.ZERO;
		} else {
			return lineItems.stream().map(LineItem::calcTotal).reduce(BigDecimal::add).get();
		}
	}

	public Order() {
	}

	/**
	 * Adds the given [LineItem] to the [Order].
	 *
	 * @param lineItem a new {@link LineItem}
	 */
	public void add(LineItem lineItem) {
		lineItems.add(lineItem);
	}

}
