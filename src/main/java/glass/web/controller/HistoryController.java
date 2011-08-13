package glass.web.controller;

import glass.history.History;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
public class HistoryController {
    @Inject
    protected History history;

    @RequestMapping("/history")
    public String history(Model model) {
        model.addAttribute("logs", history.getLogs());

        return "history";
    }
}
