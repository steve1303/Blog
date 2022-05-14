package com.kuang.controller.admin;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuang.dao.UserMapper;
import com.kuang.pojo.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * 后台管理页面 crud
 * ok
 */
// shiro验证权限
// 仅有管理员权限才可以访问
@RequiresPermissions("user:admin")
@Controller
public class UserController {


    @Autowired
    UserMapper userMapper;


    /**
     * 查询全部用户信息
     */
    //分页查询数据
    @GetMapping("/users")
    public String users(Model model,
                        @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                        @RequestParam(required = false, defaultValue = "5", value = "pageSize") Integer pageSize) {

        //为了程序的严谨性，判断非空：
        if (pageNum == null) {
            //设置默认当前页
            pageNum = 1;
        }
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null) {
            //设置默认每页显示的数据数
            pageSize = 5;
        }

        System.out.println("当前页是：" + pageNum + "显示条数是：" + pageSize);

        //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
        PageHelper.startPage(pageNum, pageSize);
        //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用PageHelper.startPage
        try {
            // 查询所有数据
            List<User> userList = userMapper.getAllUsers();
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo<User> pageInfo = new PageInfo<User>(userList, pageSize);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo", pageInfo);
        } finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的html前端页面
        return "user/list";
    }


    /**
     * 后台用户搜索框
     */
    @GetMapping("/query")
    public String query(@RequestParam("searchParam") String searchParam,
                        Model model) {

        List<User> userList = userMapper.getAllUsersIF(searchParam);
        System.out.println(userList);
        model.addAttribute("userList", userList);

        return "user/query";
    }

    /**
     * 去添加页面
     */
    @GetMapping("/user")
    public String toAddpage() {
        return "user/add";
    }


    /**
     * 添加用户
     */
    @PostMapping("/user")
    public String addUser(User user) {
        userMapper.addUser(user);
        // 重定向到全部列表页面
        return "redirect:/users";
    }





    /**
     * 去添加页面
     */
    @GetMapping("/toAddPhoto")
    public String toAddPhoto() {
        return "user/photo";
    }

    /**
     * 上传文件
     * 
     * */
    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("userPhoto") MultipartFile userPhoto) throws IOException {
        // 1. 获取要上传文件的文件名
        String fileName = userPhoto.getOriginalFilename();
        System.out.println(fileName);
        // 2. 自定义上传路径
        //String path = "D:\\JavaProjects\\blog\\src\\main\\resources\\static\\images\\users";
        String path = "";
        // 3. 判断路径是否存在，不存在则新建
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        // 3.改名,避免重名,定义新文件的名字
        String newName = path + File.separator +
                System.currentTimeMillis() +
                fileName.substring(fileName.lastIndexOf("."));

        // 保存文件 try
        try {
            userPhoto.transferTo(new File(newName));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        User user = new User();
        user.setUserPhoto(newName);

        userMapper.addUser(user);
        // 重定向到全部列表页面
        return "success";
    }


    /**
     * 去修改页面
     */
    @GetMapping("/user/{id}")
    public String toUpdatePage(@PathVariable("id") Integer id, Model model) {
        User user = userMapper.getUserById(id);
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * 修改用户
     */
    @PostMapping("/updateUser")
    public String updateUser(User user) {
        System.out.println(user);
        userMapper.updateUser(user);
        System.out.println(user);
        return "redirect:/users";
    }


    /**
     * 删除用户
     */
    @GetMapping("/deluser/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userMapper.deleteUserById(id);
        return "redirect:/users";
    }

}
