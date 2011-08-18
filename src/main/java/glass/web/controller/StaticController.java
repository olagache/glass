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

package glass.web.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
public class StaticController {
    @RequestMapping("/css/style.css")
    public void ccs(HttpServletResponse response) throws IOException {
        response.setContentType("text/css");

        serveResource("/glass/velocity/css/style.css", response);
    }

    @RequestMapping("/image/{name}.png")
    public void image(@PathVariable String name, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");

        serveResource("/glass/velocity/image/" + name + ".png", response);
    }

    @RequestMapping("/js/{name}.js")
    public void javascript(@PathVariable String name, HttpServletResponse response) throws IOException {
        response.setContentType("application/javascript");

        serveResource("/glass/velocity/js/" + name + ".js", response);
    }

    private void serveResource(String name, HttpServletResponse response) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(name);

        IOUtils.copy(inputStream, response.getOutputStream());

        inputStream.close();
    }
}
