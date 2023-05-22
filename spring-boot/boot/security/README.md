# Security Example

## Description
In this example we will show you how to configure a client to connect to a secured GemFire cluster using Spring Boot.

## Prerequisites
In order to run the following example one requires the following to be set up:
* Java 17
* Latest VMware GemFire is installed locally
* Set up local `gradle` or `maven` environment by following Steps 1-6 and 8 from [Spring Boot For VMware GemFire Quickstart](https://docs.vmware.com/en/Spring-Boot-for-VMware-GemFire/index.html#spring-boot-for-vmware-gemfire-quick-start-0)

## Steps to run
1. Clone `Spring For GemFire Examples` project
2. Start GemFire server-side processes
    1. Open new Terminal
    2. `cd` into the `boot/security` project directory.
    3. run `gradle jar` to build the artifact containing the security manager.
    4. Start `gfsh` located in the `{VMware_GemFire_installation_location}/bin` directory
    5. Replace `{project_directory}` in `start.gfsh` with the path to the project directory.
    6. Launch the Locator and Server by running `start.gfsh` file located in the `security` project directory in `gfsh`. <br> e.g `run --file={pathToStartGfshFile}/start.gfsh`
    7. Confirm that the locator and server are running by running `list members` in `gfsh`, which should contain two entries.
3. Start Client
    1. Open a new Terminal
    2. `cd` into the `security` project directory
    3. Run `gradle clean bootRun` to run the client

## Expected Outcome

```
Successfully put [Customer[id=2, name=William Evans]] in Region [Customers]
Attempting to read from Region [Customers]...
Read failed because "true not authorized for DATA:READ:Customers:2"
```

Hit `http://localhost:8080/message` and see the truststore using to configure SSL.

## Shut down and Cleanup
Run the following commands to shut down and cleanup:
In the Server Terminal window (assuming `gfsh` is still active):
1. Run `shutdown --include-locators`
2. Exit from the `gfsh` by running `quit`
3. Delete the created directories `example-locator` and `example-server`