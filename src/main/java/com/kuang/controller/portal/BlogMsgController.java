package com.kuang.controller.portal;

import com.kuang.dao.BlogMapper;
import com.kuang.dao.CommentMapper;
import com.kuang.dao.UserMapper;
import com.kuang.pojo.Blog;
import com.kuang.pojo.Comment;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;


/**
 * 博客详情页
 */
@Controller
public class BlogMsgController {

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommentMapper CommentMapper;


    /**
     * 查看博客信息
     * ok 展示正常
     * 查询出了一级评论 二级回复
     */
    @GetMapping("/p/{blogId}")
    public String userMsg(@PathVariable("blogId") Integer blogId,
                          Model model,
                          HttpSession session) {

        /*根据博客id查询博客信息*/
        Blog blogMsg = blogMapper.getBlogById(blogId);
        model.addAttribute("blogMsg", blogMsg);

        /*根据博客id查询评论 展示所有一级评论*/
        List<Comment> allCommentsByBlogId = CommentMapper.getAllCommentsByBlogId(blogId);
        model.addAttribute("allCommentsByBlogId", allCommentsByBlogId);

        /*回复模块 查询所有回复*/
        List<Comment> allRepliesByBlogId = CommentMapper.getAllRepliesByBlogId(blogId);
        model.addAttribute("allRepliesByBlogId", allRepliesByBlogId);

        return "portal/blogMsg";
    }

    /**
     * 添加评论
     *
     * @RequiresAuthentication shiro 必须登录
     * ok
     */
    @RequiresAuthentication
    @PostMapping("/addComment")
    public String addComment(@RequestParam("blogId") Integer blogId,
                             @RequestParam("commentContent") String commentContent,
                             HttpSession session) {
        System.out.println("---------------------");
        System.out.println(blogId);
        System.out.println("---------------------");

        // 获取session中的id
        Integer userid = (Integer) session.getAttribute("userid");

        Comment comment = new Comment();
        Date date = new Date();

        comment.setBlogId(blogId);
        comment.setUserId(userid);
        comment.setCommentContent(commentContent);
        comment.setCommentCreateTime(date);

        /* 设置父id为0  代表一级评论 */
        comment.setParentCommentId(BigInteger.valueOf(0));

        comment.setCommentLikeCount(0);
        comment.setCommentStatus(0);
        comment.setIsDeleted(0);

        CommentMapper.addComment(comment);

        String str = "/p/" + blogId;
        // 重定向
        return "redirect:" + str;
    }

    
    /**
     * removeCommentLogic 逻辑删除评论
     * ok
     */
    @RequiresAuthentication
    @GetMapping("/removeCommentLogic/{commentId}/{blogId}")
    public String removeCommentLogic(@PathVariable("commentId") Integer commentId,
                                     @PathVariable("blogId") Integer blogId) {

        CommentMapper.removeCommentLogic(commentId);

        String str = "/p/" + blogId;
        // 重定向
        return "redirect:" + str;
    }


    /**
     * 彻底删除 根据评论id
     * ok
     *
     * @RequiresAuthentication shiro 必须登录
     */
    @RequiresAuthentication
    @GetMapping("/delBlogComment/{commentId}/{blogId}")
    public String delBlogComment(@PathVariable("commentId") Integer commentId,
                                 @PathVariable("blogId") Integer blogId) {

        CommentMapper.deleteCommentById(commentId);

        String str = "/p/" + blogId;
        // 重定向
        return "redirect:" + str;
    }


    /**
     * 添加回复
     *
     * @RequiresAuthentication shiro 必须登录
     */
    @RequiresAuthentication
    @PostMapping("/addReplay")
    public String addReply(@RequestParam("blogId") Integer blogId,
                           @RequestParam("parentCommentId") BigInteger parentCommentId,
                           @RequestParam("commentContent") String commentContent,
                           HttpSession session) {
        // 获取session中的id
        Integer userid = (Integer) session.getAttribute("userid");

        Comment comment = new Comment();
        Date date = new Date();

        comment.setBlogId(blogId);
        comment.setUserId(userid);

        comment.setCommentContent(commentContent);
        comment.setCommentCreateTime(date);

        // 和添加评论的区别 在于这个字段
        comment.setParentCommentId(parentCommentId);

        comment.setCommentLikeCount(0);
        comment.setCommentStatus(0);
        comment.setIsDeleted(0);

        CommentMapper.addComment(comment);

        String str = "/p/" + blogId;
        // 重定向
        return "redirect:" + str;
    }
}
