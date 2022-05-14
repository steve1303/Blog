package com.kuang.controller.portal;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuang.dao.BlogMapper;
import com.kuang.dao.TagMapper;
import com.kuang.pojo.Blog;
import com.kuang.pojo.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 播客主页 博客搜索框
 * */

@Controller
public class IndexController {


    @Autowired
    BlogMapper blogMapper;

    @Autowired
    TagMapper tagMapper;


    /**
     * 去博客前台主页
     *
     */
    @RequestMapping({"/", "/index", "/index.html"})
    public String test(Model model,
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
            PageInfo<Blog> pageInfo = new PageInfo<Blog>(blogList, pageSize);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo", pageInfo);
        } finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }


        /*查询最新的10条博客*/
        List<Blog> blogsNew = blogMapper.getBlogsNew();
        model.addAttribute("blogsNew", blogsNew);

        /*查询最热的10条博客*/
        List<Blog> blogsHot = blogMapper.getBlogsHot();
        model.addAttribute("blogsHot", blogsHot);

        /*获得所有的标签*/
        List<Tag> allTags = tagMapper.getAllTags();
        model.addAttribute("allTags", allTags);

        /*根据标签名查询*/
        List<Blog> blogsByTagName = blogMapper.getBlogsByTagName("jpa");
        int tid = 0;
        for (Blog blog : blogsByTagName) {
            int tagId = blog.getBlogTagRelation().getTagId();
            tid = tagId;
        }
        Tag tagById = tagMapper.getTagById(tid);
        System.out.println(tid);
        System.out.println(tagById);

        model.addAttribute(tid);
        model.addAttribute("tagById", tagById);

        //5.设置返回的html前端页面
        return "index";
    }

    /**
     * 前台搜索框 多字段模糊查询
     * @RequiresGuest shiro 游客可以访问
     * */
    @PostMapping("/queryBlog")
    public String queryBlog(@RequestParam("keyword") String keyword, Model model) {

        List<Blog> allBlogsIfFore = blogMapper.getAllBlogsIfFore(keyword);
        model.addAttribute("allBlogsIfFore",allBlogsIfFore);

        return "portal/query";
    }


}
