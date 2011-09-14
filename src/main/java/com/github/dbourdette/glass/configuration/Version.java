/*
 * Copyright 2011 Olivier Lagache
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

package com.github.dbourdette.glass.configuration;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author olivier lagache
 */

@Component
public class Version {
    private static final String APPLICATION_VERSION_NAME = "application.version";
    private static final String COMPILATION_DATE_NAME = "compilation.date";
    private static final String COMPILATION_DATE_FORMAT = "yyyyMMdd_HHmmss";

    private static final Logger LOG = LoggerFactory.getLogger(Version.class);

    private String applicationVersion;
    private Date compilationDate;

    @PostConstruct
    public void initialize() {
        Properties properties = new Properties();
        InputStream propertyStream = getClass().getResourceAsStream("/version.txt");

        if (propertyStream == null) {
            return;
        }

        try {
            properties.load(propertyStream);

            applicationVersion = properties.getProperty(APPLICATION_VERSION_NAME);

            String compilationDateAsString = properties.getProperty(COMPILATION_DATE_NAME);
            if (StringUtils.isNotEmpty(compilationDateAsString)) {
                SimpleDateFormat sdf = new SimpleDateFormat(COMPILATION_DATE_FORMAT);
                compilationDate = sdf.parse(compilationDateAsString);
            }

            propertyStream.close();
        } catch (IOException e) {
            LOG.warn("Enable to find version.txt file !");
        } catch (ParseException e) {
            LOG.warn("Enable to parse date from version.txt file !");
        }

    }

    public Date getCompilationDate() {
        return compilationDate;
    }

    public String getApplicationVersion() {

        return applicationVersion;
    }

}
