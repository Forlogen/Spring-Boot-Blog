package dyliang;

import dyliang.domain.Blog;
import dyliang.domain.User;
import dyliang.service.BlogService;
import dyliang.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;

import javax.sql.DataSource;

@SpringBootTest
class DyliangApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    UserService userService;

    @Autowired
    private BlogService blogService;

    @Test
    void contextLoads(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Blog> blogs = blogService.listBlog(pageable);
        System.out.println(blogs);
    }

}
