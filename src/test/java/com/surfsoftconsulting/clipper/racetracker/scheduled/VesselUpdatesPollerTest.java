package com.surfsoftconsulting.clipper.racetracker.scheduled;

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

import com.surfsoftconsulting.clipper.racetracker.service.VesselService;
import com.surfsoftconsulting.clipper.racetracker.web.*;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

class VesselUpdatesPollerTest {

    private final RaceStandingsDocumentFactory raceStandingsDocumentFactory = mock(RaceStandingsDocumentFactory.class);
    private final RaceStandingsDataParser raceStandingsDataParser = mock(RaceStandingsDataParser.class);
    private final SpeedAndCourseDataParser speedAndCourseDataParser = mock(SpeedAndCourseDataParser.class);
    private final VesselService vesselService = mock(VesselService.class);

    private final VesselUpdatesPoller underTest = new VesselUpdatesPoller(raceStandingsDocumentFactory, raceStandingsDataParser, speedAndCourseDataParser, vesselService);

    @Test
    void pollForUpdates() {

        Document raceStandingsDocument = mock(Document.class);
        when(raceStandingsDocumentFactory.fromUrl("http://clipperroundtheworld.com/race/standings")).thenReturn(raceStandingsDocument);
        List<SpeedAndCourseData> speedsAndCourses = mock(List.class);
        when(speedAndCourseDataParser.parse(raceStandingsDocument)).thenReturn(speedsAndCourses);
        RaceStandingsData raceStandingsData1 = mock(RaceStandingsData.class);
        RaceStandingsData raceStandingsData2 = mock(RaceStandingsData.class);
        when(raceStandingsDataParser.parse(raceStandingsDocument)).thenReturn(asList(raceStandingsData1, raceStandingsData2));

        underTest.pollForUpdates();

        verify(vesselService).updatePosition(raceStandingsData1, speedsAndCourses);
        verify(vesselService).updatePosition(raceStandingsData2, speedsAndCourses);

    }

    @Test
    void noDocumentAvailable() {

        when(raceStandingsDocumentFactory.fromUrl("http://clipperroundtheworld.com/race/standings")).thenReturn(null);

        underTest.pollForUpdates();

        verify(vesselService, never()).updatePosition(any(), any());

    }

}