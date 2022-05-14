package com.kuang.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuang.dao.CommentMapper;
import com.kuang.pojo.Comment;
import org.apache.shiro.authz.annotation.Logical;
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
 * // todo 设置用户读取自己的评论 管理员读取全部
 */
@RequiresPermissions(value = {"user:admin", "user:common"}, logical = Logical.OR)
@Controller
public class CommentController {


    @Autowired
    CommentMapper commentMapper;


    /**
     * 分页查询全部标签信息
     */
    @GetMapping("/comments")
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
            List<Comment> commentList = commentMapper.getAllComments();
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo<Comment> pageInfo = new PageInfo<Comment>(commentList, pageSize);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo", pageInfo);
        } finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的html前端页面
        return "comment/list";
    }


    /**
     * 去添加页面
     */
    @GetMapping("/comment")
    public String toAddpage() {

        return "comment/add";
    }


    /**
     * 添加
     */
    @PostMapping("/comment")
    public String addUser(Comment comment) {

        commentMapper.addComment(comment);

        return "redirect:/comments";
    }


    /**
     * 去修改页面
     */
    @GetMapping("/comment/{id}")
    public String toUpdatePage(@PathVariable("id") Integer id, Model model) {

        Comment comment = commentMapper.getCommentById(id);
        model.addAttribute("comment", comment);

        return "comment/update";
    }

    /**
     * 修改
     */
    @PostMapping("/updateComment")
    public String updateUser(Comment comment) {

        commentMapper.updateComment(comment);

        return "redirect:/comments";
    }


    /**
     * 删除
     */
    @GetMapping("/delComment/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {

        commentMapper.deleteCommentById(id);

        return "redirect:/comments";


    }
}
