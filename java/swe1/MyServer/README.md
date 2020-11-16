#Monster Card Game Server

Before starting a program please start the database by
opening terminal from toplevel folder (where Dockerfile is) and running:

<code>
docker build -t monster-db . && docker run -d -p 5432:5432 monster-db
</code>
<br/>
<br/>
<i>If docker doesn't run check that port 5432 is available</i>

##Protocol

There is a FrontDispatcher that delegates the requests (in a new thread) to the correct controller 
based on the endpoint.

Dao layer is done using the template pattern.

Service layer provides communication between controllers and dao layer.

I tried making my classes immutable whenever possible, minimizing state and giving preference to functional programming 
because less state => less side effects => less debugging

The tests cover everything except getters and setters even when these have some minor logic to them.
Also, DAO Layer isn't tested directly (in accordance with industry standards), but the service layer is tested instead 
and since the service layer delegates everything to DAO layer, that covers both.
