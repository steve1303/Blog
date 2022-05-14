package com.kuang.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuang.dao.*;
import com.kuang.pojo.Blog;
import com.kuang.pojo.BlogTagRelation;
import com.kuang.pojo.Category;
import com.kuang.pojo.Tag;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


/**
 * 后台管理页面 crud
 * // todo 设置用户创建自己的博客  管理员全部
 */
@RequiresPermissions(value = {"user:admin", "user:common"}, logical = Logical.OR)
@Controller
public class BlogController {

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    BlogTagRelationMapper blogTagRelationMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommentMapper CommentMapper;


    /**
     * 分页查询全部标签信息
     */
    @GetMapping("/blogs")
    public String blogs(Model model,
                        @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                        @RequestParam(required = false, defaultValue = "10", value = "pageSize") Integer pageSize) {

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
            List<Blog> blogList = blogMapper.getAllBlogs();
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo<Blog> pageInfo = new PageInfo<>(blogList, pageSize);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo", pageInfo);
        } finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的html前端页面
        return "blog/list";
    }


    /**
     * 去添加页面
     */
    @GetMapping("/blog")
    public String toAddpage(Model model, HttpSession session) {

        // 根据用户id查询博客分类
        Object userid = session.getAttribute("userid");
        List<Category> allCategories = categoryMapper.getAllCategoryByUserId((Integer) userid);
        model.addAttribute("allCategories", allCategories);

        /*查出所有标签*/
        List<Tag> allTags = tagMapper.getAllTags();
        model.addAttribute("allTags", allTags);

        return "blog/add";
    }


    /**
     * 添加 博客  同时写入 中间表信息
     */
    @PostMapping("/blog")
    public String addBlog(@RequestParam("blogTags") String[] blogTags, Blog blog) {
        //调用底层方法保存员工信息
        blogMapper.addBlog(blog);

        // 新增博客的同时获取博客id
        Integer blogId = blog.getBlogId();
        System.out.println("=====blogId=====" + blogId);
        // 添加中间表信息
        BlogTagRelation bt = new BlogTagRelation();
        for (String blogTagId : blogTags) {
            bt.setBlogId(blogId);
            /* String转化为int类型 */
            bt.setTagId(Integer.parseInt(blogTagId));
            blogTagRelationMapper.addBlogTag(bt);

        }

        return "redirect:/blogs";
    }


    /**
     * 去修改页面
     */
    @GetMapping("/blog/{id}")
    public String toUpdatePage(@PathVariable("id") Integer id, Model model) {
        Blog blog = blogMapper.getBlogById(id);
        model.addAttribute("blog", blog);

        /*查出所有分类*/
        List<Category> allCategories = categoryMapper.getAllCategories();
        model.addAttribute("allCategories", allCategories);

        /*查出所有标签*/
        List<Tag> allTags = tagMapper.getAllTags();
        model.addAttribute("allTags", allTags);

        /*查出所有的标签id*/
        List<BlogTagRelation> blogTagById = blogTagRelationMapper.getBlogTagById(id);
        ArrayList<Integer> tagList = new ArrayList<>();
        for (BlogTagRelation blogTagRelation : blogTagById) {
            tagList.add(blogTagRelation.getTagId());
            System.out.println(blogTagRelation);
        }

        System.out.println(tagList);
        model.addAttribute("tagList", tagList);


        return "blog/update";
    }

    /**
     * 修改
     */
    @PostMapping("/updateBlog")
    public String updateBlog(@RequestParam("blogTags") String[] blogTags, @RequestParam("blogId") Integer blogId, Blog blog) {
        blogMapper.updateBlog(blog);

        System.out.println("========================");
        System.out.println(blogId);
        System.out.println("========================");

        /*删除所有的标签*/
        blogTagRelationMapper.deleteBlogTagById(blogId);

        // 添加中间表信息
        BlogTagRelation bt = new BlogTagRelation();
        for (String blogTagId : blogTags) {
            bt.setBlogId(blogId);
            bt.setTagId(Integer.parseInt(blogTagId));
            blogTagRelationMapper.addBlogTag(bt);
        }

        return "redirect:/blogs";
    }


    /**
     * 移动至回收站  修改文章状态
     */
    @GetMapping("/removeBlog/{id}")
    public String removeBlogById(@PathVariable("id") Integer id) {

        blogMapper.removeBlogById(id);

        return "redirect:/blogs";
    }

    /**
     * 彻底删除
     */
    @GetMapping("/delBlog/{id}")
    public String deleteBlog(@PathVariable("id") Integer id) {
        blogMapper.deleteBlogById(id);
        return "redirect:/blogs";
    }


}
