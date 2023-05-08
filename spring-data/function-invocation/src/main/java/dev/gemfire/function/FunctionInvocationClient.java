/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.gemfire.function;

import dev.gemfire.function.domain.*;
import dev.gemfire.function.execution.CustomerFunctionExecution;
import dev.gemfire.function.execution.OrderFunctionExecution;
import dev.gemfire.function.execution.ProductFunctionExecution;
import dev.gemfire.function.repository.CustomerRepository;
import dev.gemfire.function.repository.OrderRepository;
import dev.gemfire.function.repository.ProductRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@SpringBootApplication
public class FunctionInvocationClient {

    private static final Log logger = LogFactory.getLog(FunctionInvocationClient.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(FunctionInvocationClient.class).web(WebApplicationType.NONE).build().run(args);
    }

    @Bean
    public ApplicationRunner runner(CustomerRepository customerRepository, OrderRepository orderRepository, ProductRepository productRepository, CustomerFunctionExecution customerFunctionExecution, OrderFunctionExecution orderFunctionExecution, ProductFunctionExecution productFunctionExecution) {
        return args -> {
            createCustomerData(customerRepository);

            List<Customer> cust = customerFunctionExecution.listAllCustomersForEmailAddress("2@2.com", "3@3.com").get(0);
            assert cust.size() == 2;
            logger.info("All customers for emailAddresses:3@3.com,2@2.com using function invocation: \n\t " + cust);

            createProducts(productRepository);
            BigDecimal sum = productFunctionExecution.sumPricesForAllProducts();
            assert sum.equals(BigDecimal.valueOf(1499.97));
            logger.info("Running function to sum up all product prices: \n\t" + sum);

            createOrders(orderRepository, productRepository);

            sum = orderFunctionExecution.sumPricesForAllProductsForOrder(1L).get(0);
            assert sum.compareTo(BigDecimal.valueOf(99.99)) >= 0;
            logger.info("Running function to sum up all order lineItems prices for order 1: \n\t" + sum);
            Order order = orderRepository.findById(1L).get();
            logger.info("For order: \n\t " + order);
        };
    }

    public void createCustomerData(CustomerRepository customerRepository) {

        logger.info("Inserting 3 entries for keys: 1, 2, 3");

        customerRepository.save(new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith"));
        customerRepository.save(new Customer(2L, new EmailAddress("3@3.com"), "Frank", "Lamport"));
        customerRepository.save(new Customer(3L, new EmailAddress("5@5.com"), "Jude", "Simmons"));
    }

    public void createProducts(ProductRepository productRepository) {

        productRepository.save(new Product(1L, "Apple iPod", new BigDecimal("99.99"), "An Apple portable music player"));
        productRepository.save(new Product(2L, "Apple iPad", new BigDecimal("499.99"), "An Apple tablet device"));
        var macbook = new Product(3L, "Apple macBook", new BigDecimal("899.99"), "An Apple notebook computer");
        macbook.addAttribute("warranty", "included");

        productRepository.save(macbook);
    }

    public void createOrders(OrderRepository orderRepository, ProductRepository productRepository) {

        var random = new Random();
        var address = new Address("it", "doesn't", "matter");

        LongStream.rangeClosed(1, 100).forEach((orderId) -> LongStream.rangeClosed(1, 3).forEach((customerId) -> {
            var order = new Order(orderId, customerId, address);
            IntStream.rangeClosed(0, random.nextInt(3) + 1).forEach((lineItemCount) -> {
                var quantity = random.nextInt(3) + 1;
                long productId = random.nextInt(3) + 1;
                order.add(new LineItem(productRepository.findById(productId).get(), quantity));
            });
            orderRepository.save(order);
        }));
    }
}

