#Monster Card Game Server

To start Postgres in Docker - open terminal from toplevel folder (where Dockerfile is) and run:

<code>
    docker build -t monster-db . && docker run -d -p 5432:5432 monster-db
</code>

<i>If docker doesn't run check that port 5432 is available</i>