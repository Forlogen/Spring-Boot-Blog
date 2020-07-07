package dyliang.service;

import dyliang.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    User checkUser(String username, String password);

    List<User> findAll();
}
