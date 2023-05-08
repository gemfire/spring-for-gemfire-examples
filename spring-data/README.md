Spring Data For GemFire Examples
=========================================================

This project provides a number of examples to get you started using Spring Data for GemFire.

Examples:

* **eviction** - In this example GemFire is configured to remove data from memory when certain thresholds are reached.
* **expiration** - In this example GemFire is configured to delete entries after a certain idle period or after a Time-To-Live period.
* **function-invocation** - In this example the server will have 3 functions registered. The client will invoke each of the functions.
* **queries** - In this example a client will query the data in various ways using OQL and continuous queries.
* **transactions** - In this example the client will perform operations within a transaction. First, it will do a successful transaction where entries are saved to the server, and then a failed transaction where all changes are reverted.

# Running The Examples

Each example will require a server be started using Gfsh and consist of a client application that will be run against the server. 
The clients are configured to connect on default ports on localhost.