# Expiration Example

# Expiration Example

## Description
In this example we will show you ways to automatically expire data from your region.

Expiration removes data after it has existed for a certain amount of time, or after it has been unused for a certain amount of time. There are multiple ways to configure Expiration that will be shown in this example.
1. Configure an expiration policy on the region using the `@EnableExpiration` and `@ExpirationPolicy` annotations.
2. Configure a custom expiration policy by extending `CustomExpiry`.
3. Entity defined expiration using the `@IdleTimeoutExpiration` and `@TimeToLiveExpiration` annotations.

Entity defined expiration has its own client because it has a lower priority and is trumped by the expiration policy defined on the `@EnableExpiration` annotation. The polices defined by the `@EnableExpiration` annotation can be found in ExpirationPolicyConfig.

## Prerequisites
In order to run the following example one requires the following to be set up:
* Java 17
* Latest VMware GemFire is installed locally
* Set up local `gradle` or `maven` environment by following Steps 1-6 and 8 from [Spring Boot For VMware GemFire Quickstart](https://docs.vmware.com/en/Spring-Boot-for-VMware-GemFire/index.html#spring-boot-for-vmware-gemfire-quick-start-0)

## Steps to run
1. Clone `Spring For GemFire Examples` project
2. Start GemFire server-side processes
   1. Open new Terminal
   2. `cd` into the `expiration` project directory, `spring-examples/spring-data/expiration`
   3. Start `gfsh` located in the `{VMware_GemFire_installation_location}/bin` directory
   4. Launch the Locator and Server by running `start.gfsh` file located in the `expiration` project directory in `gfsh`. <br> e.g `run --file={pathToStartGfshFile}/start.gfsh`
   5. Confirm that the locator and server are running by running `list members` in `gfsh`, which should contain two entries.
3. Start Client
   1. Open a new Terminal
   2. `cd` into the `expiration` project directory
   3. Run `./gradlew clean bootRun` to run the main expiration client
   4. Run `./gradlew clean bootRunEntity` to run the entity-defined expiration client


## Expected Outcome
```
--- {expiration type} ---
Starting TTL wait period: 2023-05-22T19:51:21.994873Z
Ending TTL wait period: 2023-05-22T19:51:23.030213Z
Starting Idle wait period: 2023-05-22T19:51:23.037180Z
Ending Idle wait period: 2023-05-22T19:51:25.045327Z
```

## Shut down and Cleanup
Run the following commands to shut down and cleanup:
1. In the Client Terminal window
   1. `Ctrl+C` to kill the running client app
2. In the Server Terminal window (assuming `gfsh` is still active):
   1. Run `shutdown --include-locators`
   2. Exit from the `gfsh` by running `quit`
   3. Delete the created directories