/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package com.vmware.gemfire.integration;

import net.datafaker.Faker;
import org.apache.geode.cache.Region;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gemfire.inbound.CacheListeningMessageProducer;
import org.springframework.integration.gemfire.inbound.EventType;
import org.springframework.integration.gemfire.outbound.CacheWritingMessageHandler;
import org.springframework.integration.jdbc.JdbcMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.util.Map;

@SpringBootApplication
@EnableIntegration
@ClientCacheApplication
@EnableEntityDefinedRegions
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
@EnableScheduling
public class IntegratorApplication {
    private static final String QUEUE_NAME = "orderQueue";
    private Long nextId = 0L;
    private final Faker faker = new Faker();
    private final RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(IntegratorApplication.class, args);
    }

    @Bean
    public MessageChannel rabbitToGemFire() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public MessageChannel gemFireToPostgres() {
        return new DirectChannel();
    }

    // create a queue in RabbitMQ
    @Autowired
    public IntegratorApplication(ConnectionFactory connectionFactory, RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        Queue rabbitQueue = new Queue(QUEUE_NAME);
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        String response = rabbitAdmin.declareQueue(rabbitQueue);
        if(response == null) {
            throw new RuntimeException("Unable to create Rabbit queue");
        }
    }

    // send data to RabbitMQ
    @Scheduled(fixedDelay = 2000, initialDelay = 500)
    public void sendMessagesToRabbitMQ() {
        Order order = new Order(nextId++, faker.commerce().productName(), faker.number().randomDigitNotZero(), "$" + faker.commerce().price(), faker.name().fullName(), faker.address().fullAddress());
        rabbitTemplate.convertAndSend(QUEUE_NAME, order);
        System.out.println("Sent '" + order + "' to " + QUEUE_NAME);
    }

    // pipe data from RabbitMQ
    @Bean
    public IntegrationFlow rabbitInbound(ConnectionFactory connectionFactory) {
        return IntegrationFlow.from(Amqp.inboundAdapter(connectionFactory, QUEUE_NAME))
                .channel(rabbitToGemFire())
                .handle(m -> System.out.println("Message from RabbitMQ: " + m.getPayload()))
                .get();
    }

    // write data to GemFire
    @Bean
    @ServiceActivator(inputChannel = "rabbitToGemFire")
    public CacheWritingMessageHandler cacheWritingMessageHandler(@Qualifier("orders") Region<Long, Order> orderRegion) {
        CacheWritingMessageHandler cacheWritingMessageHandler = new CacheWritingMessageHandler(orderRegion);
        cacheWritingMessageHandler.setCacheEntries(Map.of("payload.getId()", "payload"));
        return cacheWritingMessageHandler;
    }

    // pipe data from GemFire
    @Bean
    public CacheListeningMessageProducer cacheListeningMessageProducer(@Qualifier("orders") Region<Long, Order> orderRegion) {
        CacheListeningMessageProducer cacheListeningMessageProducer = new CacheListeningMessageProducer(orderRegion);
        cacheListeningMessageProducer.setSupportedEventTypes(EventType.CREATED, EventType.UPDATED);
        cacheListeningMessageProducer.setPayloadExpressionString("newValue");
        cacheListeningMessageProducer.setOutputChannel(gemFireToPostgres());
        return cacheListeningMessageProducer;
    }

    // write data to Postgres
    @Bean
    @ServiceActivator(inputChannel = "gemFireToPostgres")
    public JdbcMessageHandler jdbcMessageHandler(DataSource dataSource) {
        return new JdbcMessageHandler(dataSource, "INSERT INTO Orders (id, product, quantity, cost, recipient, address) VALUES (:payload.id, :payload.productName, :payload.quantity, :payload.cost, :payload.recipientName, :payload.recipientAddress) ON CONFLICT DO NOTHING;");
    }
}
