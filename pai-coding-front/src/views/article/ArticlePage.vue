<!-- ArticlePage.vue -->
<template>
  <a-layout class="article-container">
    <!-- 主内容区 -->
    <a-row type="flex" justify="center">
      <a-col :xs="24" :sm="22" :md="18" :lg="16">
        <!-- 文章内容组件 -->
        <article-content :article="article" />
      </a-col>
    </a-row>
    <a-divider />
    <a-row type="flex" justify="center">
      <comment-list :comments="comments" :articleId="article.articleId"></comment-list>
    </a-row>
  </a-layout>
</template>
<script>
import { useRoute, useRouter } from 'vue-router'
import ArticleContent from '@/components/article/ArticleContent.vue'
import CommentList from '@/components/article/CommentList.vue'
import { doGet } from '@/http/BackendRequests.js'
import { ARTICLE_URL } from '@/http/URL'
import { message } from 'ant-design-vue'
export default {
  components: {
    ArticleContent,
    CommentList
  },
  data() {
    return {
      article: {},
      comments: [],
      loading: true
    }
  },
  mounted() {
    const route = useRoute()
    // const router = useRouter()
    doGet(ARTICLE_URL, {
      articleId: route.params.articleId
    })
      .then((response) => {
        console.log(response)
        this.article = response.data.result
        this.comments = response.data.result.comments
        this.loading = false
      })
      .catch((error) => {
        console.log(error)
        message.warning('查看文章不存在!😢😢')
        // router.push('/')
      })
  }
}
</script>
<style>
/* 全局文字排版 */
.article-container {
  font-family: 'Georgia', serif;
  line-height: 1.6;
  background-color: white;
}
</style>
