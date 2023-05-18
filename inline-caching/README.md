# Inline Cache with GemFire

Inline caching involves caching query results or frequently accessed data directly within the application code 
or within a caching layer, such as a memory cache like GemFire. Instead of repeatedly querying the database 
for the same data, the application checks the cache first. If the data is found in the cache, it can be 
retrieved quickly without the need for a database round-trip.

Using an inline cache with a database can help improve performance, reduce database scaling limits, and provide greater flexibility in terms of data retrieval. 
It is an important tool for any application that relies heavily on database access, and can help ensure that critical data is always available when it is needed.

GemFire's inline cache is a mechanism for temporarily storing frequently accessed data in memory, rather than querying a database every time the data is needed. 
This can significantly improve the performance of database-driven applications, especially when dealing with large amounts of data or complex queries.

One of the main reasons to use GemFire with a database is to address scaling limits of the database itself. 
As the size and complexity of a database grows, it can become increasingly difficult to maintain fast and efficient access to the data. 
This is especially true in situations where multiple users or applications are simultaneously accessing the database, leading to contention and slowdowns.

By using GemFire's inline cache, frequently accessed data can be stored in GemFire's memory, reducing the number of queries that need to be made 
to the database. This can help alleviate some of the scaling limits of the database, as it reduces the amount of load placed on the database server. 
It can also help reduce network latency and improve overall system performance, as data can be retrieved locally rather than over a network connection.

## Install and Configure GemFire

The first step is to install and configure GemFire on your system.
Download and install VMware GemFire from [Tanzu Network](https://network.tanzu.vmware.com/). Follow the installation instructions in the [GemFire documentation](https://docs.vmware.com/en/VMware-GemFire/10.0/gf/getting_started-installation-install_intro.html).

## Clone the Spring for GemFire Examples repository from GitHub.

There is a working code examples for how to setup an inline cache here.
```text
$ git clone git@github.com:gemfire/spring-for-gemfire-examples.git
```

## Configure Access to Pivotal Commercial Maven Repository

The quickstart tutorial requires access to the VMware Commercial Maven Repository for the GemFire product jars. Please sign-up for access to repo at <https://commercial-repo.pivotal.io/register>.

Create a `gradle.properties` file in the root folder of this project (inline-caching) with the following information:

```plain
pivotalCommercialMavenRepoUsername=<your commercial maven repository username>
pivotalCommercialMavenRepoPassword=<your commercial maven repository password>
```

These properties will be used in the `web` and `cache-loader` gradle projects. 

We can now build our example using a terminal in the root directory of the inline-caching project using 
```bash
./gradlew build
```

## Database setup

### Install Postgres
You can use any database you want that has a JDBC driver for it. For this example, we will be using Postgres. 
You can either download Postgres from https://www.postgresql.org/download/ and install it manually or if on a Mac, use brew install.

```bash
brew install postgresql
brew services start postgresql
```

### Configure Postgres

Now that Postgres is running, we will want to create some tables and a user. One of the easier ways to do this
is to use the test database that Postgres provides for benchmarking via `pgbench`.

```bash
pgbench -i -s 50 postgres
```

This will create a few tables for us that we can manipulate. We will also need a user that our GemFire client can use to access the database

```bash
psql postgres
create user myuser with encrypted password 'mypass';
grant all privileges on database postgres to myuser;
grant all privileges on all tables in schema public to myuser;
```

## Configure GemFire

Once GemFire is installed, you need to configure GemFire to use Postgres as a data source. 
This involves starting a GemFire cluster with all of the necessary class files as well as creating regions to store cached data and the data source to be used.

The `cache-loader` project contains an implementation of `AsyncEventListener` and `CacheLoader` interfaces. 
GemFire's AsyncEventListener and CacheLoader are useful features that can help improve the performance and scalability of your application by allowing you to asynchronously process write events and read data within your GemFire cluster.

The AsyncEventListener interface provides a way for your application to send write events to your database asynchronously in a batch operation, without blocking the sender. 
This can help improve the throughput and performance of your application by allowing it to process events in parallel, while the sender continues to send more write events.
This also helps keep the data in GemFire in sync with what is in the database.  

In order to simplify the SQL necessary to write data to Postgres, the [JOOQ library](https://www.jooq.org/) is used in both the `ItemAsyncEventListener` and `ItemCacheLoader` classes. 
An example of using JOOQ to update a table is:

```java
int result = create.update(table)
                    .set(filler, value)
                    .where(tid.eq(itemId))
                    .execute();
```

The table `pgbench_tellers` and column `filler` is something we get when Postgres created our benchmark database. 
It's just a place to store data for our example.

We are getting our Postgres credentials from System properties in our `ConnectionPool` class
```java
		String userName = System.getProperty("postgres.username");
		String password = System.getProperty("postgres.password");
```

These properties will be passed in later when we create our GemFire region below.


### ItemCacheLoader
GemFire's CacheLoader is used to fetch data from external systems and load it into the GemFire cache. 
Our CacheLoader implementation also uses JOOQ for connecting to Postgres and Java system properties to retrieve Postgres' username and password.

### AsyncEventListener
Gemfire's AsyncEventListener is used to listen for events to the cache, and then perform a batch update to the backing Postgres database.
HikariCP, a popular connection pooling library for Java applications, is being used here to help handle our Postgres connections. We also
are using a singleton pattern in order to load only one connection pool in our cache. 

### Create a region with an AsyncEventListener and a CacheLoader

To create a region with an AsyncEventListener and a CacheLoader using GemFire, you can use GemFire Shell (`gfsh`). 
Be sure that you have already built the example project and downloaded the necessary JDBC drivers as outlined above. 
We will be creating a GemFire region with the name `item`.
```bash
./<location of gemfire>/bin/gfsh
start locator --name 'locator'
start server --name='server' --classpath="<project root directory>/cache-loader/build/libs/*" --J="-Dpostgres.username=myuser" --J="-Dpostgres.password=mypass"
create async-event-queue --listener=io.vmware.event.ItemAsyncEventListener --id=item-writebehind-queue --batch-size=10 --batch-time-interval="20"
create region --name=item --type=PARTITION --cache-loader=io.vmware.event.ItemCacheLoader --async-event-queue-id=item-writebehind-queue
```

Notice that the jars are passed into the classpath for our JDBC Postgres driver and the jar that contains our CacheLoader and AsyncEventListener implementations.
These classes will be used by GemFire whenever we interact with our newly created `item` region. Our new region also utilizes the options `--cache-loader` and `--async-event-queue-id`. 
These point to the implementation of the AsyncEventListener and CacheLoader that were referenced earlier.

## Define the data model using Spring Data

The Spring Data model that will be used to represent data in GemFire typically involves defining a set of Java classes that represent the Spring Data model entities.
For our example, we are just going to use a simple String, but any Java object can be used. 

Here is an example for how to define a Spring Data model using GemFire's repositories that is mapped to our `item` region that we created above

```java
@Region("/item")
public interface ItemRepository extends GemfireRepository<String, String> {
}
```

Again, we are using Java Strings here for simplicity, but we can use any Java object to store data in GemFire. 

## Web Project

Our `web` project consists of:
1. Application - spring configuration for our app
2. ItemController - handles the web traffic and interacts with the Service
3. ItemRepository - defines key and value of our data model
4. ItemService - uses a Repository and can utilize business logic to adapt GemFire data into a nicer format 


The web application will retrieve a request from the user, and then ask GemFire for the data in it's `item` region. 
For read operations to the region, if the data exists in the GemFire region, the data will be retrieved without using the CacheLoader. 
If the data does **not** exist in the GemFire region, the data will be fetched via the CacheLoader interface defined above.

For write operations to the GemFire region, the data is written to the GemFire region first and then batched up to be written to Postgres using the AsyncEventListener.

## Start the Spring Boot web service with GemFire integration

In this example, we have a simple client that passes all reads and writes to the web server. 
The web server is a Spring Boot web service configured to delegate all read and write operations to a GemFire cluster using Spring Boot for VMware GemFire. 

In our web projects `application.properties` file, 
we have the following line

```plain
spring.data.gemfire.pool.locators=localhost[10334]
```
This tells the GemFire client the location of our GemFire cluster that we created above. 
The port `10334` is the default port, but any port can be used if configured to do so.

Starting the Spring Boot app with GemFire can be done from the inline-caching project root directory with

```bash
./gradlew bootRun
```

When the application starts, you should see in the console output that the GemFire client has discovered a locator.

```plain
AutoConnectionSource discovered new locators [...:10334]
```

This means that our Spring Boot web application is connected and ready to handle requests. 

## Performing Requests

**The following web requests can be made to test the Cache Loader**

Add a value to Postgres database. This will bypass GemFire but will change a value in Postgres.

    psql postgres -U myuser;
    update pgbench_tellers set filler = 'hello' where tid = 1;

Retrieve a value from the webservice. This will invoke GemFire's CacheLoader. GemFire shouldn't know of this 
data with the cooresponding key `1` and therefore will reach out to Postgres.

    curl localhost:8080/1          # should see the value hello

Change a value on the Postgres database. Again, this will bypass GemFire but will update the value in the backing database. 
This will setup a demonstration that GemFire is indeed caching values. 

    psql postgres -U myuser;
    update pgbench_tellers set filler = 'goodbye' where tid = 1;

Retrieve a value from webservice. This will still show the originally set value because the web service / GemFire was not involved. 

    curl localhost:8080/1          # should still see the value hello in the console because it is cached

Clear the cache in GemFire using gfsh

    remove --key=1 --region=item

Retrieve the value from the webservice after clearing the GemFire cache so that the CacheLoader will be invoked again. 

    curl localhost:8080/1          # should see the new value goodbye

**The following web requests can be made to test the Async Event Listener**

Write a value to the web service. This will write a value to GemFire first, and then the AsyncEventListener will pick up the write event and 
persist the value to Postgres. 

    curl -X PUT localhost:8080/1 -H 'Content-Type: application/json' -d '{"value":"potato"}'

See the value in Postgres. We can see here that the value has been written by observing the return result in the console. 

    psql postgres -U myuser;
    select filler from pgbench_tellers where tid=1;  -- should see potato

Retrieve the value from the web service. Just to be certain, we can see the new value has been updated for reads as well. 
The AsyncEventListener and CacheLoader work together. 

    curl localhost:8080/1          # should see the value potato


## Cleanup

#### Gemfire 
From GemFire Shell (`gfsh`), to stop the locator and server:

    shutdown --include-locators=true

#### Spring Boot web service
In the window that is running the `./gradlew bootRun` command, send a break interrupt (`ctrl+c`)

#### Postgres
The databases created via pg_bench can be cleaned up with:

    pgbench -i -I d postgres

And the service can be stopped with

    brew services stop postgresql