[TOC]

------

![博客管理页](D:%5Cproject%5Cwork%5CJava%5CSpringBoot%5Cimages%5C%E5%8D%9A%E5%AE%A2%E7%AE%A1%E7%90%86%E9%A1%B5.png)

___

### 1. 需求分析

首先根据设计的静态HTML页面分析下所有的需求，博客管理页如上所示。整体上来看，博客管理页分为三大部分内容：

- **切换栏**：这里指的是导航栏下的那部分，可以实现博客列表和博客新增。编辑页之间的切换。由于并不涉及其他的操作，实现页较简单
- **搜索栏**：搜索栏主要依据三方面的信息：标题、分类和是否是推荐博客，当获取到信息后，点击搜索按钮进行搜索工作，并在下面的部分展示搜索的结果
- **博客列表管理栏**：博客信息包含：标题、类别、是否推荐、状态、更新时间和相关的操作，相关操作主要包含编辑原有博文、删除和新增

经过分析可知，前端进行结果的展示，已经获取到某些输入信息；而后主要针对于搜索、编辑、删除和新增进行处理。

### 2. 前端处理

#### 2.1 切换栏

切换栏的工作就是进行页面的切换，因此`th:href`来实现页面的跳转即可。

```html
<!--博客管理页的功能切换栏-->
<div class="ui attached pointing menu">
    <div class="ui container">
        <div class="right menu">
            <a href="#" th:href="@{/admin/blogs/input}" class=" item">发布</a>
            <a href="#" th:href="@{/admin/blogs}" class="teal active item">列表</a>
        </div>
    </div>
</div>
```

所涉及的后端处理逻辑后续再讲。

#### 2.2 搜索栏

搜索栏的页面设计如下所示：

```html
<div class="ui secondary segment form">
    <input type="hidden" name="page">
    <div class="inline fields">
        <!--标题栏-->
        <div class="field">
            <input type="text" name="title" placeholder="标题">
        </div>
        <!--分类栏-->
        <div class="field">
            <div class="ui labeled action input">
                <div class="ui type selection dropdown">
                    <input type="hidden" name="typeId">
                    <i class="dropdown icon"></i>
                    <div class="default text">分类</div>
                    <div class="menu">
                        <div th:each="type : ${types}" class="item" data-value="1" th:data-value="${type.id}" th:text="${type.name}">错误日志</div>
                    </div>
                </div>
                <!--清除按钮-->
                <button id="clear-btn" class="ui compact button">clear</button>
            </div>
        </div>
        <!--推荐按钮-->
        <div class="field">
            <div class="ui checkbox">
                <input type="checkbox" id="recommend" name="recommend">
                <label for="recommend">推荐</label>
            </div>
        </div>
        <!--搜索按钮-->
        <div class="field">
            <button type="button" id="search-btn" class="ui mini teal basic button"><i class="search icon"></i>搜索
            </button>
        </div>
    </div>
</div>
```

标题部分就是一个文本输入框，因此使用`<input type="text"> `即可。分类框这里设计为一个下拉框，当点击时会在下拉框中展示当前已有的类别。推荐是一个单选框，因此使用`<input type="checkbox">`显示推荐这两个字，而单选框使用的是Sementic UI中的checkbox组件。

#### 2.3 博客列表管理栏

博客管理栏设计如下所示

```html
<div id="table-container">
    <!--使用th:fragment进行局部刷新-->
    <table th:fragment="blogList" class="ui compact teal table">
        <thead>
            <tr>
                <th></th>
                <th>标题</th>
                <th>类型</th>
                <th>推荐</th>
                <th>状态</th>
                <th>更新时间</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <!--
				使用th:each来遍历查询到的博客列表
				博客列表信息使用${}从前端传来的page对象的content字段获取
				iterStat表示状态变量
			-->
            <tr th:each="blog,iterStat : ${page.content}">
                <!--取iterStat中的count属性，即当前迭代对象的索引，从1开始-->
                <td th:text="${iterStat.count}">1</td>
                <!--使用th:text获取博客标题-->
                <td th:text="${blog.title}">刻意练习清单</td>
                <!--使用th:text获取Blog中type对象的name字段，即标签-->
                <td th:text="${blog.type.name}">认知升级</td>
                <!--使用th:text获取推荐信息，recommend为Boolean字段，使用三元表达式进行判断输出-->
                <td th:text="${blog.recommend} ? '是':'否'">是</td>
                <!--使用th:text获取发布信息，published为Boolean字段，使用三元表达式进行判断输出-->
                <td th:text="${blog.published} ? '发布':'草稿'">草稿</td>
                <!--使用th:text获取更新时间，同时使用#date.format方法进行日期的格式化-->
                <td th:text="${#dates.format(blog.updateTime,'yyyy-MM-dd')}">2017-10-02 09:45</td>
                <td>
                    <!--使用 th:href进行页面跳转-->
                    <a href="#" th:href="@{/admin/blogs/{id}/input(id=${blog.id})}"
                       class="ui mini teal basic button">编辑</a>
                    <a href="#" th:href="@{/admin/blogs/{id}/delete(id=${blog.id})}"
                       class="ui mini red basic button">删除</a>
                </td>
            </tr>
        </tbody>
        <tfoot>
            <tr>
                <th colspan="7">
                    <!--通过前面隐藏域中的page来获取分页查询结果-->
                    <div class="ui mini pagination menu" th:if="${page.totalPages}>1">
                        <a onclick="page(this)" th:attr="data-page=${page.number}-1" class="item"
                           th:unless="${page.first}">上一页</a>
                        <a onclick="page(this)" th:attr="data-page=${page.number}+1" class=" item"
                           th:unless="${page.last}">下一页</a>
                    </div>
                    <a href="#" th:href="@{/admin/blogs/input}"
                       class="ui mini right floated teal basic button">新增</a>
                </th>
            </tr>
        </tfoot>
    </table>
</div>
```

博客列表展示部分仍然是一个表单，这里使用了Sementic UI的table组件。首先在` <thead>`标签内部定义所有的标题部分，然后在`<tbody>`内部展示具体的每篇博客。编辑和删除按钮使用`th:href`来根据具体博客的id进行相应的操作。`   <tfoot>`标签部分主要进行上一页和下一页功能的实现，根据分页查询中设定的阈值来选择显示上一页和下一页这两个按钮，从而实现分页显示。

前端页面就是进行使用Thymleaf来进行动态的渲染，只要通过Thymleaf来拿从后端传过来的数据即可。

### 3. 后端处理 

由前端处理分析可知，后端处理主要处理如下的几个链接请求：

- /admin/blogs
- /admin/blogs/input
- /admin/blogs/{id}/input
- /admin/blogs/{id}/delete
- /admin/blogs/search

#### 3.1 /admin/blogs - get

首先来处理 /admin/blogs，它指向的就是博客管理页，因此处理该请求的方法应该向前端传递所需的类别列表和具体的分页查询结果

```java
@Controller
@RequestMapping("/admin")
public class BlogController {

    //博客列表页面链接
    private static final String LIST = "admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

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
}
```

根据前端页面设计的需求，`blogs()`中应向前端传递当前所有的类别和分页查询的结果。因此，应该在Type的持久层定义方法`listType()`用于获取所有的类别，在Blog的持久层定义`listBlog()`来获取分页查询的结果。另外，因为此时分页查询是根据搜索栏输入的标题、类别和是否推荐信息进行的复合查询，所以还应该定义一个封装查询条件的类BlogQuery，如下所示：

```java
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BlogQuery {
    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private Long typeId;  // 类别通过id标识

    @Getter
    @Setter
    private boolean recommend;
}
```

接着看如何实现`listType()`和`listBlog()`。

- listType：在业务层TypeService中定义方法：

  ```java
  public interface TypeService {
  
      // 获取所有的类别
      List<Type> listType();
  }
  ```

  接口的实现类只需要调用持久层TypeRepository的`findAll()`即可

  ```java
  @Service
  public class TypeServiceImpl implements TypeService {
  
      @Autowired
      TypeRepository typeRepository;
      
        @Override
      public List<Type> listType() {
          return typeRepository.findAll();
      }
  }
  ```

- listBlog：方法的参数有两个：

  - Pageable pageable：实现分页查询需传入的参数，另外使用@PageableDefault注解设置了分页查询的一些默认值，如每页最多5条记录，并更具updateTime倒序排列
  - QueryBlog blog：用于封住从前端获取的查询项，后端方法中根据该查询项进行查询

  

  首先在业务层BlogService中定义方法：

  ```java
  public interface BlogService {
      // 根据复合查询条件获取博客列表，实现分页查询
      Page<Blog> listBlog(Pageable pageable, BlogQuery blog);
  }
  ```

  接口的实现类为：

  ```java
  @Service
  public class BlogServiceImpl implements BlogService {
  
  
      @Autowired
      private BlogRepository blogRepository;
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
  }
  ```

  由于使用了复合的查询条件，对应的持久层BlogRepository还应继承`JpaSpecificationExecutor<Blog>`接口。

  ```java
  public interface BlogRepository extends JpaRepository<Blog, Long> , JpaSpecificationExecutor<Blog> {}
  ```

  经过业务层和持久层的处理，最终表现层的`blogs()`或获取到相应的结果，并通过Model对象传给前端。

#### 3.2 /admin/blogs/search

它相应的`search()`用于根据用于输入的查询条件执行查询，并刷新博客列表。它和前一部分使用`listBlog(Pageable pageable, BlogQuery blog)`不同之处在于，`search()`用于处理post请求。它通过JS动态的获取输入的信息，然后发送请求，实现搜索和刷新。

```java
@PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {

        model.addAttribute("page", blogService.listBlog(pageable, blog));
        // 这里只刷新结果部分的segment，实现区域刷新
        // 前端使用 th:fragment="blogList"指定刷新的区域
        return "admin/blogs :: blogList";
    }
```

#### 3.3 /admin/blogs/input

它相应的方法为`input()`，用于博客的新增。

```java
@GetMapping("/blogs/input")
public String input(Model model) {
    model.addAttribute("types", typeService.listType());
    model.addAttribute("tags", tagService.listTag());
    model.addAttribute("blog", new Blog());
    
    return INPUT;
}
```

博客新增页面如下所示

![博客新增页](D:%5Cproject%5Cwork%5CJava%5CSpringBoot%5Cimages%5C%E5%8D%9A%E5%AE%A2%E6%96%B0%E5%A2%9E%E9%A1%B5.png)

由于在博客保存/发布时需要选择分类和标签，因此后端需向前端传递类别列表和标签列表。此外，为了保存新增的博客，还需要传递一个Blog对象。其中`listType()`在前一部分已经实现，`listTag()`和它几乎一样，所以这里不加解释的直接给出方法实现。

```java
public interface TagService {
  
    List<Tag> listTag();
}
```

```java
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;
    
    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }
}
```

那么前端如何来处理后端传递过来tags、types和blog呢？前端实现如下所示：

```html
<div class="m-container m-padded-tb-big">
    <div class="ui container">
        <!--
			博客新增本质上也是提交一个表单
			通过th:object来获取blog
		-->
        <form id="blog-form" action="#" th:object="${blog}" th:action="@{/admin/blogs}" method="post" class="ui form">
            <!--隐藏域，用于标识博文是草稿还是已发布-->
            <input type="hidden" name="published" th:value="*{published}">
            <!--博客id-->
            <input type="hidden" name="id" th:value="*{id}">
            <div class="required field">
                <div class="ui left labeled input">
                    <div class="ui selection compact teal basic dropdown label">
                        <!--博客flag-->
                        <input type="hidden" value="原创" name="flag" th:value="*{flag}">
                        <i class="dropdown icon"></i>
                        <div class="text">原创</div>
                        <div class="menu">
                            <div class="item" data-value="原创">原创</div>
                            <div class="item" data-value="转载">转载</div>
                            <div class="item" data-value="翻译">翻译</div>
                        </div>
                    </div>
                    <!--博客标题-->
                    <input type="text" name="title" placeholder="标题" th:value="*{title}">
                </div>
            </div>
			<!--博客具体内容-->
            <div class="required field">
                <div id="md-content" style="z-index: 1 !important;">
                    <textarea placeholder="博客内容" name="content" style="display: none" th:text="*{content}"></textarea>
                </div>
            </div>

            <div class="two fields">
                <div class="required field">
                    <div class="ui left labeled action input">
                        <label class="ui compact teal basic label">分类</label>
                        <div class="ui fluid selection dropdown">
                            <!--博客类别-->
                            <input type="hidden" name="type.id" th:value="*{type}!=null ? *{type.id}">
                            <i class="dropdown icon"></i>
                            <div class="default text">分类</div>
                            <div class="menu">
                                <div th:each="type : ${types}" class="item" data-value="1" th:data-value="${type.id}"
                                     th:text="${type.name}">错误日志
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class=" field">
                    <div class="ui left labeled action input">
                        <!--博客标签-->
                        <label class="ui compact teal basic label">标签</label>
                        <div class="ui fluid selection multiple search  dropdown">
                            <input type="hidden" name="tagIds" th:value="*{tagIds}">
                            <i class="dropdown icon"></i>
                            <div class="default text">标签</div>
                            <div class="menu">
                                <div th:each="tag : ${tags}" class="item" data-value="1" th:data-value="${tag.id}"
                                     th:text="${tag.name}">java
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
			<!--博客首图地址-->
            <div class="required field">
                <div class="ui left labeled input">
                    <label class="ui teal basic label">首图</label>
                    <input type="text" name="firstPicture" th:value="*{firstPicture}" placeholder="首图引用地址">
                </div>
            </div>
			<!--博客描述信息-->
            <div class="required field">
                <textarea name="description" th:text="*{description}" placeholder="博客描述..." maxlength="200"></textarea>
            </div>
			<!--推荐、转载声明、赞赏、评论-->
            <div class="inline fields">
                <div class="field">
                    <div class="ui checkbox">
                        <input type="checkbox" id="recommend" name="recommend" checked th:checked="*{recommend}"
                               class="hidden">
                        <label for="recommend">推荐</label>
                    </div>
                </div>
                <div class="field">
                    <div class="ui checkbox">
                        <input type="checkbox" id="shareStatement" name="shareStatement" th:checked="*{shareStatement}"
                               class="hidden">
                        <label for="shareStatement">转载声明</label>
                    </div>
                </div>
                <div class="field">
                    <div class="ui checkbox">
                        <input type="checkbox" id="appreciation" name="appreciation" th:checked="*{appreciation}"
                               class="hidden">
                        <label for="appreciation">赞赏</label>
                    </div>
                </div>
                <div class="field">
                    <div class="ui checkbox">
                        <input type="checkbox" id="commentable" name="commentable" th:checked="*{commentable}"
                               class="hidden">
                        <label for="commentable">评论</label>
                    </div>
                </div>
            </div>
            <div class="ui error message"></div>
            <div class="ui right aligned container">
                <button type="button" class="ui button" onclick="window.history.go(-1)">返回</button>
                <button type="button" id="save-btn" class="ui secondary button">保存</button>
                <button type="button" id="publish-btn" class="ui teal button">发布</button>
            </div>
        </form>
    </div>
</div>
```

当点击保存或发布按钮时，程序就会发送/admin/blogs链接的post请求。

#### 3.4 /admin/blogs - post

表现层对应的处理方法如下所示：

```java
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
}
```

参数列表中使用blog来接收前端传来的blog，首先从session中获取user，设置blog的user字段。接着从前端的blog中获取type对应的id来设置type字段。对于标签来说，由于可以为一篇博客指定多个标签，所以前端传过来的是类似"1,2,3"这样的字符串，所以就需要在TagService中有相应的方法转换为tag的列表，然后用于设置blog的tags字段。

如果前端blog的id为空，那么表示此时是博客新增，否则是博客编辑。如果是新增，则调用BlogService的`save()`进行保存。如果保存成功，则重定向或之前的博客管理页。

**TypeService**

```java
public interface TypeService {

    // 根据Id获取类别
    Type getType(Long id);
}
```

**TypeService**接口实现类

```java
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeRepository typeRepository;

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.getOne(id);
    }
}
```

**tagService**

```java
public interface TagService {

    List<Tag> listTag(String ids);
}
```

接口实现类

```java
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository; 
    @Override
    public List<Tag> listTag(String ids) { 
        return tagRepository.findAllById(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
}
```

**BlogService**

```java
public interface BlogService {

    // 保存博客
    Blog saveBlog(Blog blog);
}
```

接口实现类

```java
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    
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
}
```

如果是更新，则需调用BlogService中的`updateBlog()`:

```java
public interface BlogService {

    // 更新博客
    Blog updateBlog(Long id,Blog blog);
}
```

```java
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
```

#### 3.5 /admin/blogs/{id}/input

它用于编辑已有的博客，表现层对应的方法为：

```java
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


    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        
        setTypeAndTag(model);
        // 获取要编辑的博客
        Blog blog = blogService.getBlog(id);
        // 获取该博客所有的标签对应的id
        blog.init();
   		// 此时直接传递的是根据id获取的Blog对象，而不是new Blog()
        model.addAttribute("blog",blog);
        // 最后跳转到博客新增页进行编辑，然后再保存
        return INPUT;
    }


    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }
}

```

不同于博客新增的操作之处在于，博客编辑向前端传递的是根据指定的id获取到的具体博客blog，而且还需要传入它已有的类别和标签。最终编辑部分类似于新增操作，当编辑结束后点击保存或发布按钮来实现博客的重新保存。具体的效果如下所示：

![博客编辑页](D:%5Cproject%5Cwork%5CJava%5CSpringBoot%5Cimages%5C%E5%8D%9A%E5%AE%A2%E7%BC%96%E8%BE%91%E9%A1%B5.png)

#### 3.6 /admin/blogs/{id}/delete

博客删除相对较简单，直接根据具体的id删除数据库中的记录即可，同时向前端传递消息，最后重定向回博客管理页。相应的代码实现如下：

```java
@GetMapping("/blogs/{id}/delete")
public String delete(@PathVariable Long id,RedirectAttributes attributes) {
    blogService.deleteBlog(id);
    attributes.addFlashAttribute("message", "删除成功");
    return REDIRECT_LIST;
}
```

```java
public interface BlogService {

    // 删除博客
    void deleteBlog(Long id);
}
```

```java
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
```

至此，博客管理的增、删、改、查就已经全部实现。

