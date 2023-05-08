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
Customer for ID before (transaction commit success) = Customer[id=2, emailAddress=EmailAddress[value=2@2.com], firstName=Franky, lastName=Hamilton]
Customer for ID after (transaction commit success) = Customer[id=2, emailAddress=EmailAddress[value=2@2.com], firstName=Humpty, lastName=Hamilton]
Customer for ID after (transaction commit failure) = Customer[id=2, emailAddress=EmailAddress[value=2@2.com], firstName=Humpty, lastName=Hamilton]
java.lang.IllegalArgumentException: This is an expected exception that should fail the transactions
	at dev.gemfire.client.transactions.service.CustomerService.updateCustomersFailure(CustomerService.java:71)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:343)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:196)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:750)
	at org.springframework.transaction.interceptor.TransactionInterceptor$1.proceedWithInvocation(TransactionInterceptor.java:123)
	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:391)
	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:119)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:750)
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:702)
	at dev.gemfire.client.transactions.service.CustomerService$$SpringCGLIB$$0.updateCustomersFailure(<generated>)
	at dev.gemfire.client.transactions.TransactionalClient.lambda$runner$0(TransactionalClient.java:42)
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:760)
	at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:750)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:317)
	at dev.gemfire.client.transactions.TransactionalClient.main(TransactionalClient.java:24)
Customer for ID after 2 updates with delay = Customer[id=2, emailAddress=EmailAddress[value=2@2.com], firstName=Frumpy, lastName=Hamilton]
```


## Shut down and Cleanup
Run the following commands to shut down and cleanup:
1. In the Client Terminal window
    1. `Ctrl+C` to kill the running client app
2. In the Server Terminal window (assuming `gfsh` is still active):
    1. Run `shutdown --include-locators`
    2. Exit from the `gfsh` by running `quit`
    3. Delete the created directories
