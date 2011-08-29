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

package com.github.dbourdette.glass.velocity;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.dbourdette.glass.SpringConfig;

/**
 * @author damien bourdette
 */
public class VelocityTest {

    private VelocityEngine velocityEngine;

    @Before
    public void init() throws IOException {
        velocityEngine = new SpringConfig().velocityConfig().getVelocityEngine();
    }

    @Test
    public void merge() {
        Context context = new VelocityContext();
        context.put("message", "hi");

        Assert.assertEquals("hi", merge("/com/github/dbourdette/glass/velocity/velocity-test.vm", context));
    }

    @Test
    public void equals() {
        Context context = new VelocityContext();
        context.put("value", new Dummy("hi"));
        context.put("message", "hi");

        Assert.assertEquals(" hi ", merge("/com/github/dbourdette/glass/velocity/velocity-test-equals.vm", context));
    }

    private String merge(String template, Context context) {
        Template velocityTemplate = velocityEngine.getTemplate(template);

        StringWriter writer = new StringWriter();

        velocityTemplate.merge(context, writer);

        return writer.toString();
    }

    public static class Dummy {
        private String message;

        private Dummy(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
