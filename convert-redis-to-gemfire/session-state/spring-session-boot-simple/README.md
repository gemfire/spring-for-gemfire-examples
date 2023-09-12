<!--
Copyright 2019 - 2021 VMware, Inc.
SPDX-License-Identifier: Apache-2.0
-->

# Spring Session Sample Boot Simple

The projects in this directory illustrate a standard Spring Boot application using Spring Session to save session data
with either Redis or VMware GemFire. In this guide, we will highlight the changes necessary for switching from Redis to
VMware GemFire for session state caching utilizing the
[Spring Boot for VMware GemFire](https://docs.vmware.com/en/Spring-Boot-for-VMware-GemFire/1.0/sbgf/index.html) Spring dependency.

## How to Convert from Redis to Tanzu GemFire

### Update `build.gradle`
The Spring Boot Redis dependencies need to be updated to use Spring Boot for VMware GemFire.

Remove these dependencies:

```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
implementation 'org.springframework.session:spring-session-data-redis'
```

Replace them with these dependencies:

```groovy
dependencies {
implementation 'com.vmware.gemfire:spring-boot-3.1-vmware-gemfire-10.0:1.0.0'
implementation 'com.vmware.gemfire:spring-boot-session-3.1-vmware-gemfire-10.0:1.0.0'
...
}
```


### Add `@EnableClusterAware`
In your main application or config class (in this example `Application.java`), import and add the `@EnableClusterAware` 
annotation:

```java
import org.springframework.geode.config.annotation.EnableClusterAware;

@SpringBootApplication
@EnableClusterAware
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```

### Remove Redis Bean from `WebMvcConfig`
This example needed the following Bean to use Redis on the Tanzu Application Service. You can remove this bean and its imports
if your app is currently using it for Redis. It is not needed for Tanzu GemFire.

```java
@Bean
public static ConfigureRedisAction configureRedisAction() {
    return ConfigureRedisAction.NO_OP;
}
```

### Remove `SessionConfig` class
This class has configuration for a Redis session that is no longer needed for Tanzu GemFire.

### Optional/Housekeeping
For most projects, the following changes will not be necessary, but in this example the Tanzu GemFire application is a
separate, self-contained project and these tweaks were needed:

- In `settings.gradle`, update the `rootProject.name` from `bootsimple.session.redis` to `bootsimple.session.gemfire`.
- In `manifest.yml`, update the JAR name in `path` from `bootsimple.session.redis` to `bootsimple.session.gemfire-0.0.1-SNAPSHOT.jar`.

## Running the Tanzu GemFire Application

Navigate to the Tanzu GemFire application directory and execute the following command:
```bash
./gradlew bootRun
```

Go to localhost:8080 in your browser of choice. You should see a login screen like the following:
![login page](readme-images/login-page.png)

If you haven't changed the password or user, you can login with the username of user and password of password.

Once you've logged in successfully, you should see a page similar to the following:
![secured page](readme-images/secured-page.png)

You should be able to refresh the page or close the tab and open a new one, but when you navigate back to the
application you will still be logged in.

**Note:** When running these examples on the Tanzu Application Service, you will need to update the manifest.yml file to bind to your
Redis or Tanzu GemFire service instance.

## Notes on Testing
For these applications, the intention was to demonstrate how to migrate from Redis to Tanzu GemFire.  If your tests are 
not specific to either framework, they should still pass once you've migrated.