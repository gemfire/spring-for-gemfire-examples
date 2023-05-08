/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function.domain;


import java.math.BigDecimal;

/**
 * A LineItem used in the examples
 */
public class LineItem {

	private Product product;
	private Integer amount;

	public LineItem() {
	}

	@Override
	public String toString() {
		return "LineItem{" +
				"product=" + product +
				", amount=" + amount +
				'}';
	}

	public LineItem(Product product, Integer amount) {
		this.product = product;
		this.amount = amount;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public BigDecimal calcTotal() {
		return product.getPrice().multiply(BigDecimal.valueOf(amount));
	}
}
