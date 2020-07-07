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
@Table(name = "t_comment")
public class Comment {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String nickname;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private String avatar;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Getter
    @Setter
    @ManyToOne
    private Blog blog;

    @Getter
    @Setter
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replyComments = new ArrayList<>();

    @Getter
    @Setter
    @ManyToOne
    private Comment parentComment;

    @Getter
    @Setter
    private boolean adminComment;
}
