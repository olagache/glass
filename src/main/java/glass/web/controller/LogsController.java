package glass.web.controller;

import glass.log.Logs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
public class LogsController {
    @Inject
    protected Logs logs;

    @RequestMapping("/logs")
    public String history(Model model) {
        model.addAttribute("logs", logs.getLogs());

        return "logs";
    }
}
