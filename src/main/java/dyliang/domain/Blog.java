package dyliang.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "t_blog")
public class Blog {

    @Getter
    @Setter
    @Id
    @GeneratedValue
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;

    @Getter
    @Setter
    private String firstPicture;

    @Getter
    @Setter
    private String flag;

    @Getter
    @Setter
    private Integer views;

    @Getter
    @Setter
    private Boolean appreciation;

    @Getter
    @Setter
    private Boolean shareStatement;

    @Getter
    @Setter
    private Boolean commentable;

    @Getter
    @Setter
    private Boolean published;

    @Getter
    @Setter
    private Boolean recommend;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    // 博客对应的多个标签，该部分数据并不写入数据库
    @Getter
    @Setter
    @Transient
    private String tagIds;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    @ManyToOne
    private Type type;

    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Tag> tags = new ArrayList<>();

    @Getter
    @Setter
    @ManyToOne
    private User user;

    @Getter
    @Setter
    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();

    public void init() {
        this.tagIds = tagsToIds(this.getTags());
    }

    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }
}
