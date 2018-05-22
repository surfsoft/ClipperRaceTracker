# Clipper Race Tracker

A Java service that scrapes data the cliiperroundtheworld.com race tracker and stores it for use

The source for the data is the web page https://clipperroundtheworld.com/race/standings - the race standings table and parts of the JavaScript (that contain speed and heading) are parsed.

Data is stored in a MongoDB collection, 'Vessel'.

The application exposes the following endpoints:
- /healthcheck (GET) - returns an empty body with http status 200
- /vessel (POST) - creates a new Vessel record, using form parameters 'id' and 'name'
- /fleet/list (GET) - returns a JSON list of vessels, each with an id and name
- /position/${id} (GET) - returns the current position of the specified vessel
- /export/${id}/${raceNo} (GET) - returns a CSV file of all the positions logged for the vessel for a particular race

## Use of this service

You will need Java 8 and MongoDB 3 installed. Build the code using gradlew (Unix/Linux/OS X) or gradlew.bat (Windows)

Run the built JAR file: `java -jar build/libs/racetracker-0.0.1-SNAPSHOT.jar`

By default the application will connect to MongoDB on port 27017 and create its collections under a schema called 'test'.
To change this, explicitly configure the database connection uri:
- spring.data.mongodb.uri (default is "mongodb://localhost/test")

If you don't want to use spring.data.mongodb.uri then set these properties instead:
- spring.data.mongodb.host (default is "localhost")
- spring.data.mongodb.database (default is "test")
- spring.data.mongodb.port (default is 27017)
- spring.data.mongodb.username (default is blank)
- spring.data.mongodb.password (default is blank)

These properties can be supplied when starting the service; they are not set in the application.properties file.

Once the application is running you will need to seed the database with the name and id of each vessel in the race.
This can be achieved via the `/vessel` endpoint, for example by using curl:

> `curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "id=cv26&name=Visit%20Seattle" localhost:8080/vessel`

The name supplied **must** match the name as written into the current race standings table (the web page shows all team names in uppercase but the team names are actually mixed case).
The bash script seed-vessels.sh will seed the correct values for the 2017 race.

## What it does

Clipper update the race viewer hourly for most of each leg, changing this to as frequent as every 15 minutes as vessels approach the end of the race.

Periodically (but more frequently than every 15 minutes) the app loads the race viewer page and scrapes the current data for all the vessels.
This ensures that updates to the race tracker aren't lost if there are intermittent connection issues or delayed position updates, regardless of the expected frequency with which the Clipper page is actually updated.

The refresh interval is configurable in `application.properties` and is currently set to five minutes.

For each vessel the service will store the supplied position details, but only if the timestamp is later than the most recent position for that vessel.
This caters for the situation where the position update for one or more vessels arrives late.

# Clipper Race Tracker Slack Plugin

The SlackController and associated classes (eg SlackService, SlackResponse etc) have been developed to support a slack plugin.

Before my instance of the slack plugin can be made available to the public I need to add SSL support to the surfsoftconsulting.com web server.

# Clipper Race Tracker Client

This is an ESP8266 connected to an AdaFruit NeoPixel 8x8 matrix.
It uses the service above to display the current race position of your selected boat.

I can, on request, supply a kit comprising a pre-flashed ESP8266 together with a NeoPixel 8x8 matrix and connecting wires.
You'll need to assemble these items, (instructions provided) and connect a USB 3 power supply (not provided).
Optionally you can add a case and/or a LiPo battery for mains-free use (the USB charger charges the LiPo when both are plugged in).

Once it is all powered up all you have to do is configure it:
- Connect it to your local WiFi
- Choose your boat on the configuration page

And that's it. The display updates regularly from the web and its configuration is preserved when turned off.