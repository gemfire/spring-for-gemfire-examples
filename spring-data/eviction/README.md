# Eviction Example

## Description
In this example we will show you ways to automatically evict data from your region if certain entry count or memory thresholds are reached.

## Prerequisites
In order to run the following example one requires the following to be set up:
* Java 17
* Latest VMware GemFire is installed locally
* Set up local `gradle` or `maven` environment by following Steps 1-6 and 8 from [Spring Boot For VMware GemFire Quickstart](https://docs.vmware.com/en/Spring-Boot-for-VMware-GemFire/index.html#spring-boot-for-vmware-gemfire-quick-start-0)

## Steps to run
1. Clone `Spring For GemFire Examples` project
2. Start GemFire server-side processes
   1. Open new Terminal
   2. `cd` into the `eviction` project directory, `spring-examples/spring-data/eviction`
   3. Start `gfsh` located in the `{VMware_GemFire_installation_location}/bin` directory
   4. Launch the Locator and Server by running `start.gfsh` file located in the `eviction` project directory in `gfsh`. <br> e.g `run --file={pathToStartGfshFile}/start.gfsh`
   5. Confirm that the locator and server are running by running `list members` in `gfsh`, which should contain two entries.
3. Start Client
   1. Open a new Terminal
   2. `cd` into the `eviction` project directory
   3. Run `./gradlew clean bootRun`


## Expected Outcome
```
Inserting 13 entries
Asserting that there are only 10 entries
Entries: 10
```

## Shut down and Cleanup
Run the following commands to shut down and cleanup:
1. In the Client Terminal window
   1. `Ctrl+C` to kill the running client app
2. In the Server Terminal window (assuming `gfsh` is still active):
   1. Run `shutdown --include-locators`
   2. Exit from the `gfsh` by running `quit`
   3. Delete the created directories