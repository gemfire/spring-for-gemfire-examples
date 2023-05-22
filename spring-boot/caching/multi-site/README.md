# Multi-site Caching Example

## Description
In this example we will show you how to implement multi-site caching using GemFire.

## Prerequisites
In order to run the following example one requires the following to be set up:
* Java 17
* Latest VMware GemFire is installed locally
* Set up local `gradle` or `maven` environment by following Steps 1-6 and 8 from [Spring Boot For VMware GemFire Quickstart](https://docs.vmware.com/en/Spring-Boot-for-VMware-GemFire/index.html#spring-boot-for-vmware-gemfire-quick-start-0)

## Steps to run
1. Clone `Spring For GemFire Examples` project
2. Start GemFire server-side processes
    1. Open new Terminal
    2. `cd` into the `caching/multi-site` project directory.
    3. Start `gfsh` located in the `{VMware_GemFire_installation_location}/bin` directory
    4. Launch the site 1 Locator and Server by running `start.gfsh` file located in the `multi-site` project directory in `gfsh`. <br> e.g `run --file={pathToStartGfshFile}/start-site1.gfsh`
    5. Launch the site 2 Locator and Server by running `start.gfsh` file located in the `multi-site` project directory in `gfsh`. <br> e.g `run --file={pathToStartGfshFile}/start-site2.gfsh`
    6. Confirm that the locator and server are running by running `list members` in `gfsh`, which should contain two entries.
3. Start Client
    1. Open a new Terminal
    2. `cd` into the `multi-site` project directory
    3. Run `gradle clean bootRunSite1` to run the site1 client.
    4. Run `gradle clean bootRunSite2` to run the site2 client.


## Expected Outcome

Hit `localhost:8080/customers/bob`, it will be a cache miss.
Hit `localhost:8080/customers/bob` again, it will not be a cache miss.
Hit `localhost:9090/customers/bob`, it will not be a cache miss.
Hit `localhost:9090/customers/jon`, it will be a cache miss.
Bob wasn't a cache miss on site 2 because after bob was crated in site 1, the entry was replicated to site 2.


## Shut down and Cleanup
Run the following commands to shut down and cleanup:
In the Server Terminal window (assuming `gfsh` is still active):
1. Run `shutdown --include-locators`
2. Exit from the `gfsh` by running `quit`
3. Delete the created directories `locator-site1`, `server-site1`, `locator-site2`, and `server-site2`.