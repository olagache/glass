/*
 * Copyright 2011 Damien Bourdette
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

package com.github.dbourdette.glass.web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.github.dbourdette.glass.configuration.Configuration;
import org.quartz.Job;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

/**
 * Scans for classes which are @Job annotated.
 */
@Component
public class JobPathScanner {

    List<String> jobPaths = new ArrayList<String>();

    @Inject
    private Configuration configuration;

    public List<String> getJobsPaths() {
        return jobPaths;
    }

    @PostConstruct
    protected void scanPaths() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(Job.class));

        for (BeanDefinition definition : provider.findCandidateComponents(configuration.getJobBasePackage())) {
            jobPaths.add(definition.getBeanClassName());
        }

        Collections.sort(jobPaths);
    }
}
