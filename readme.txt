Introduction:
This application is to provide an REST interface for school management of courses enrollment. The data is saved in local database.


________________________________________________________________________________________________________________________

How to run the application
java -DAPP_PATH="{APP_PATH}" school.App

________________________________________________________________________________________________________________________

Default settings:
1.host port is 80
2.APP_PATH is c:\work


________________________________________________________________________________________________________________________

Integration Test
Those *RestServiceTest is testing the API interface.


________________________________________________________________________________________________________________________

Framework and libraries.
It only uses tool libraries. It is faster and easy to understand.
Jetty server as web server
jdbi as jdbc wrapper
hsql as database source.


________________________________________________________________________________________________________________________

Design principle:
1. The application is supposed to work under high load and with low latency.
2. Low latency is achieved by following design
    a) it has local cache, it synchronized with DB at startup and is supposed to be synchronized with DB all the times.
    b) make DB operation non-blocking. the application returns to the client before confirming the db operation finished. It only update the local cache and then send the db operation into the queue and it returns back to client.

    These two things make the application more complicated

3. It doesn't support to multiple instance deployment at the moment. To support multiple application instance, just replace the local cache with distributed cache, e.g. Redis

________________________________________________________________________________________________________________________

Usage of Local in programming
In order to reduce latency under high load, most of the operation is lock free by using concurrent collection utils.
Only two cases, it needs "add enrollment" and "remove enrollment", this is to make sure the local cache is in sync with the db. If there is no lock, then it is possible to
occur situation like this:
1. "add an enrollment to local cache"
2. "remove the enrollment to local cache"
3. "remove the enrollment from db" (failed)
4. "add the enrollment to db"

This ends up that the db and local cache out of sync: db still have the enrollment but local cache doesn't have it.


________________________________________________________________________________________________________________________

Further improvement:
1. the database is internal at the moment. This is just convenient for demo purpose. Obviously it would need a standalone db provider.
2. use configuration profile.  For simplicity the application has no use of configuration file, things are either hardcoded or from environment property,
3. test coverage is far from enough. It is not tested thoroughly, many edge cases are not tested.
4. Exception exception is a bit rough at the moment, it is not taking care of different situation.
5. add more comments to the classes.