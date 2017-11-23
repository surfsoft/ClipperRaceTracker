/*
 * Copyright 2017 Phil Haigh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <ESP8266IotCore.h>
#include <Adafruit_NeoPixel.h>
#include <ArduinoJson.h>
#include <ESP8266HTTPClient.h>

#define STATUS_LED_OUTPUT_PIN      0
#define NEOPIXEL_OUTPUT_PIN        15
#define NUM_ROWS                   8
#define NUM_COLS                   8
#define NUMPIXELS                  NUM_ROWS * NUM_COLS
#define DISPLAY_STATE_PARAM        "p"
#define DISPLAY_NOTHING            0
#define DISPLAY_STEALTH_MODE       13

#define VESSEL_ID_PARAM                 "id"
#define VESSEL_ID_PARAM_LENGTH          4
#define VESSEL_ID_PARAM_DEFAULT         "cv26"
#define REFRESH_INTERVAL_PARAM          "refresh"
#define REFRESH_INTERVAL_PARAM_LENGTH   4
#define REFRESH_INTERVAL_PARAM_DEFAULT  "300"
#define POSITION_PARAM                  "position"
#define POSITION_PARAM_LENGTH           2
#define POSITION_PARAM_DEFAULT          "?"
#define SELECT_LIST_PARAM               "vesselList"
#define SELECT_LIST_PARAM_LENGTH        512
#define SELECT_LIST_PARAM_DEFAULT       ""

#define RACETRACKER_URL "http://clipper.surfsoftconsulting.com"

byte symbols[14][8] = {
    // ?
    { B00000000,
      B00011100,
      B00100010,
      B00000100,
      B00001000,
      B00001000,
      B00000000,
      B00001000 },
    // 1
    { B00000000,
      B00001000,
      B00011000,
      B00001000,
      B00001000,
      B00001000,
      B00001000,
      B00011100 },
    // 2
    { B00000000,
      B00111100,
      B00000010,
      B00000010,
      B00011100,
      B00100000,
      B00100000,
      B00111110 },
    // 3
    { B00000000,
      B00111100,
      B00000010,
      B00000010,
      B00001100,
      B00000010,
      B00000010,
      B00111100 },
    // 4
    { B00000000,
      B00001100,
      B00010100,
      B00100100,
      B01000100,
      B01111110,
      B00000100,
      B00000100 },
    // 5
    { B00000000,
      B00111110,
      B00100000,
      B00100000,
      B00011100,
      B00000010,
      B00000010,
      B00111100 },
    // 6
    { B00000000,
      B00011110,
      B00100000,
      B00100000,
      B00011100,
      B00100010,
      B00100010,
      B00011100 },
    // 7
    { B00000000,
      B00111110,
      B00000010,
      B00000010,
      B00000100,
      B00000100,
      B00001000,
      B00001000 },
    // 8
    { B00000000,
      B00011100,
      B00100010,
      B00100010,
      B00011100,
      B00100010,
      B00100010,
      B00011100 },
    // 9
    { B00000000,
      B00011100,
      B00100010,
      B00100010,
      B00011100,
      B00000010,
      B00000010,
      B00111100 },
    // 10
    { B00000000,
      B01000110,
      B11001001,
      B01001001,
      B01001001,
      B01001001,
      B01001001,
      B11100110 },
    // 11
    { B00000000,
      B01000100,
      B11001100,
      B01000100,
      B01000100,
      B01000100,
      B01000100,
      B11101110 },
    // 12
    { B00000000,
      B01001110,
      B11000001,
      B01000001,
      B01000110,
      B01001000,
      B01001000,
      B11101111 },
    // s
    { B00000000,
      B00011100,
      B00100010,
      B00100000,
      B00011100,
      B00000010,
      B00100010,
      B00011100 },
};

Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, NEOPIXEL_OUTPUT_PIN, NEO_GRB + NEO_KHZ800);
uint32_t offColour = pixels.Color(0, 0, 0);
uint32_t nothingColour = pixels.Color(12, 0, 0);
uint32_t digitColour = pixels.Color(0, 12, 0);
uint32_t stealthColour = pixels.Color(0, 0, 12);

unsigned long lastRefreshChangeAt = 0;

HTTPClient http;

String getVesselId() {
  return iotCore.getApplicationParam(VESSEL_ID_PARAM);
}

void updatePixels(byte symbolIndex, uint32_t onColour) {

#ifdef DISPLAY_NOT_CONNECTED

#ifdef DEBUG
  Serial.println("updating:");
  Serial.print("  updatingsymbolIndex: ");
  Serial.println(symbolIndex);
  Serial.print("  onColour: ");
  Serial.println(onColour);
#endif

#else

  for (int row = 0; row < NUM_ROWS; row++) {
    for (int col = 0; col < NUM_COLS; col++) {
      int pixelNo = (row * NUM_ROWS) + (NUM_COLS - col - 1);
      boolean on = symbols[symbolIndex][row] >> col & 1 == 1;
      pixels.setPixelColor(pixelNo, on ? onColour : offColour);
    }
  }
  pixels.show();

#endif

}

void updateDisplay() {

    String symbol = iotCore.getApplicationParam(POSITION_PARAM);
    byte symbolIndex = DISPLAY_NOTHING;
    uint32_t pixelColour = nothingColour;
    if (symbol == "s" ) {
      symbolIndex = DISPLAY_STEALTH_MODE;
      pixelColour = stealthColour;
    }
    else {
      symbolIndex = symbol.toInt();
      if (symbolIndex < 1 || symbolIndex > 12) {
        symbolIndex = DISPLAY_NOTHING;
      }
      else {
        pixelColour = digitColour;
      }
    }
    updatePixels(symbolIndex, pixelColour);

}

void handleRoot() {
  String response = "<h1>Clipper Position Tracker</h1><p>Currently tracking: " +
                    getVesselId() +
                    "</p><p>Refreshing: every " +
                    iotCore.getApplicationParam(REFRESH_INTERVAL_PARAM) +
                    "s</p><p><a href=\"/configure\">change</a></p>";
  iotCore.sendResponse(200, response);
}

boolean isRefreshDue() {
  return lastRefreshChangeAt + (iotCore.getApplicationParam(REFRESH_INTERVAL_PARAM).toInt() * 1000) < millis() && iotCore.isConnectedToWifi();
}

void handleRefresh() {

  if (iotCore.isConnectedToWifi()) {
    http.begin(String(RACETRACKER_URL) + "/position/" + getVesselId());
    int httpCode = http.GET();
    if (httpCode == HTTP_CODE_OK) {
      String jsonString = http.getString();
      char json[jsonString.length() + 1];
      jsonString.toCharArray(json, jsonString.length() + 1);
      StaticJsonBuffer<128> jsonBuffer;
      JsonObject& root = jsonBuffer.parseObject(json);
      String mode = root["mode"];
      String position;
      if (mode == "S") {
        position = mode;
      }
      else {
        position = root["position"];
      }
      iotCore.setApplicationParam(POSITION_PARAM, position);
      lastRefreshChangeAt = millis();
    }
    http.end();
  }

  updateDisplay();

}

void onProvideList(String paramName, char* json) {

  if (iotCore.isConnectedToWifi()) {
    if (paramName == VESSEL_ID_PARAM) {
      String jsonString = iotCore.getApplicationParam(SELECT_LIST_PARAM);
      if (jsonString == "") {
        http.begin(String(RACETRACKER_URL) + "/fleet/list");
        int httpCode = http.GET();
        if (httpCode == HTTP_CODE_OK) {
          jsonString = "{\"list\":" + http.getString() + "}";
          iotCore.setApplicationParam(SELECT_LIST_PARAM, jsonString);
        }
        http.end();
      }
      if (jsonString != "") {
        jsonString.toCharArray(json, jsonString.length() + 1);
      }
    }
  }

}

void onConfigurationChange() {
  updateDisplay();
}


void setup() {

#ifdef DEBUG
  Serial.begin(115200);
#endif

  iotCore.setStatusLedPin(STATUS_LED_OUTPUT_PIN);

  iotCore.addApplicationConfigParam(VESSEL_ID_PARAM, PARAM_TYPE_STRING, INPUT_TYPE_DROPDOWN, VESSEL_ID_PARAM_LENGTH, VESSEL_ID_PARAM_DEFAULT, true);
  iotCore.addApplicationConfigParam(REFRESH_INTERVAL_PARAM, PARAM_TYPE_NUMBER, INPUT_TYPE_NUMBER, REFRESH_INTERVAL_PARAM_LENGTH, REFRESH_INTERVAL_PARAM_DEFAULT, true);
  iotCore.addApplicationConfigParam(POSITION_PARAM, PARAM_TYPE_NUMBER, INPUT_TYPE_NUMBER, POSITION_PARAM_LENGTH, POSITION_PARAM_DEFAULT, false);
  iotCore.addApplicationConfigParam(SELECT_LIST_PARAM, PARAM_TYPE_STRING, INPUT_TYPE_NONE, SELECT_LIST_PARAM_LENGTH, SELECT_LIST_PARAM_DEFAULT, true);
  iotCore.setOnConfigurationChange(onConfigurationChange);

  if (iotCore.isInClientMode()) {
    iotCore.getWebserver()->on("/", handleRoot);
    iotCore.setOnProvideList(onProvideList);
  }

  iotCore.begin();

  // Force a refresh of the vessel list from the web after start-up/reset
  iotCore.setApplicationParam(SELECT_LIST_PARAM, "");

#ifndef DISPLAY_NOT_CONNECTED
  pixels.begin();
  pixels.show();
#endif

  updateDisplay();

}

void loop() {

  iotCore.handleRequest();
  delay(100);
  if (isRefreshDue()) {
    handleRefresh();
  }

}