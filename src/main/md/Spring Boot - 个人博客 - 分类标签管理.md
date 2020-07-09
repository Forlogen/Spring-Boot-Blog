[TOC]

------

**分类管理**

![分类管理](https://typora-forlogen.oss-cn-shenzhen.aliyuncs.com/img/20200708091941.png)

**标签管理**

![标签管理](https://typora-forlogen.oss-cn-shenzhen.aliyuncs.com/img/20200708091942.png)

___

> 前面已经讲述了关于博客系统后台管理中的登录验证和博客管理，下面继续介绍分类管理和标签管理。由于分类管理和标签管理几乎是一样的流程，代码也基本相似，所以这里将它们放到一起来说，不再占用其他的篇幅。

### 1. 需求分析

从上面的分类管理和标签管理图中可以看出，这两部分内容相当于博客管理部分的一个简化版本。对于分类或标签来说，相应的操作仍然是编辑、删除和新增，但是对应的状态栏只有名称这一项，这样管理的信息就少了许多。如果读过了前面博客管理部分的内容，那么理解起来几乎没有难度。

总之，分类/标签管理主要要实现如下几个方面的功能：

- 显示分类/标签列表
- 点击新增按钮跳转到新增页，此时输入框中内容为空，等待输入 
- 点击标记按钮同样跳转到新增页，不同之处在于此时输入框中显示的是当前待编辑的分类/标签
- 点击删除按钮，删除当前选择的类别/标签，并跳转回列表页

### 2. 前端处理

分类管理页设计如下所示：

```html
<div class="m-container-small m-padded-tb-big">
    <div class="ui container">
        <!--提示信息-->
        <div class="ui success message" th:unless="${#strings.isEmpty(message)}">
            <i class="close icon"></i>
            <div class="header">提示：</div>
            <p th:text="${message}">恭喜，操作成功！</p>
        </div>
        <!--分类列表区-->
        <table class="ui compact blue table">
            <thead>
                <!--表头-->
                <tr>
                    <th></th>
                    <th>名称</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr th:each="type,iterStat : ${page.content}">
                    <td th:text="${iterStat.count}">1</td>
                    <td th:text="${type.name}">刻意练习清单</td>
                    <td>
                        <a href="#" th:href="@{/admin/types/{id}/input(id=${type.id})}"
                           class="ui mini teal basic button">编辑</a>
                        <a href="#" th:href="@{/admin/types/{id}/delete(id=${type.id})}"
                           class="ui mini red basic button">删除</a>
                    </td>
                </tr>
            </tbody>
            <tfoot>
                <tr>
                    <th colspan="6">
                        <div class="ui mini pagination menu" th:if="${page.totalPages}>1">
                            <a th:href="@{/admin/types(page=${page.number}-1)}" class="  item"
                               th:unless="${page.first}">上一页</a>
                            <a th:href="@{/admin/types(page=${page.number}+1)}" class=" item"
                               th:unless="${page.last}">下一页</a>
                        </div>
                        <a href="#" th:href="@{/admin/types/input}" class="ui mini right floated teal basic button">新增</a>
                    </th>
                </tr>
            </tfoot>
        </table>
    </div>
</div>
```

标签管理页设计如下所示：

```html
<div class="m-container-small m-padded-tb-big">
    <div class="ui container">
        <div class="ui success message" th:unless="${#strings.isEmpty(message)}">
            <i class="close icon"></i>
            <div class="header">提示：</div>
            <p th:text="${message}">恭喜，操作成功！</p>
        </div>
        <table class="ui compact blue table">
            <thead>
                <tr>
                    <th></th>
                    <th>名称</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr th:each="type,iterStat : ${page.content}">
                    <td th:text="${iterStat.count}">1</td>
                    <td th:text="${type.name}">刻意练习清单</td>
                    <td>
                        <a href="#" th:href="@{/admin/tags/{id}/input(id=${type.id})}"
                           class="ui mini teal basic button">编辑</a>
                        <a href="#" th:href="@{/admin/tags/{id}/delete(id=${type.id})}"
                           class="ui mini red basic button">删除</a>
                    </td>
                </tr>
            </tbody>
            <tfoot>
                <tr>
                    <th colspan="6">
                        <div class="ui mini pagination menu" th:if="${page.totalPages}>1">
                            <a th:href="@{/admin/tags(page=${page.number}-1)}" class="  item"
                               th:unless="${page.first}">上一页</a>
                            <a th:href="@{/admin/tags(page=${page.number}+1)}" class=" item"
                               th:unless="${page.last}">下一页</a>
                        </div>
                        <a href="#" th:href="@{/admin/tags/input}" class="ui mini right floated teal basic button">新增</a>
                    </th>
                </tr>
            </tfoot>
        </table>
    </div>
</div>
```

两个页面基本上是相同的，而且前端的处理逻辑在博客管理那一篇中已经详细的讲述过了，这里就不再赘述。另外，博客新增页内容也相似，下面只给出类别新增页的设计：

```html
<div class="m-container-small m-padded-tb-big">
    <div class="ui container">
        <form action="#" method="post" th:object="${type}"
              th:action="*{id}==null ? @{/admin/types} : @{/admin/types/{id}(id=*{id})} " class="ui form">
            <input type="hidden" name="id" th:value="*{id}">
            <div class=" field">
                <div class="ui left labeled input">
                    <label class="ui teal basic label">名称</label>
                    <input type="text" name="name" placeholder="分类名称" th:value="*{name}">
                </div>
            </div>

            <div class="ui error message"></div>
            <!--/*/
            <div class="ui negative message" th:if="${#fields.hasErrors('name')}"  >
              <i class="close icon"></i>
              <div class="header">验证失败</div>
              <p th:errors="*{name}">提交信息不符合规则</p>
            </div>
             /*/-->
            <div class="ui right aligned container">
                <button type="button" class="ui button" onclick="window.history.go(-1)">返回</button>
                <button class="ui teal submit button">提交</button>
            </div>

        </form>
    </div>
</div>
```

由于新增和编辑操作共用了这个页面，因此，它会使用三元表达式来根据id的情况判断跳转到该页面的是新增操作还是编辑操作，然后再进行后续的处理。

### 3. 后端处理

#### 3.1 分类管理

由于分类管理和标签管理的相似性，这里只讲一下分类管理功能的实现，最后不加解释的直接给出标签管理的核心代码，详细代码可到github项目中下载。

首先当用户通过`localhost:8080/admin/types`来到分类管理页时，页面应该显示当前已有的类别列表，因此，表现层设计如下：

```java
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model){
        model.addAttribute("page",typeService.listType(pageable));
        return "/admin/types";
    }
}
```

后端只需要向前端通过Model对象传递类别列表即可，业务层中`listType()`实现如下所示：

```java
public interface TypeService {

     // 分页查询
    List<Type> listType(Pageable pageable);
}
```

```java
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeRepository typeRepository;
    
    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }
}
```

前端通过`th:each`标签遍历列表page.content，然后使用`${}`表达式就可以获取到标签的name。

对于新增来说，它对应的跳转链接为`/admin/types/input`，表现层对应的方法为：

```java
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    TypeService typeService;

    // 分类新增页面
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }
}
```

它会向前端传入一个Type对象，用于接收前端写入的数据，最终跳转到新增页来处理新增请求。前面讲到，新增页和标记页共用同一个页面，根据id来判断具体是哪一个操作。如果是编辑操作，它需要向前端传递当前id对应的类别信息，然后再跳转到页面进行编辑 ：

```java
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    TypeService typeService;

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";
    }
}
```

而编辑操作对应的后端逻辑为：

```java
@PostMapping("/types/{id}")
public String editPost(@Valid Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes) {
    // 根据前端传来的Type对象的name字段来验证是否是当前已有的类别
    Type type1 = typeService.getTypeByName(type.getName());
    if (type1 != null) {
        result.rejectValue("name","nameError","不能添加重复的分类");
    }
    // 如果输入不合法，重新跳转到输入页
    if (result.hasErrors()) {
        return "admin/types-input";
    }
    // 否则更新数据库中给定id的类别信息
    Type t = typeService.updateType(id,type);
    // 根据更新操作的结果给出提示信息
    if (t == null ) {
        attributes.addFlashAttribute("message", "更新失败");
    } else {
        attributes.addFlashAttribute("message", "更新成功");
    }
    
    // 最后重定向回类别管理页，显示最新的类别列表
    return "redirect:/admin/types";
}
```

更新操作的业务层实现为：

```java
public interface TypeService {

    // 更新类别
    Type updateType(Long id, Type type);
}
```

```java
@Transactional
@Override
public Type updateType(Long id, Type type) {
    // 首先验证给定id对应的类别是否存在，如果不存在直接抛异常
    Type type1 = typeRepository.getOne(id);
    if (type1 == null){
        throw new NotFoundException("不存在该类型");
    }
    // 否则执行更新操作
    BeanUtils.copyProperties(type, type1);
    return typeRepository.save(type1);
}
```

如果是新增操作，那么它处理的就是Post请求，后续的处理逻辑和上面的是相同的。

```java
// 新增分类
@PostMapping("/types")
public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes){
    Type type1 = typeService.getTypeByName(type.getName());
    if (type1 != null){
        result.rejectValue("name", "nameError", "不能添加重复的分类");
    }
    if (result.hasErrors()){
        return "admin/types-input";
    }
    Type type2 = typeService.saveType(type);
    if (type2 == null){
        attributes.addFlashAttribute("message", "新增失败");
    } else {
        attributes.addFlashAttribute("message", "新增成功");
    }
    return "redirect:/admin/types";
}
```

最后删除操作的实现就简单了，直接根据id删除数据库中相应的记录，最后重定向回分类管理页。

```java
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    TypeService typeService;

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }
}
```

业务层方法的实现如下所示：

```java
public interface TypeService {
    
    // 删除类别
    void deleteType(Long id);
}
```

```java
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeRepository typeRepository;

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }
}
```

#### 3.2 标签管理

**表现层：**

```java
@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 3,sort = {"id"},direction = Sort.Direction.DESC)
                               Pageable pageable, Model model) {
        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }


    @PostMapping("/tags")
    public String post(@Valid Tag tag,BindingResult result, RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name","nameError","不能添加重复的标签");
        }
        if (result.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t = tagService.saveTag(tag);
        if (t == null ) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/tags";
    }


    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result,@PathVariable Long id, RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name","nameError","不能添加重复标签");
        }
        if (result.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t = tagService.updateTag(id,tag);
        if (t == null ) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }
}
```

**业务层:**

```java
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }


    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        if (t == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag,t);
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
```

