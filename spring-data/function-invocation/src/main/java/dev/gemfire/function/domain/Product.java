/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * A product used in the examples.
 */
@Region("Products")
public class Product {

	@Id
	private Long id;
	private String name;
	private BigDecimal price;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	private String description;

	public Product() {
	}

	@Transient
	private Map<String, String> attributes = new HashMap<>();

	public Product(Long id, String name, BigDecimal price, String description) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.description = description;
	}

	/**
	 * Sets the attribute with the given name to the given value.
	 *
	 * @param name  must not be null or empty.
	 * @param value
	 */
	public void addAttribute(String name, String value) {
		this.attributes.put(name, value);
	}

	@Override
	public String toString() {
		return "Product{" +
				"id=" + id +
				", name='" + name + '\'' +
				", price=" + price +
				", description='" + description + '\'' +
				", attributes=" + attributes +
				'}';
	}
}
