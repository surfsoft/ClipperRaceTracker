package com.surfsoftconsulting.clipper.racetracker.web;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class SpeedAndCourseDataFactoryTest {

    private final String HTML =
            "<html><body>" +
                    "<div class=\"markerInfo\" style=\"position: absolute; visibility: visible; width: 170px; left: 986.403px; bottom: -366.633px; cursor: default;\">" +
                    "    <h3 class=\"\" style=\"background-color: #166fb8; color: #FFFFFF\"><span>Unicef</span></h3>" +
                    "    <div><span>SOG</span><span class=\"lc\">11.3kn</span></div>" +
                    "    <div><span>COG</span><span>112</span></div>" +
                    "    <div><span>DTF</span><span class=\"lc\">2788.67nm</span></div>" +
                    "    <div><span>Updated</span><span>42 mins ago</span>" +
                    "</div><div><img src=\"/inc/img/logo35_small.png\"></div></div>" +
                    "</body></html>";

    private final Element element = Jsoup.parse(HTML).body().getElementsByClass("markerInfo").first();

    private final SpeedAndCourseDataFactory underTest = new SpeedAndCourseDataFactory();

    @Test
    void getName() {
        assertThat(underTest.getName(element), is("Unicef"));
    }

    @Test
    void getSpeed() {
        assertThat(underTest.getSpeed(element), is(Double.parseDouble("11.3")));
    }

    @Test
    void getHeading() {
        assertThat(underTest.getHeading(element), is(Integer.parseInt("112")));
    }


}