package cn.twopair.controller;

import cn.twopair.pojo.User;
import cn.twopair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/users")
@ResponseBody
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * @Param: params
     * @Description: <strike>获取分页查询user数据</strike>已弃用
     * @Return: java.lang.String
     * @Author: 李佳骏
     * @Date: 2022/12/6 22:09
     */
    @PostMapping(value = "paging", produces = "application/json;charset=utf-8")
    public String selectByPage(@RequestBody HashMap<String, String> params) {

        int currentPage = Integer.parseInt(params.get("currentPage"));
        int pageSize = Integer.parseInt(params.get("pageSize"));

        return userService.selectByPage(currentPage, pageSize);
    }

    @PostMapping(produces = "application/json;charset=utf-8")
    public String signUp(@RequestBody User user) {
        return userService.add(user);
    }

    @DeleteMapping(produces = "application/json;charset=utf-8")
    public String deleteUser(@RequestBody HashMap<String, List<Integer>> params) {
        List<Integer> ids = params.get("idList");
        return userService.deleteByIds(ids);
    }

    @PutMapping(produces = "application/json;charset=utf-8")
    public String modifyUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PostMapping(value = "/choose", produces = "application/json;charset=utf-8")
    public String conditionQuery(@RequestBody HashMap<String, String> params) {
        int currentPage = Integer.parseInt(params.get("currentPage"));
        int pageSize = Integer.parseInt(params.get("pageSize"));
        User user = new User();
        user.setUsername(params.get("username"));
        user.setName(params.get("name"));
        user.setGender(params.get("gender"));
        user.setStatus(params.get("status"));
        return userService.conditionQuery(user, currentPage, pageSize);
    }

    @PostMapping(value = "/doLogin", produces = "application/json;charset=utf-8")
    public String doLogin(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getPassword());
    }

    @GetMapping(value = "/getInfo", produces = "application/json;charset=utf-8")
    public String getUserInfoByUsername(@RequestParam String username) {
        return userService.selectByUsername(username);
    }

}
