# Function Invocation Example

## Description
In this example a GemFire client will invoke remote functions registered on a GemFire server.

## Prerequisites
In order to run the following example one requires the following to be set up:
* Java 17
* Latest VMware GemFire is installed locally
* Set up local `gradle` or `maven` environment by following Steps 1-6 and 8 from [Spring Boot For VMware GemFire Quickstart](https://docs.vmware.com/en/Spring-Boot-for-VMware-GemFire/index.html#spring-boot-for-vmware-gemfire-quick-start-0)

## Steps to run
1. Clone `Spring For GemFire Examples` project
2. Start GemFire server-side processes
    1. Open new Terminal
    2. `cd` into the `function-invocation` project directory, `spring-examples/spring-data/function-invocation`
    3. Run `gradle build` to build the jar that will contain the functions.
    4. Start `gfsh` located in the `{VMware_GemFire_installation_location}/bin` directory
    5. Launch the Locator and Server by running `start.gfsh` file located in the `function-invocation` project directory in `gfsh`. <br> e.g `run --file={pathToStartGfshFile}/start.gfsh`
    6. Confirm that the locator and server are running by running `list members` in `gfsh`, which should contain two entries.
    7. Deploy the functions and domain objects to the server by running `deploy --jar=./build/libs/function-invocation-plain.jar` in `gfsh`
    8. Confirm that the functions were registered byr running `list functions` in `gfsh`, which should contain three entries.
3. Start Client
    1. Open a new Terminal
    2. `cd` into the `function-invocation` project directory
    3. Run `./gradlew clean bootRun`


## Expected Outcome
```
All customers for emailAddresses:3@3.com,2@2.com using function invocation: 
	 [Customer{id=1, emailAddress=EmailAddress{value='2@2.com'}, firstName='John', lastName='Smith'}, Customer{id=2, emailAddress=EmailAddress{value='3@3.com'}, firstName='Frank', lastName='Lamport'}]
Running function to sum up all product prices: 1499.97
Running function to sum up all order lineItems prices for order 1: 4599.94
For order: 
	 Order{id=1, customerId=3, billingAddress=Address{street='it', city='doesn't', country='matter'}, shippingAddress=Address{street='it', city='doesn't', country='matter'}, lineItems=[LineItem{product=Product{id=3, name='Apple macBook', price=899.99, description='An Apple notebook computer', attributes={}}, amount=2}, LineItem{product=Product{id=2, name='Apple iPad', price=499.99, description='An Apple tablet device', attributes={}}, amount=2}, LineItem{product=Product{id=3, name='Apple macBook', price=899.99, description='An Apple notebook computer', attributes={}}, amount=2}]}
```


## Shut down and Cleanup
Run the following commands to shut down and cleanup:
   In the Server Terminal window (assuming `gfsh` is still active):
   1. Run `shutdown --include-locators`
   2. Exit from the `gfsh` by running `quit`
   3. Delete the created directories `example-locator` and `example-server`