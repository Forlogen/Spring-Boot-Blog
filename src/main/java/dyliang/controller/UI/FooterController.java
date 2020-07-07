package dyliang.controller.UI;

import dyliang.service.BlogService;
import dyliang.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FooterController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/footer/statistic")
    public String statistic(Model model){
        model.addAttribute("blogCount", blogService.countBlog());
        model.addAttribute("blogView", blogService.numberOfViews());
        model.addAttribute("blogComment", commentService.numberOfComment());

        return "_fragments :: statisticList";
    }
}
