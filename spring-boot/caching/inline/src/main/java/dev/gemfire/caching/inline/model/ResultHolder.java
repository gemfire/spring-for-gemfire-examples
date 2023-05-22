/*
 * Copyright 2017-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package dev.gemfire.caching.inline.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/**
 * Abstract Data Type (ADT) and persistent entity modeling the results of a mathematical calculation.
*
 * @see java.io.Serializable
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.IdClass
 * @see jakarta.persistence.Table
 * @since 1.1.0
 */
// tag::class[]
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
// end::class[]
