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

package org.glass.web.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;
import org.springframework.web.servlet.view.velocity.VelocityView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * Our implementation of VelocityView that supports tools.
 *
 * @author damien bourdette
 * @version \$Revision$
 */
public class VelocityToolsView extends VelocityView {
    private static ToolContext toolContext = initVelocityToolContext();

    @Override
    protected Context createVelocityContext(Map model, HttpServletRequest request, HttpServletResponse response) {
        VelocityContext context = new VelocityContext(toolContext);
        if(model != null) {
            for(Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>)model.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
        }
        return context;
    }

    private static ToolContext initVelocityToolContext() {
        if(toolContext == null) {
            ToolManager velocityToolManager = new ToolManager();
            velocityToolManager.autoConfigure(true);
            toolContext = velocityToolManager.createContext();
        }
        return toolContext;
    }
}
