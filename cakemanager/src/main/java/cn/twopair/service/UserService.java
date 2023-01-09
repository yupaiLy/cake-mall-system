package cn.twopair.service;

import cn.twopair.pojo.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description: TODO
 * @author: 李佳骏
 * @time: 2022/8/29 20:29
 */

public interface UserService {
    String login(String username, String password);

    String selectByUsername(String username);

    List<User> selectAll();

    String selectByPage(int currentPage, int size);

    @Transactional
    String add(User user);

    @Transactional
    String deleteByIds(List<Integer> ids);

    @Transactional
    String updateUser(User user);

    String conditionQuery(User user, int currentPage, int size);
}

