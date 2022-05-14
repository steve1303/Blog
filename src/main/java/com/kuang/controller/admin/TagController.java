package com.kuang.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuang.dao.TagMapper;
import com.kuang.pojo.Tag;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 后台管理页面 crud
 * ok
 */
// shiro验证权限
// 仅有管理员权限才可以访问
@RequiresPermissions("user:admin")
@Controller
public class TagController {

    @Autowired
    TagMapper tagMapper;


    /**
     * 分页查询全部标签信息
     */
    @GetMapping("/tags")
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
            List<Tag> tagList = tagMapper.getAllTags();
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo<Tag> pageInfo = new PageInfo<Tag>(tagList, pageSize);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo", pageInfo);
        } finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的html前端页面
        return "tag/list";
    }


    /**
     * 去添加页面
     */
    @GetMapping("/tag")
    public String toAddpage() {
        return "tag/add";
    }


    /**
     * 添加标签
     */
    @PostMapping("/tag")
    public String addUser(Tag tag) {
        //调用底层方法保存员工信息
        tagMapper.addTag(tag);
        // 重定向到全部列表页面
        return "redirect:/tags";
    }


    /**
     * 去修改页面
     */
    @GetMapping("/tag/{id}")
    public String toUpdatePage(@PathVariable("id") Integer id, Model model) {
        Tag tag = tagMapper.getTagById(id);
        model.addAttribute("tag", tag);
        return "tag/update";
    }

    /**
     * 修改
     */
    @PostMapping("/updateTag")
    public String updateUser(Tag tag) {
        System.out.println(tag);
        tagMapper.updateTag(tag);
        System.out.println(tag);
        return "redirect:/tags";
    }


    /**
     * 删除
     */
    @GetMapping("/deltag/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        tagMapper.deleteTagById(id);
        return "redirect:/tags";
    }

}
