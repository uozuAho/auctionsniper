# Auction Sniper

Following along the example in "Growing OO software guided by tests".

# Getting started

- Install a JDK. This is tested on win10, openjdk 11 (?)
- Install maven
- Install Openfire: https://www.igniterealtime.org/downloads/index.jsp
- Set up openfire (see p. 89)
    - add users:
        - sniper, password sniper
        - auction-item-54321, password auction
        - auction-item-65432, password auction

# Running tests

- Make sure the Openfire server is running
- run `mvn test`

# To do

- e2e tests fail for unknown reason: auction items not appearing
  in window. why? tests should say.
- get rid of the smack-config error messages on each test run
- install, configure and run openfire automatically (docker??)
- check that fresh checkout can build without intellij