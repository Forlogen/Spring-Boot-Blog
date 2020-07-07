package dyliang.service.impl;

import dyliang.NotFoundException;
import dyliang.dao.BlogRepository;
import dyliang.domain.Blog;
import dyliang.domain.BlogQuery;
import dyliang.domain.Type;
import dyliang.service.BlogService;
import dyliang.utils.MakeDownUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;


@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    /**
     * 使用getOne方法的前提是确保该id后面的博客一定存在，这里先简单的使用
     *         后续再处理如果为空的情况，即使用QueryByExampleExecutor接口中的findOne方法
     *             <S extends T> Optional<S> findOne(Example<S> example);
     *
     * @param id
     * @return
     */
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }


    /**
     * 根据复合的查询条件实现分页查询
     *
     * @param pageable : 使用Jpa进行分页查询需传入Pageable对象
     * @param blog ：复合查询条件对象
     * @return
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        // 这里调用了JpaSpecificationExecutor中的findAll方法，方法的参数为Specification对象
        return blogRepository.findAll(new Specification<Blog>() {
            // 重写toPredicate方法，添加查询条件
            @Override
            public Predicate toPredicate(Root<Blog> root,
                                         CriteriaQuery<?> cq,
                                         CriteriaBuilder cb) {
                List<Predicate> predicates  = new ArrayList<>();
                // 如果输入了标题信息，则根据标题构建查询语句
                // 这里使用模糊查询
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null){
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                // 如果输入了类型，则获取输入类型对应的类型id，将其作为查询条件
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                // 如果点了推荐，则同样将其作为查询条件
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                // 构建复合查询条件
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }


    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    /**
     * 保存博客
     *
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        // 如果执行的是新增操作，则需设置CreateTime和UpdateTime为相同值
        // 同时设置Views值为0
        if (blog.getId() == null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else{
            // 否则，说明执行的是更新操作，只需要设置UpdateTime
            blog.setUpdateTime(new Date());
        }

        // 最后调用save方法保存博客
        return blogRepository.save(blog);
    }

    /**
     * 更新博客
     *
     * @param id
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        // 使用Optional避免空指针异常
        Optional<Blog> b = blogRepository.findById(id);
        // 如果根据id可以找到blog，则得到对应的blog对象
        Blog blog1 = b.orElse(blog);

        // 如果该id对应的blog为空，则告知博客不存在
        if (blog1 == null){
            throw new NotFoundException("该博客不存在...");
        }

        // 否则将更新内容后的博客进行复制
        BeanUtils.copyProperties(blog, blog1);
        // 设置更新时间
        blog1.setUpdateTime(new Date());
        // 调用save方法进行保存
        return blogRepository.save(blog1);
    }

    /**
     * 根据id删除博客，只需要执行deleteById方法即可
     *
     * @param id
     */
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    /**
     * 将MakeDown格式保存的内容转换为HTML形式，便于在前端显示
     * @param id
     * @return
     */
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MakeDownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Long numberOfViews() {
        return blogRepository.numberOfviews();
    }
}
