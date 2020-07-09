package dyliang.domain;

import lombok.*;

/*
    根据条件搜索博客时可选择的条件有：
    - 标题
    - 类别：这里使用类别的主键，即type的id属性作为条件
    - 是否属于推荐文章

 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BlogQuery {

    private String title;

    private Long typeId;

    private boolean recommend;
}
