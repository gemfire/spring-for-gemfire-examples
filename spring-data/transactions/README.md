# Transactions Example

## Description
In this example a GemFire client will be configured to perform the basic CRUD operations within a transaction. First, it will do a successful transaction where entries are saved to the server, and then a failed transaction where all changes are reverted.

## Prerequisites
In order to run the following example one requires the following to be set up:
* Java 17
* Latest VMware GemFire is installed locally
* Set up local `gradle` or `maven` environment by following Steps 1-6 and 8 from [Spring Boot For VMware GemFire Quickstart](https://docs.vmware.com/en/Spring-Boot-for-VMware-GemFire/index.html#spring-boot-for-vmware-gemfire-quick-start-0)

## Steps to run
1. Clone `Spring For GemFire Examples` project
2. Start GemFire server-side processes
    1. Open new Terminal
    2. `cd` into the `transactions` project directory, `spring-examples/spring-data/transactions`
    3. Start `gfsh` located in the `{VMware_GemFire_installation_location}/bin` directory
    4. Launch the Locator and Server by running `start.gfsh` file located in the `transactions` project directory in `gfsh`. <br> e.g `run --file={pathToStartGfshFile}/start.gfsh`
    5. Confirm that the locator and server are running by running `list members` in `gfsh`, which should contain two entries.
3. Start Client
    1. Open a new Terminal
    2. `cd` into the `transactions` project directory
    3. Run `./gradlew clean bootRun`


## Expected Outcome
```
Number of Entries stored before = 0
Number of Entries stored after = 5
Original Customer for ID before (transaction commit success) = Customer[id=2, emailAddress=EmailAddress[value=2@2.com], firstName=Franky, lastName=Hamilton]
Updated Customer for ID after (transaction commit success) = Customer[id=2, emailAddress=EmailAddress[value=2@2.com], firstName=Humpty, lastName=Hamilton]
Attempting to update the Customer again: Customer[id=2, emailAddress=EmailAddress[value=2@2.com], firstName=Numpty, lastName=Hamilton]
Unchanged Customer for ID after (transaction commit failure) = Customer[id=2, emailAddress=EmailAddress[value=2@2.com], firstName=Humpty, lastName=Hamilton]
java.lang.IllegalArgumentException: This is an expected exception that should fail the transactions
	at dev.gemfire.client.transactions.service.CustomerService.updateCustomersFailure(CustomerService.java:71)
Updated Customer for ID after 2 updates with delay = Customer[id=2, emailAddress=EmailAddress[value=2@2.com], firstName=Frumpy, lastName=Hamilton]
```


## Shut down and Cleanup
Run the following commands to shut down and cleanup:
   In the Server Terminal window (assuming `gfsh` is still active):
   1. Run `shutdown --include-locators`
   2. Exit from the `gfsh` by running `quit`
   3. Delete the created directories `example-locator` and `example-server`
