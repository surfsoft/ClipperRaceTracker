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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

@Component
public class RaceStandingsDocumentFactory {

    private final Logger LOGGER = LoggerFactory.getLogger(RaceStandingsDocumentFactory.class);

    private static final String URL_IS_EMPTY = "URL is empty";

    public Document fromUrl(String url) {

        if (StringUtils.isBlank(url)) {
            LOGGER.error(URL_IS_EMPTY);
            throw new RuntimeException(URL_IS_EMPTY);
        }

        try {
            return Jsoup.parse(IOUtils.toString(new URL(url), Charset.defaultCharset()));
        } catch (MalformedURLException e) {
            String message = String.format("URL '%s' is malformed", url);
            LOGGER.error(message);
            throw new RuntimeException(message, e);
        }catch (IOException e) {
            LOGGER.warn("Unable to retrieve page at {}", url);
            return null;
        }
    }

}
