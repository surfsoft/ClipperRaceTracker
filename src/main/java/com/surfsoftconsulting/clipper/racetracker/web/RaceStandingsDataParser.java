package com.surfsoftconsulting.clipper.racetracker.web;

/*
 * Copyright 2019 Phil Haigh
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

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RaceStandingsDataParser {

    private final RaceStandingsDataFactory raceStandingsDataFactory;

    public RaceStandingsDataParser(RaceStandingsDataFactory raceStandingsDataFactory) {
        this.raceStandingsDataFactory = raceStandingsDataFactory;
    }

    public List<RaceStandingsData> parse(Document raceStandingsPage) {

        List<RaceStandingsData> raceStandingsData = new ArrayList<>();

        // Note: the first row is the table headings, remaining rows are team standings
        Elements standingsRows = raceStandingsPage.body().getElementById("currentstandings").getElementsByTag("tr");
        for (int index = 1; index < standingsRows.size(); index++) {
            raceStandingsData.add(raceStandingsDataFactory.fromTableRow(standingsRows.get(index)));
        }

        return raceStandingsData;

    }

}
