[TOC]

------

**博客首页**

![博客首页主体](https://typora-forlogen.oss-cn-shenzhen.aliyuncs.com/img/20200709083429.png)

___

> 1. [SpringBoot - 个人博客 - 前期准备](https://blog.csdn.net/Forlogen/article/details/107162281)
> 2. [Spring Boot - 个人博客 - 前端页面](https://blog.csdn.net/Forlogen/article/details/107168368)
> 3. [Spring Boot - 个人博客 - 登录](https://blog.csdn.net/Forlogen/article/details/107176080)
> 4. [Spring Boot - 个人博客 - 博客管理](https://blog.csdn.net/Forlogen/article/details/107182945)
> 5. [Spring Boot - 个人博客 - 分类/标签管理](https://blog.csdn.net/Forlogen/article/details/107199013)

### 1. 需求分析

在前面的几篇博文中对于博客系统搭建的前期准备，以及管理后台相关功能的实现已经全部介绍完毕。通过管理后台博主可以进行博客、类别和标签的增删改查，当然后期改进中还可以增加其他的功能。本文将开始另一个篇章的介绍，即面向访客一端的相关部分的实现。

首先介绍博客首页部分，它最终整体的效果如上图所示，从页面布局可以看出，整体的功能可以分为如下四个部分：

- 博客列表：展示当前数据库中所有的博客，同样使用分页查询实现上一页和下一页的切换功能。在列表中的具体一篇博客中需显示的信息有：

  - 博客的描述，即Blog的description字段的内容
  - 博主的头像和名字
  - 更新时间
  - 访问次数
  - 首图

  这些信息可以通过访问表中具体的字段的数据获得，最后在前端动态显示即可。另外在列表的头部显示当前数据库中博客的总量。

- 分类列表：展示最多6个当前的分类，更多分类点击more跳转到博客分类页查看

- 标签列表：展示最多10个标签，更多分类点击more跳转到博客标签页查看

- 最新推荐：展示最多8篇Blog字段recommend字段为true的博客

当然上述内容中涉及展示数量的部分可在表现层相应的方法中自行设置。

### 2. 前端处理

#### 2.1 博客列表

博客列表部分的前端设计如下所示：

```html
<div class="ui stackable grid">
    <!--左边博客列表-->
    <div class="eleven wide column">
        <!--header-->
        <div class="ui top attached segment">
            <div class="ui middle aligned two column grid">
                <div class="column">
                    <h3 class="ui teal header">博客</h3>
                </div>
                <div class="right aligned column">
                    共 <h2 class="ui orange header m-inline-block m-text-thin" th:text="${page.totalElements}">
                    14 </h2> 篇
                </div>
            </div>
        </div>
        <!--博客内容部分-->
        <div class="ui attached  segment">
            <div class="ui padded vertical segment m-padded-tb-large" th:each="blog : ${page.content}">
                <div class="ui mobile reversed stackable grid">
                    <div class="eleven wide column">
                        <h3 class="ui header"><a href="#" th:href="@{/blog/{id}(id=${blog.id})}" target="_blank"
                                                 class="m-black" th:text="${blog.title}">标题</a></h3>
                        <p class="m-text" th:text="|${blog.description}......|">
                            博客描述</p>
                        <div class="ui grid">
                            <div class="eleven wide column">
                                <div class="ui mini horizontal link list">
                                    <div class="item">
                                        <img src="https://unsplash.it/100/100?image=1005"
                                             th:src="@{${blog.user.avatar}}" alt="" class="ui avatar image">
                                        <div class="content"><a href="#" class="header"
                                                                th:text="${blog.user.nickname}">Forlogen</a>
                                        </div>
                                    </div>
                                    <div class="item">
                                        <i class="calendar icon"></i><span
                                                                           th:text="${#dates.format(blog.updateTime,'yyyy-MM-dd')}">2017-10-01</span>
                                    </div>
                                    <div class="item">
                                        <i class="eye icon"></i><span th:text="${blog.views}">2342</span>
                                    </div>
                                </div>
                            </div>
                            <div class="right aligned five wide column">
                                <a href="#" target="_blank"
                                   class="ui teal basic label m-padded-tiny m-text-thin"
                                   th:text="${blog.type.name}">认知升级</a>
                            </div>
                        </div>
                    </div>
                    <div class="five wide column">
                        <a href="#" th:href="@{/blog/{id}(id=${blog.id})}" target="_blank">
                            <img src="https://unsplash.it/800/450?image=1005" th:src="@{${blog.firstPicture}}"
                                 alt="" class="ui rounded image">
                        </a>
                    </div>
                </div>
            </div>
        </div>
        <div class="ui bottom attached segment" th:if="${page.totalPages}>1">
            <div class="ui middle aligned two column grid">
                <div class="column">
                    <a href="#" th:href="@{/(page=${page.number}-1)}"  th:unless="${page.first}" class="ui mini teal basic button">上一页</a>
                </div>
                <div class="right aligned column">
                    <a href="#" th:href="@{/(page=${page.number}+1)}"  th:unless="${page.last}" class="ui mini teal basic button">下一页</a>
                </div>
            </div>
        </div>
    </div>
```

前端页面通过后端传来的page来获取和分页相关的信息，实现博客数量显示、上一页和下一页的切换功能。同时使用page.content中传递的博客相关的信息，通过提取相关字段的数据来进行动态替换和显示，包括博客的标题、描述、访问次数、类别……

#### 2.2 分类栏

分类栏的前端设计如下所示：

```html 
<!--分类-->
<div class="ui segments">
    <div class="ui secondary segment">
        <div class="ui two column grid">
            <div class="column">
                <i class="idea icon"></i>分类
            </div>
            <div class="right aligned column">
                <a href="#" th:href="@{types/-1}" target="_blank">more <i class="angle double right icon"></i></a>
            </div>
        </div>
    </div>
    <div class="ui teal segment">
        <div class="ui fluid vertical menu">
            <a href="#" class="item" th:each="type : ${types}">
                <span th:text="${type.name}">学习日志</span>
                <div class="ui teal basic left pointing label" th:text="${#arrays.length(type.blogs)}">
                    13
                </div>
            </a>
        </div>
    </div>
</div>
```

通过`th:each`遍历前端传来的types来获取保存的类别数据，最后将Type的name字段显示到菜单的每一栏中。

#### 2.3 标签栏

标签栏的前端设计如下所示：

```html 
<!--标签-->
<div class="ui segments m-margin-top-large">
    <div class="ui secondary segment">
        <div class="ui two column grid">
            <div class="column">
                <i class="tags icon"></i>标签
            </div>
            <div class="right aligned column">
                <a href="#" th:href="@{tags/-1}" target="_blank">more <i class="angle double right icon"></i></a>
            </div>
        </div>
    </div>
    <div class="ui teal segment">
        <a href="#" target="_blank" class="ui teal basic left pointing label m-margin-tb-tiny" th:each="tag : ${tags}">
            <span th:text="${tag.name}">方法论</span>
            <div class="detail" th:text="${#arrays.length(tag.blogs)}">23</div>
        </a>
    </div>
</div>
```

它和分类栏的实现是类似的，都是通过`th:each`遍历前端传来的tags来获取保存的类别数据，最后将Tag的name字段显示到segment组件中。

#### 2.4 最新推荐

最新推荐部分的前端如下所示：

```html 
<div class="ui segments m-margin-top-large">
    <div class="ui secondary segment "><i class="bookmark icon"></i>最新推荐</div>
    <div class="ui segment" th:each="blog : ${recommendBlogs}">
        <a href="#" th:href="@{/blog/{id}(id=${blog.id})}" target="_blank" class="m-black m-text-thin"
           th:text="${blog.title}">用户故事（User Story）</a>
    </div>
</div>
```

它取出recommendBlogs中所有的推荐博客，最后进行动态显示即可。

### 3. 后端处理

#### 3.1 博客列表

上面讲到，博客列表部分需要后端传递的信息主要就是分页查询结果，通过查询结果page来获取所需的各个字段的信息。因此，相应的后端处理方法实现就要调用业务层方法来实现分页查询。具体的方法实现如下所示：

```java
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }
}
```

对应的业务层listBlog方法实现为：

```java
public interface BlogService {

    // 获取博客列表
    Page<Blog> listBlog(Pageable pageable);
}
```

```java
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;


    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }
}
```

方法的实现直接交给Jpa处理，并不需要自己写分页查询的业务逻辑。通过调用`listBlog()`获取到分页查询的结果，最后使用Model对象将它交给前端使用即可。

#### 3.2 分类栏

分类栏的表现层方法通过是使用上面的`index()`，它通过调用业务层的`listTypeTop()`来获取要显示的类别信息。具体的方法实现如下所示：

```java
public interface TypeService {

    List<Type> listTypeTop(Integer size);
}
```

```java
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeRepository typeRepository;
    
	@Override
    public List<Type> listTypeTop(Integer size) {
        // 根据每个类别对应的博客数量多少降序排列
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return typeRepository.findTop(pageable);
    }
}
```

通过Sort和PageRequest来构建Pageable对象来实现分页查询。但此时`findTop()`的实现就需要我们自己在持久层来实现：

```java
public interface TypeRepository extends JpaRepository<Type, Long> {

    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);
}
```

由于是自定义的查询，所以需使用@Query注解，注解的value属性为具体的SQL语句，这里查询的是所有的类别。

#### 3.3 标签栏

标签栏的表现层方法依然使用上面的`index()`，它通过调用业务层的`listTagTop()`来获取要显示的类别信息。具体的方法实现如下所示：

```java
public interface TagService {

    List<Tag> listTagTop(Integer size);
}
```

```java
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagRepository.findTop(pageable);
    }
}
```

方法的实现逻辑和上面分类栏是一样的，它同样需要我们自己在持久层中定义方法的实现

```java
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
```

#### 3.4 最新推荐

最新推荐列表的表现层方法的相关部分为：

```java
model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
```

因此，业务层需要实现所需的`listRecommendBlogTop()`，方法的具体实现如下：

```java
public interface BlogService {

    // 获取推荐博客列表
    List<Blog> listRecommendBlogTop(Integer size);
}
```

```java
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }
}
```

这里是根据更新时间降序排列，因此推荐的都是最新的博客。持久层具体的实现为：

```java
public interface BlogRepository extends JpaRepository<Blog, Long> , JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend = true")
    List<Blog> findTop(Pageable pageable);
}
```

这里只查询那些recommend字段值为true的博客。至此博客首页主题部分的功能实现也就介绍完了，是不是很简单呢？