# Http Session Caching Example

## Description
In this example we will show you how to cache HTTP sessions using GemFire.

## Prerequisites
In order to run the following example one requires the following to be set up:
* Java 17
* Latest VMware GemFire is installed locally
* Set up local `gradle` or `maven` environment by following Steps 1-6 and 8 from [Spring Boot For VMware GemFire Quickstart](https://docs.vmware.com/en/Spring-Boot-for-VMware-GemFire/index.html#spring-boot-for-vmware-gemfire-quick-start-0)

## Steps to run
1. Clone `Spring For GemFire Examples` project
2. Start GemFire server-side processes
    1. Open new Terminal
    2. `cd` into the `caching/http-session` project directory.
    3. Start `gfsh` located in the `{VMware_GemFire_installation_location}/bin` directory
    4. Launch the Locator and Server by running `start.gfsh` file located in the `http-session` project directory in `gfsh`. <br> e.g `run --file={pathToStartGfshFile}/start.gfsh`
    5. Confirm that the locator and server are running by running `list members` in `gfsh`, which should contain two entries.
3. Start Client
    1. Open a new Terminal
    2. `cd` into the `http-session` project directory
    3. Run `gradle clean bootRun` to run the client


## Expected Outcome

Hit `http://localhost:8080/session` and see the session request counter increase with each request.

## Shut down and Cleanup
Run the following commands to shut down and cleanup:
In the Server Terminal window (assuming `gfsh` is still active):
1. Run `shutdown --include-locators`
2. Exit from the `gfsh` by running `quit`
3. Delete the created directories `example-locator` and `example-server`