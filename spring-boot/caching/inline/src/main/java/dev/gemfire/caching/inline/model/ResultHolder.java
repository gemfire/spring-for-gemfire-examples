// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.inline.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract Data Type (ADT) and persistent entity modeling the results of a mathematical calculation.
*
 * @see java.io.Serializable
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.IdClass
 * @see jakarta.persistence.Table
 * @since 1.1.0
 */
@Entity
@IdClass(ResultHolder.ResultKey.class)
@Table(name = "Calculations")
public class ResultHolder implements Serializable {

	@Id
	private Integer operand;

	@Id
	@Enumerated(EnumType.STRING)
	private Operator operator;

	private Integer result;

	protected ResultHolder() { }

	public ResultHolder(int operand, Operator operator, int result) {
		this.operator = operator;
		this.operand = operand;
		this.result = result;
	}

	public static ResultHolder of(int operand, Operator operator, int result) {
		return new ResultHolder(operand, operator, result);
	}

	@Override
	public String toString() {
		return operator.toString(operand, result);
	}

	public int getResult() {
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ResultHolder that = (ResultHolder) o;

		if (!Objects.equals(operand, that.operand)) return false;
		if (operator != that.operator) return false;
		return Objects.equals(result, that.result);
	}

	@Override
	public int hashCode() {
		int result1 = operand != null ? operand.hashCode() : 0;
		result1 = 31 * result1 + (operator != null ? operator.hashCode() : 0);
		result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
		return result1;
	}

	public static class ResultKey implements Serializable {

		private Integer operand;

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			ResultKey resultKey = (ResultKey) o;

			if (!Objects.equals(operand, resultKey.operand)) return false;
			return operator == resultKey.operator;
		}

		@Override
		public int hashCode() {
			int result = operand != null ? operand.hashCode() : 0;
			result = 31 * result + (operator != null ? operator.hashCode() : 0);
			return result;
		}

		private Operator operator;

		protected ResultKey() { }

		public ResultKey(int operand, Operator operator) {
			this.operand = operand;
			this.operator = operator;
		}

		public static ResultKey of(int operand, Operator operator) {
			return new ResultKey(operand, operator);
		}
	}
}
