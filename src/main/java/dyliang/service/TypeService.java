package dyliang.service;

import dyliang.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    // 保存类别
    Type saveType(Type type);

    // 根据Id获取类别
    Type getType(Long id);

    // 根据名字获取类别
    Type getTypeByName(String name);

    // 分页查询
    Page<Type> listType(Pageable pageable);

    // 获取所有的类别
    List<Type> listType();

    // 更新类别
    Type updateType(Long id, Type type);


    // 删除类别
    void deleteType(Long id);


    List<Type> listTypeTop(Integer size);

}
