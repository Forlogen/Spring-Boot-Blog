package dyliang.service.impl;

import dyliang.dao.UserRepository;
import dyliang.domain.User;
import dyliang.service.UserService;
import dyliang.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        System.out.println(username + " " + password);
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));

        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


}
