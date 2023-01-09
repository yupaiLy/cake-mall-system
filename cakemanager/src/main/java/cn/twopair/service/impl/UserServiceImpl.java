package cn.twopair.service.impl;

import cn.twopair.mapper.UserMapper;
import cn.twopair.pojo.User;
import cn.twopair.result.Code;
import cn.twopair.result.Result;
import cn.twopair.service.UserService;
import cn.twopair.util.JsonTool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @description: TODO
 * @author: 李佳骏
 * @time: 2022/8/29 22:47
 */

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    public String selectByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return JsonTool.createJsonData(Result.error("获取个人信息失败"));
        }
        return JsonTool.createJsonData(Result.ok(Code.createCode(Code.SUCCESS, "获取个人信息成功!"), user, 1));
    }

    @Override
    public List<User> selectAll() {
        List<User> users = userMapper.selectAll();
        assert (users != null && users.size() > 0);
        for (User user : users) {
            switch (user.getGender()) {
                case "1" -> user.setGender("男");
                case "0" -> user.setGender("女");
                case "-1" -> user.setGender("secret");
            }
        }
        return users;
    }


    /**
     * @Param: currentPage
     * @Param: size
     * @Description: 已弃用
     * @Return: java.lang.String
     * @Author: 李佳骏
     * @Date: 2022/12/6 22:09
     */
    @Override
    public String selectByPage(int currentPage, int size) {
        List<User> users = userMapper.selectByPage((currentPage - 1) * size, size);
        // 数据库总条数
        int tot = userMapper.selectAll().size();

        converse(users);

        return JsonTool.createJsonData(Result.ok(Code.createCode(Code.SUCCESS, "查询成功!"), users, tot));
    }

    @Override
    public String add(User user) {
        if (user.getStatus() == null) {
            user.setStatus("1");
        }
        if (userMapper.selectByUsername(user.getUsername()) == null) {
            int row = userMapper.insert(user);
            if (row > 0) {
                return JsonTool.createJsonData(Result.success("注册成功!"));
            }
            return JsonTool.createJsonData(Result.error("®注册失败"));
        }
        return JsonTool.createJsonData(Result.error("账号已存在，注册失败"));
    }

    @Override
    public String deleteByIds(List<Integer> ids) {
        int cnt = userMapper.deleteByIds(ids);
        if (cnt == ids.size()) {
            return JsonTool.createJsonData(Result.success("删除成功!"));
        }
        //回滚
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return JsonTool.createJsonData(Result.error("删除失败！请稍后再试"));

    }

    @Override
    public String updateUser(User user) {
        int cnt = userMapper.update(user);
        if (cnt > 0) {
            return JsonTool.createJsonData(Result.success("修改成功!"));
        }

        //回滚
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return JsonTool.createJsonData(Result.error("修改失败！请稍后再试"));
    }

    @Override
    public String conditionQuery(User user, int currentPage, int size) {
        List<User> users = userMapper.conditionQuery(user, (currentPage - 1) * size, size);
        // 数据库总条数
        int tot = userMapper.conditionQueryData(user).size();

        // 防止点击最后一夜再条件查询出现空情况
        if (tot < (currentPage - 1) * size) {
            users = userMapper.conditionQuery(user, 0, size);
            // 性别转换
            converse(users);
            return JsonTool.createJsonData(Result.warning(Code.createCode(Code.WARNING, "查询成功!"), users, tot));
        }
        converse(users);
        return JsonTool.createJsonData(Result.ok(Code.createCode(Code.SUCCESS, "查询成功!"), users, tot));
    }

    @Override
    public String login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null || !user.getPassword().equals(password.trim())) {
            return JsonTool.createJsonData(Result.error("用户名或密码错误!"));
        }
        if (!"1".equals(user.getStatus())) {
            return JsonTool.createJsonData(Result.error("账号状态异常，请联系管理员!"));
        }
        return JsonTool.createJsonData(Result.success("登录成功!"));
    }

    private void converse(List<User> users) {
        if (users.size() == 0) return;
        for (User user : users) {
            switch (user.getGender()) {
                case "1" -> user.setGender("男");
                case "0" -> user.setGender("女");
                case "-1" -> user.setGender("secret");
            }
            switch (user.getStatus()) {
                case "1" -> user.setStatus("正常");
                case "0" -> user.setStatus("封禁");
            }

        }
    }


}
