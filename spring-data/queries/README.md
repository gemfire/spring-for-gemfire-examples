# Queries Example

## Description
In this example we will demonstrate different kinds of queries. The kinds of queries we will demonstrate are listed below.
1. OQL Queries - OQL is similar to SQl and can be used to query regions like SQl queries tables in a relational database.
2. Continuous Queries - Continuous queries are special OQl queries that run continuously and are updated when th returned result set changes (like when data is added/removed on the region)

## Prerequisites
In order to run the following example one requires the following to be set up:
* Java 17
* Latest VMware GemFire is installed locally
* Set up local `gradle` or `maven` environment by following Steps 1-6 and 8 from [Spring Boot For VMware GemFire Quickstart](https://docs.vmware.com/en/Spring-Boot-for-VMware-GemFire/index.html#spring-boot-for-vmware-gemfire-quick-start-0)

## Steps to run
1. Clone `Spring For GemFire Examples` project
2. Start GemFire server-side processes
    1. Open new Terminal
    2. `cd` into the `queries` project directory, `spring-examples/spring-data/queries`
    3. Start `gfsh` located in the `{VMware_GemFire_installation_location}/bin` directory
    4. Launch the Locator and Server by running `start.gfsh` file located in the `queries` project directory in `gfsh`. <br> e.g `run --file={pathToStartGfshFile}/start.gfsh`
    5. Confirm that the locator and server are running by running `list members` in `gfsh`, which should contain two entries.
3. Start Client
    1. Open a new Terminal
    2. `cd` into the `queries` project directory
    3. Run `./gradlew clean bootRun`


## Expected Outcome
```
--- Continuous Queries ---
Inserting 3 entries for keys: 1, 2, 3
Received message for CQ 'CustomerCQ'CqEvent [CqName=CustomerCQ; base operation=CREATE; cq operation=CREATE; key=2; value=Customer[id=2, emailAddress=EmailAddress[value=3@3.com], firstName=Frank, lastName=Lamport]]
Received message for CQ 'CustomerCQ'CqEvent [CqName=CustomerCQ; base operation=CREATE; cq operation=CREATE; key=1; value=Customer[id=1, emailAddress=EmailAddress[value=2@2.com], firstName=John, lastName=Smith]]
Received message for CQ 'CustomerCQ'CqEvent [CqName=CustomerCQ; base operation=CREATE; cq operation=CREATE; key=3; value=Customer[id=3, emailAddress=EmailAddress[value=5@5.com], firstName=Jude, lastName=Simmons]]
--- OQL Queries ---
Inserting 3 entries for keys: 1, 2, 3
Find customer with key=2 using GemFireRepository: Customer[id=2, emailAddress=EmailAddress[value=3@3.com], firstName=Frank, lastName=Lamport]
Find customer with key=2 using GemFireTemplate: [Customer[id=2, emailAddress=EmailAddress[value=3@3.com], firstName=Frank, lastName=Lamport]]
Find customers with emailAddress=3@3.com: [Customer[id=2, emailAddress=EmailAddress[value=3@3.com], firstName=Frank, lastName=Lamport], Customer[id=1, emailAddress=EmailAddress[value=3@3.com], firstName=Jude, lastName=Smith]]
Find customers with firstName=Frank: [Customer[id=2, emailAddress=EmailAddress[value=3@3.com], firstName=Frank, lastName=Lamport]]
Find customers with firstName=Jude: [Customer[id=1, emailAddress=EmailAddress[value=3@3.com], firstName=Jude, lastName=Smith], Customer[id=3, emailAddress=EmailAddress[value=5@5.com], firstName=Jude, lastName=Simmons]]
```


## Shut down and Cleanup
Run the following commands to shut down and cleanup:
1. In the Client Terminal window
    1. `Ctrl+C` to kill the running client app
2. In the Server Terminal window (assuming `gfsh` is still active):
    1. Run `shutdown --include-locators`
    2. Exit from the `gfsh` by running `quit`
    3. Delete the created directories