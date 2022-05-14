package com.kuang.controller.portal;

import com.kuang.dao.UserMapper;
import com.kuang.pojo.User;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 导航栏通用功能
 * */
@Controller
public class PasswordController {


    @Autowired
    UserMapper userMapper;

    /**
     * 查看个人信息
     * @RequiresAuthentication shiro 必须登录
     */
    @RequiresAuthentication
    @GetMapping("/userMsg/{userid}")
    public String userMsg(@PathVariable("userid") Integer id, Model model) {
        User userMsg = userMapper.getUserById(id);
        model.addAttribute("userMsg", userMsg);
        return "user/userMsg";
    }

    /**
     * 去修改密码页面
     * @RequiresAuthentication shiro 必须登录
     */
    @RequiresAuthentication
    @GetMapping("/changePwd/{userid}")
    public String toUpdatePasswordPage(@PathVariable("userid") Integer id, Model model) {
        User userInfo = userMapper.getUserById(id);
        model.addAttribute("userInfo", userInfo);
        System.out.println("userInfo" + userInfo);
        return "admin/changePwd";
    }

    /**
     * 修改密码
     * @RequiresAuthentication shiro 必须登录
     * */
    @RequiresAuthentication
    @PostMapping("/updatePassword/{userid}")
    public String updatePassword(@PathVariable("userid") Integer id,
                                 @RequestParam("old_password") String old_password,
                                 @RequestParam("new_password") String new_password,
                                 @RequestParam("check_new_password") String check_new_password,
                                 Model model) {

        User userById = userMapper.getUserById(id);
        String pwd = userById.getUserPassword();


        // 业务逻辑
        if ("".equals(old_password) || "".equals(new_password) || "".equals(check_new_password)) {
            model.addAttribute("msg", "密码不能为空");
        } else if (!old_password.equals(pwd)) {
            model.addAttribute("msg", "原密码错误");
        } else if (!new_password.equals(check_new_password)) {
            model.addAttribute("msg", "修改的密码不一致");
        } else if (new_password.equals(old_password)) {
            model.addAttribute("msg", "新密码不能和旧密码相同");
        } else {
            userMapper.updateUserPassword(id, new_password);
            model.addAttribute("msg", "密码修改成功");
        }

        return "login/login";
    }
}
