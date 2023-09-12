<!--
Copyright 2019 - 2021 VMware, Inc.
SPDX-License-Identifier: Apache-2.0
-->

# Publish And Subscribe

The projects in this directory illustrate a Spring Boot application that creates a publish and subscribe channel with
either Redis or VMware GemFire. In this guide, we will highlight the changes necessary for switching from Redis to VMware GemFire for the publish/subscribe implementation.

In the VMware GemFire example, a Region will represent the equivalent of a PatternTopic as defined in the Redis example. 


## How to Convert from Redis to Tanzu GemFire

### Update `build.gradle`
The Spring Boot Redis dependencies need to be updated to use Spring Boot for VMware GemFire.

Remove these Redis dependencies:

```groovy
implementation "org.springframework.boot:spring-boot-starter-data-redis"
```

Replace them with this Spring dependency:

```groovy
dependencies {
    implementation 'com.vmware.gemfire:spring-boot-3.1-vmware-gemfire-10.0:1.0.0'
}
```

### Add `@EnableClusterAware` and `@EnableCachingDefinedRegions`
In your main application or config class (in this example `Application.java`), import and add the `@EnableClusterAware` 
annotations:

```java
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.geode.config.annotation.EnableClusterAware;

@SpringBootApplication
@EnableCachingDefinedRegions
@EnableClusterAware
public class Application {
...
```

### Replace Redis Beans
The Redis example uses the following Beans to implement the pub/sub pattern on the Tanzu Application Service. In the
RedisMessageListenerContainer Bean, the application defines the “chat” topic. 

```java
    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(connectionFactory);
        redisMessageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic("chat"));

        return redisMessageListenerContainer;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Subscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "receiveMessage");
    }

    @Bean
    Subscriber subscriber(CountDownLatch countDownLatch) {
        return new Subscriber(countDownLatch);
    }

    @Bean
    StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
```

Replace the above Redis Beans and their imports with the following GemFire equivalents in the Application.java
class:

```java
...

import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.util.CacheListenerAdapter;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.RegionConfigurer;

...

    @Bean
    CacheListenerAdapter<String, String> chatRegionSubscriber(CountDownLatch countDownLatch) {
        return new CacheListenerAdapter<String, String>() {
            public void afterCreate(EntryEvent<String, String> event) {
                LOGGER.info("Subscriber received <" + event.getNewValue() + ">");
                countDownLatch.countDown();
            }
        };
    }

    @Bean
    RegionConfigurer regionConfigurer(CacheListener<String, String> chatRegionSubscriber) {
        return new RegionConfigurer() {
            @Override
            public void configure(String regionName, ClientRegionFactoryBean<?, ?> regionFactory) {
                if (regionName.equals("chat")) {
                    LOGGER.info("Attaching cache listener to region: " + regionName);
                    regionFactory.setCacheListeners(
                            new CacheListener[]{chatRegionSubscriber});
                }
            }
        };
    }
```

The chatRegionSubscriber creates a CacheListenerAdapter that logs when the afterCreate method is triggered on a
region.  

The RegionConfigurer sets the chatRegionSubscriber as a CacheListener on the "chat" region when that region is
created.


### Update the `main` Method in `Application.java`

The Redis example uses this code to publish a message:

```java
StringRedisTemplate stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
CountDownLatch countDownLatch = applicationContext.getBean(CountDownLatch.class);

LOGGER.info("Sending message...");
stringRedisTemplate.convertAndSend("chat", "Hello from Redis!");
```

The GemFire application calls the sendMessage method on the MessageSender class to add an entry to the region that
the cache listener is subscribed to:

```java
CountDownLatch countDownLatch = applicationContext.getBean(CountDownLatch.class);
MessageSender messageSender = applicationContext.getBean(MessageSender.class);

LOGGER.info("Sending message...");
messageSender.sendMessage("Hello from Tanzu GemFire!");

```

### Remove `Subscriber.java`

This is not needed for GemFire; the subscriber is a CacheListenerAdapter bean in this application (as described above).

### Add a MessageSender class

The GemFire app uses the @Cacheable annotation in MessageSender.java to create a region (named “chat”) that caches
messages:
```java
...

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
    @Cacheable("chat")
    public String sendMessage(String message) {
        return message;
    }
}
```

### Optional/Housekeeping
For most projects, the following changes will not be necessary, but in this example the GemFire application is a
separate, self-contained project and these tweaks were needed:

- In `settings.gradle`, update the `rootProject.name` from `pubsub.redis` to `pubsub.gemfire`.
- In `manifest.yml`, update the JAR name in `path` from `pubsub.redis` to `pubsub.gemfire`.

## Running the GemFire Application
Navigate to the GemFire application directory and execute the following command:
```bash
./gradlew bootRun
```

Inspect the logs in the terminal. You should see a sequence of log entries like:

```bash
--- [main] o.s.d.g.client.ClientRegionFactoryBean   : Creating client Region [Messages]
--- [main] pubsub.gemfire.Application            : Started Application in 2.204 seconds (JVM running for 2.555)
--- [main] pubsub.gemfire.Application            : Sending message...
--- [main] pubsub.gemfire.Application            : Subscriber received <Hello from GemFire!>
```

After sending and receiving the message, the application will exit.

**Note:** When running these examples on the Tanzu Application Service, you will need to update the manifest.yml file to bind to your
Redis or Tanzu GemFire service instance.

## Notes on Testing
For these applications, the intention was to demonstrate how to migrate from Redis to Tanzu GemFire.  If your tests are 
not specific to either framework, they should still pass once you've migrated.