package com.cjw.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjw.common.lang.RpcResult;
import com.cjw.entity.Blog;
import com.cjw.service.BlogService;
import com.cjw.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author cjw
 * @since 2021-08-08
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public RpcResult getList(@RequestParam(defaultValue = "1") Integer currentPage) {
        Page<Blog> page = new Page<>(currentPage, 5);
        IPage<Blog> pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return RpcResult.succ(pageData);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public RpcResult getBolgById(@PathVariable Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除！");
        return RpcResult.succ(null);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RpcResult edit(@Validated @RequestBody Blog blog) {
        Blog temp;
        if (blog.getId() != null) {
            // 编辑
            temp = blogService.getById(blog.getId());
            Assert.isTrue(temp.getUserId().equals(ShiroUtils.getUserId()), "没有权限编辑");
        } else {
            temp = new Blog(ShiroUtils.getUserId(), LocalDateTime.now(), 0);
        }
        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
        blogService.saveOrUpdate(temp);
        return RpcResult.succ(temp);
    }
}
