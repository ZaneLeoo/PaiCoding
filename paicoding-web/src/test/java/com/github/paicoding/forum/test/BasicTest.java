package com.github.paicoding.forum.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.paicoding.forum.service.article.repository.entity.ArticleContentDO;
import com.github.paicoding.forum.service.article.repository.mapper.ArticleContentMapper;
import com.github.paicoding.forum.web.QuickForumApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author XuYifei
 * @date 2024-07-12
 */
@Slf4j
@SpringBootTest(classes = QuickForumApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BasicTest {

    @Autowired
    ArticleContentMapper articleContentMapper;
    @Test
    public void test() {
//        QueryWrapper<ArticleContentDO> wrapper = new QueryWrapper<ArticleContentDO>().eq("article_id", "2503946890353665");
        ArticleContentDO articleContentDO = articleContentMapper.selectById(2503946890353665L);
        System.out.println(articleContentDO);
    }
}
