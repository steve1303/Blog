package com.kuang.controller.admin;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuang.dao.CategoryMapper;
import com.kuang.pojo.Category;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * 后台管理页面 crud
 * // todo 设置用户创建自己的分类  管理员全部
 */
@RequiresPermissions("user:admin")
@Controller
public class CategoryController {


    @Autowired
    CategoryMapper categoryMapper;


    /**
     * 分页查询全部标签信息
     */
    @RequiresPermissions("user:admin")
    @GetMapping("/categories")
    public String categoriesAll(Model model,
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
            List<Category> categoryList = categoryMapper.getAllCategories();


            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo<Category> pageInfo = new PageInfo<>(categoryList, pageSize);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo", pageInfo);
        } finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的html前端页面
        return "category/list";
    }

    /**
     * 去添加页面
     */
    @RequiresPermissions("user:admin")
    @GetMapping("/category")
    public String toAddpage() {
        return "category/add";
    }


    /**
     * 添加
     */
    @RequiresPermissions("user:admin")
    @PostMapping("/category")
    public String addUser(Category category) {
        categoryMapper.addCategory(category);
        return "redirect:/categories";
    }


    /**
     * 去修改页面
     */
    @RequiresPermissions("user:admin")
    @GetMapping("/category/{id}")
    public String toUpdatePage(@PathVariable("id") Integer id, Model model) {
        Category category = categoryMapper.getCategoryById(id);
        model.addAttribute("category", category);
        return "category/update";
    }

    /**
     * 修改
     */
    @RequiresPermissions("user:admin")
    @PostMapping("/updateCategory")
    public String updateUser(Category category) {
        System.out.println(category);
        categoryMapper.updateCategory(category);
        System.out.println(category);
        return "redirect:/categories";
    }


    /**
     * 删除
     */
    @RequiresPermissions("user:admin")
    @GetMapping("/delCategory/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        categoryMapper.deleteCategoryById(id);
        return "redirect:/categories";
    }


    // 个人信息查询

    /**
     * 分页查询全部标签信息
     */
    @RequiresPermissions("user:common")
    @GetMapping("/categoriesCommon")
    public String categoriesIf(Model model,
                               @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                               @RequestParam(required = false, defaultValue = "5", value = "pageSize") Integer pageSize, HttpSession session) {

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
            // 根据个人id查询博客分类
            Object userid = session.getAttribute("userid");
            List<Category> categoryList = categoryMapper.getAllCategoryByUserId((Integer) userid);

            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo<Category> pageInfo = new PageInfo<>(categoryList, pageSize);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo", pageInfo);
        } finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的html前端页面
        return "category/list";
    }


    /**
     * 去添加页面
     */
    @RequiresPermissions("user:common")
    @GetMapping("/categoryself")
    public String toAddpageIf() {
        return "category/add";
    }


    /**
     * 添加
     */
    @RequiresPermissions("user:common")
    @PostMapping("/categoryself")
    public String addUserIf(Category category) {
        categoryMapper.addCategory(category);
        return "redirect:/categoriesCommon";
    }


    /**
     * 去修改页面
     */
    @RequiresPermissions("user:common")
    @GetMapping("/categoryself/{id}")
    public String toUpdatePageIf(@PathVariable("id") Integer id, Model model) {
        Category category = categoryMapper.getCategoryById(id);
        model.addAttribute("category", category);
        return "category/update";
    }

    /**
     * 修改
     */
    @RequiresPermissions("user:common")
    @PostMapping("/updateCategoryself")
    public String updateUserIf(Category category) {
        System.out.println(category);
        categoryMapper.updateCategory(category);
        System.out.println(category);
        return "redirect:/categoriesIf";
    }


    /**
     * 删除
     */
    @RequiresPermissions("user:common")
    @GetMapping("/delCategoryself/{id}")
    public String deleteUserIf(@PathVariable("id") Integer id) {
        categoryMapper.deleteCategoryById(id);
        return "redirect:/categoriesIf";
    }
}
