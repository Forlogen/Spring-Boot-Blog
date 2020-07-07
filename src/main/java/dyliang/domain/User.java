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
@Table(name = "t_user")
public class User {

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
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String avatar;

    @Getter
    @Setter
    private Integer type;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Getter
    @Setter
    @OneToMany(mappedBy = "user")
    private List<Blog> blogs = new ArrayList<>();

}
