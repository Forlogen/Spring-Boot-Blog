package dyliang.service;

import dyliang.domain.Blog;
import dyliang.domain.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    // 根据Id获取博客
    Blog getBlog(Long id);

    // 获取博客列表
    Page<Blog> listBlog(Pageable pageable);

    // 获取推荐博客列表
    List<Blog> listRecommendBlogTop(Integer size);

    // 根据复合查询条件获取博客列表，实现分页查询
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    // 全局搜索
    Page<Blog> listBlog(String query,Pageable pageable);


    Page<Blog> listBlog(Long tagId,Pageable pageable);


    Map<String,List<Blog>> archiveBlog();

    Long countBlog();


    // 保存博客
    Blog saveBlog(Blog blog);

    // 更新博客
    Blog updateBlog(Long id,Blog blog);

    // 删除博客
    void deleteBlog(Long id);

    // 博客内容格式转换  MakeDown -> HTML
    Blog getAndConvert(Long id);

    Long numberOfViews();
}
