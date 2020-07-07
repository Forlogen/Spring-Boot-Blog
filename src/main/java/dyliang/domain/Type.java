package dyliang.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "t_type")
public class Type {

    @Getter
    @Setter
    @Id
    @GeneratedValue
    private Long id;

    @Getter
    @Setter
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @Getter
    @Setter
    @OneToMany(mappedBy = "type")
    private List<Blog> blogs = new ArrayList<>();
}
