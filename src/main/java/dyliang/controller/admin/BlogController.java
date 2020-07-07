package dyliang.controller.admin;

import dyliang.domain.Blog;
import dyliang.domain.BlogQuery;
import dyliang.domain.User;
import dyliang.service.BlogService;
import dyliang.service.TagService;
import dyliang.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class BlogController {

    // 博客输入页面链接
    private static final String INPUT = "admin/blogs-input";

    //博客列表页面链接
    private static final String LIST = "admin/blogs";

    // 重定向
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    /**
     *  进入博客列表页面，同时实现分页查询
     *  使用@PageableDefault设置分页的一些属性值，如每页的记录条数、排序的方式、正排还是倒排……
     * @param pageable ：分页查询
     * @param blog ：复合查询条件
     * @param model ：返回结果给前端页面
     * @return
     */
    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {

        // 获取所有已有的博客类别
        // types : 类别结果
        model.addAttribute("types", typeService.listType());

        // 分页查询
        // page : 分页查询结果
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }

    /**
     *  博客列表页搜索栏
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {

        // 分页查询
        // page : 分页查询结果
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        // 这里只刷新结果部分的segment，实现区域刷新
        // 前端使用 th:fragment="blogList"指定刷新的区域
        return "admin/blogs :: blogList";
    }

    /**
     * 新增按钮的后端逻辑，点击新增后跳转到输入页面，同时获取所有的类别和标签，并在菜单栏显示
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    /**
     *  博客更新
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }


    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));

        // 获取创建好的博客指定的类别和标签
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId() == null) {
            b =  blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null ) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_LIST;
    }


    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }
}
