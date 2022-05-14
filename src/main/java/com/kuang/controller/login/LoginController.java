package com.kuang.controller.login;


import com.kuang.dao.*;
import com.kuang.pojo.Blog;
import com.kuang.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * LoginController
 * 登陆注册 控制器
 */
@Controller
public class LoginController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    CommentMapper commentMapper;


    /**
     * 登录
     */
    @RequestMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session) {
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登陆数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        System.out.println("==login username===" + username);
        System.out.println("==login password===" + password);

        try {
            //执行登陆的方法
            subject.login(token);
            //设置session
            session.setAttribute("loginUser", username);
            // 返回首页的同时给commoms.html 传值 传用户id 写入session
            int userid = userMapper.selectIdByName(username);
            session.setAttribute("userid", userid);

            System.out.println("=======================");
            System.out.println(userid);
            System.out.println("=======================");

            return "redirect:/dashboard";
        } catch (UnknownAccountException e) {
            // 用户名不存在
            model.addAttribute("msg", "用户名未找到");
            return "login/login";
        } catch (IncorrectCredentialsException e) {
            // 密码不存在
            model.addAttribute("msg", "密码错误");
            return "login/login";
        } catch (AuthenticationException e) {
            // 用户名或者密码错误
            model.addAttribute("msg", "用户名或密码错误！");
            return "login/login";
        }
    }


    /**
     * 仪表盘
     *
     * @RequiresAuthentication shiro 必须登录
     */
    @RequiresAuthentication
    @RequestMapping("/dashboard")
    public String dashboard(Model model) {

        /* 查询 标签总数 */
        int tagCount = tagMapper.getTagCount();
        model.addAttribute("tagCount", tagCount);

        /* 查询 分类总数 */
        int categoryCount = categoryMapper.getCategoryCount();
        model.addAttribute("categoryCount", categoryCount);

        /* 查询 文章总数 */
        int blogCount = blogMapper.getBlogCount();
        model.addAttribute("blogCount", blogCount);

        /*查询评论总数*/
        int commentCount = commentMapper.getCommentCount();
        model.addAttribute("commentCount", commentCount);

        /*查询最新的10条博客*/
        List<Blog> blogsNew = blogMapper.getBlogsNew();
        model.addAttribute("blogsNew", blogsNew);

        /*查询最热的10条博客*/
        List<Blog> blogsHot = blogMapper.getBlogsHot();
        model.addAttribute("blogsHot", blogsHot);

        return "admin/dashboard";
    }


    /**
     * 注册  register
     */
    @PostMapping("/register")
    public String register(User user,
                           @RequestParam("userPassword") String userPassword,
                           @RequestParam("repassword") String repassword,Model model) {

        if (userPassword == repassword){
            userMapper.addUser(user);
            return "login/login";
        }
        model.addAttribute("msg","密码不一致");
        return "login/register";

    }


    /**
     * 注销
     */
    @RequestMapping("/logout")
    public String logout() {
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        // 注销
        subject.logout();
        return "login/login";
    }


    /**
     * 没有权限
     */
    @RequestMapping("/noauth")
    @ResponseBody
    public String unauthorized() {

        return "未经授权禁止访问";
    }


    /**
     * 去登录页面
     */
    @RequestMapping("/toLogin")
    public String toLogin() {

        return "login/login";
    }


    /**
     * 去注册页面
     */
    @RequestMapping("/toRegister")
    public String register() {

        return "login/register";
    }


}
