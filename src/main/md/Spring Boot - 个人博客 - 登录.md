[TOC]

------

![登录页](D:%5Cproject%5Cwork%5CJava%5CSpringBoot%5Cimages%5C%E7%99%BB%E5%BD%95%E9%A1%B5.png)

___

### 1. 需求分析

管理后台的登录的唯一目的就是去验证用户的合法性，即根据输入的用户名和密码能否在数据库中找到相应的用户。因此，登录页需要做的最重要的工作就是**验证**。验证可以分为前端和后端两个方面进行：

- **前端**：不允许不输入用户名或密码来直接登录，如果用户在这两项中有一项没有输入相应的信息，前端页面应该要给出相应的提示；如果根据输入的用户名和密码无法在数据库中找到合法的用户，那么也应该在前端页面给出相应的提示，告诉用户用户名或密码错误
- **后端**：如果用户正常的输入了用户名和密码，那么接下来就需要根据用户名和密码到数据库中进行验证，那么就要求在数据持久层应该有相应的方法。如果根据用户名和密码能找到合法的用户，那么将跳转到欢迎页，否则在前端给出错误提示

在分析了前端和后端对于用户登录的需求后，我们就可以分别对其进行处理，从而实现用户登录验证管理功能。

### 2. 前端验证

登录页较为简单，本质上就是一个form表单

```html
 <div class="m-container-small m-padded-tb-massive" style="max-width: 30em !important;">
   <div class="ur container">
     <div class="ui middle aligned center aligned grid">
       <div class="column">
         <h2 class="ui teal image header">
           <div class="content">
             管理后台登录
           </div>
         </h2>
         <form class="ui large form" method="post" th:action="@{/admin/login}">
           <div class="ui  segment">
             <!--用户名输入框-->
             <div class="field">
               <div class="ui left icon input">
                 <i class="user icon"></i>
                 <input type="text" name="username" placeholder="用户名">
               </div>
             </div>
             <!--用户名输入框-->
             <div class="field">
               <div class="ui left icon input">
                 <i class="lock icon"></i>
                 <input type="password" name="password" placeholder="密码">
               </div>
             </div>
             <button class="ui fluid large teal submit button">登   录</button>
           </div>

           <div class="ui error mini message"></div>
           <!--
				使用th:text拿到后端传递的message，进行动态替换显示
				同时使用th:unless判断只有当message不为空时才输出
			-->
           <div class="ui mini negative message" th:unless="${#strings.isEmpty(message)}" th:text="${message}">用户名或密码错误</div>
         </form>
       </div>
     </div>
   </div>
 </div>
```

对于前端来说，主要工作是进行进行表单的验证，不允许用户输入空的信息，针对的部分就是` <div class="ui  segment">`所定义的form表单。因此，可以通过JS对表单进行验证，如下所示：

```html
<script>
  $('.ui.form').form({
    fields : {
      username : {
        identifier: 'username',
        rules: [{
          type : 'empty',
          prompt: '请输入用户名'
        }]
      },
      password : {
        identifier: 'password',
        rules: [{
          type : 'empty',
          prompt: '请输入密码'
        }]
      }
    }
  });
</script>
```

首先通过类`'.ui.form'`找到form表单，然后对于其中的两个field区域，即用户名输入和密码输入定义规则：

- 如果用户名为空，则前端提示请输入用户名
- 如果密码为空，则前端提示请输入密码

整体上最后是这样的一个效果，当我不输入任何信息时，页面就会弹出信息，另外对应的框会变红，提示这是一个不合法的登录请求。

![前端登录验证](D:%5Cproject%5Cwork%5CJava%5CSpringBoot%5Cimages%5C%E5%89%8D%E7%AB%AF%E7%99%BB%E5%BD%95%E9%AA%8C%E8%AF%81.png)

如果用户名或密码错误，同样会给出提示信息。

![前端登录验证2](D:%5Cproject%5Cwork%5CJava%5CSpringBoot%5Cimages%5C%E5%89%8D%E7%AB%AF%E7%99%BB%E5%BD%95%E9%AA%8C%E8%AF%812.png)

### 3. 后端验证

首先在表现层应该有一个Controller来处理登录请求，然后使用`localhost:8080/admin`就可以返回登录页，如下所示：

```java
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String loginPage(){
        return "admin/login";
    }
}
```

程序会在resource目录及其子目录下找名为`login.html`的页面进行加载。

而在form表单中使用thymleaf定义的动作是`th:action="@{/admin/login}`，因此需要一个方法处理/admin/login为链接的请求，进行用户验证，如下所示

```java
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session, RedirectAttributes attributes){
		...
    }
}
```

首先使用@PostMapping表示该方法处理的一个post请求，然后使用@RequestParam从request域中拿到前端输入的用户名（username）和密码（password）。同时使用HttpSession在session中记录登录的用户，使用RedirectAttributes实现重定向，当用户名或密码输入错误时，仍然重定向回登录页。

那么拿到用户名和密码后，在业务层需要使用它们到数据库中找有没有相应的用户。因此，业务层需要定义`checkUser()`来进行验证。

```java
public interface UserService {
    
    User checkUser(String username, String password);
}
```

接口的实现类为：

```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        // 控制台显示信息
        System.out.println(username + " " + password);
        // 验证
        User user = userRepository.findByUsernameAndPassword(username, password);

        return user;
    }
}
```

那么在持久层就需要相应的操作方法，根据Jpa的命名规则`findByxxxAndxxx`定义相应的方法`findByUsernameAndPassword()`，只需要定义相应的方法即可，并不需要自己写处理逻辑，Jpa会自动的帮我们处理：

```java
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsernameAndPassword(String username, String password);
}
```

> 不熟悉Jpa可以查阅我之前的一篇博文：[一文详尽 Spring Data JPA 的日常使用](https://forlogen.blog.csdn.net/article/details/107151173)

再次回到业务层，当调用UserRepository中的方法查询用户后会得到一个User对象。如果可以找到，那么user不为空，否则user就等于null，表示该用户不存在。因此，最终表现层通过调用业务层的方法就能拿到一个user，其中处理post请求的方法处理逻辑如下所示：

```java
@PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session, RedirectAttributes attributes){
		// 拿到User对象
        User user = userService.checkUser(username, password);
        // 如果当前user合法
        if (user != null){
            // 那么在session中保存user，同时为了安全将password置空
            user.setPassword(null);
            session.setAttribute("user", user);
            // 跳转到欢迎页
            return "admin/index";
        } else {
            // 如果当前的user为空，说明用户名或密码错误
            // 使用attributes向前端传递提示message
            attributes.addFlashAttribute("message", "用户名或密码错误");
            // 重定向回登录页
            return "redirect:/admin";
        }
    }
```

这样后端的验证工作就处理结束了，主要就是验证用户名和密码在数据库中是否存在。另外，通常为了密码的安全，尝试用MD5进行加密，这样数据库中保存的密码其实是加密后的结果。

```java
public class MD5Utils {
    public static String code(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[]byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }
}
```

使用MD5加密后，`checkUser()`就变成了如下的形式：

```java
@Override
public User checkUser(String username, String password) {
    System.out.println(username + " " + password);
    User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
    
    return user;
}
```

即传递到数据库中的也是加密后的结果。如果一切顺利通过，那么最终会正常跳转到后台的欢迎页。

![登录欢迎页](D:%5Cproject%5Cwork%5CJava%5CSpringBoot%5Cimages%5C%E7%99%BB%E5%BD%95%E6%AC%A2%E8%BF%8E%E9%A1%B5.png)

另外，还需要一个注销功能，当用户点击右上角的注销时会退出登录，重新回到登录页。因此，导航栏的注销按钮部分就需要使用`th:href`添加一个跳转

```html
<i class="dropdown icon"></i>
<div class="menu">
    <a href="#" th:href="@{/admin/logout}" class="item">注销</a>
</div>
```

然后表现层需要有方法来处理，即将之前保存在session域中的user清除掉，最后重定向会登录页。

```java
@GetMapping("/logout")
public String logout(HttpSession session){
    session.removeAttribute("user");
    return "redirect:/admin";
}
```

### 4. 登录欢迎页

登录欢迎页只是简单的展示一个页面，并不会处理其他的请求，页面设计如下：

```html
<div class="m-container-small m-padded-tb-big">
    <div class="ui container">
        <div class="ui success large  message">
            <h3 th:text="'Hi, ' + ${session.user.nickname}+' ,欢迎回来！'">Hi, Forlogen，欢迎登录！</h3>
        </div>
        <img src="../static/images/manba.jpg" th:src="@{/images/manba.jpg}" alt=""
             class="ui rounded bordered fluid image">
    </div>
</div
```

这里需要从session域中拿到user对象的nickname字段，然后使用`th:text`进行动态的替换；而显示的图片使用`th:src`来指定图片存放的路径。



