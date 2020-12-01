#Monster Card Game Server

Before starting a program please start the database by
opening terminal from toplevel folder (where Dockerfile is) and running:

`docker build -t monster-db . && docker run -d -p 5432:5432 monster-db`

_If docker doesn't run check that port 5432 is available_

##Protocol

**Total time spent: 20 hours**<br/>
**Test coverage: 85%**

There are 3 main layers: controller, persistence (dao) and service

There is a FrontDispatcher in the controller layer that delegates the requests (in a new thread) to the correct controller 
based on the endpoint.

Dao layer is implemented with the use of the template pattern.

Service layer provides communication between controllers and dao layer, as well as contains program logic

I made my classes immutable whenever possible, minimizing state and giving preference to functional programming 
because less state => less side effects => less debugging. BattleManager, class that manages battle between 2 different 
requests, is implemented as singleton. Concurrent collections are used where necessary.

The tests cover everything except getters and setters even when these have some minor logic to them.
Also, DAO Layer isn't tested directly (in accordance with industry standards), but the service layer is tested instead 
and since the service layer delegates everything to DAO layer, that covers both.

Http and Service layers are tested with unit tests, controller is covered with integration tests.

funny issues - couple of small changes that had to be done: `user` is a reserved table in postgres so i had to use `player` 
as an entity name, and `package` is a keyword in Java so I used `bundle`