<template>
  <div class="container">
    <a-layout>
      <a-layout-header class="headerStyle">
        <!-- title -->
        <a-typography-title class="article-title">{{ article.title }}</a-typography-title>
        <!-- author -->
        <div class="author">
          <a-avatar :size="50" :src="article.authorAvatar">
            <template #icon><UserOutlined /></template>
          </a-avatar>
          <div style="display: flex; gap: 30px">
            <span
              >{{ article.authorName }} <a href="#" style="color: cornflowerblue"> Follow</a></span
            >
            <p style="all: unset">靓仔发布于 {{ article.publishTime }}</p>
          </div>
        </div>
      </a-layout-header>
      <div style="background-color: white">
        <a-divider />
        <div class="actions">
          <div style="display: flex; gap: 10px; align-items: center">
            <a href="#" @click="toggleLike" :style="likeButtonStyle">
              <LikeOutlined style="font-size: large" />
            </a>
            <a href="#" style="line-height: 1">{{ likeCount }}</a>
          </div>
          <div style="display: flex; gap: 10px; align-items: center">
            <a href="#" style="display: flex; align-items: center">
              <MessageOutlined style="font-size: large" />
            </a>
            <a href="#" style="line-height: 1">{{ commentCount }}</a>
          </div>
        </div>
        <a-divider />
      </div>
      <a-layout-content class="contentStyle">
        <div class="content-wrapper">
          <div v-html="article.content" class="content"></div>
        </div>
      </a-layout-content>
      <a-layout-footer class="footerStyle"></a-layout-footer>
    </a-layout>
  </div>
</template>

<script>
import { UserOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue'
import '@/assets/style-article-content.css'
import { doPost, doGet } from '@/http/BackendRequests.js'
import { OPERATE_URL, ARTICLE_STATUS_URL } from '@/http/URL'
export default {
  props: ['article'],
  components: {
    UserOutlined,
    LikeOutlined,
    MessageOutlined
  },
  data() {
    return {
      isLiked: false,
      likeCount: 0,
      commentCount: 0
    }
  },
  methods: {
    toggleLike() {
      this.isLiked = !this.isLiked
      this.likeCount = this.isLiked ? this.likeCount + 1 : this.likeCount - 1
      doPost(OPERATE_URL, {
        operateUserId: null,
        userOperateType: 'article',
        operateStatus: this.isLiked ? 0 : 1,
        operateEntityId: this.$route.params.articleId,
        operateEntityAuthorId: this.article.authorId
      }).then((response) => {
        console.log('hh', response)
      })
    },
    getArticleStatus() {
      doGet(ARTICLE_STATUS_URL, {
        articleId: this.$route.params.articleId
      }).then((response) => {
        console.log('status->', response)
        this.isLiked = response.data.result.isLike
        this.likeCount = response.data.result.likeCount
        this.commentCount = response.data.result.commentCount
      })
    }
  },
  computed: {
    likeButtonStyle() {
      return {
        color: this.isLiked ? 'red' : 'black',
        cursor: 'pointer'
      }
    }
  },
  mounted() {
    this.getArticleStatus()
  }
}
</script>

<style scoped>
.headerStyle {
  background-color: white;
  display: flex;
  flex-direction: column;
  padding-left: 200px;
  height: auto;
}

.contentStyle {
  background-color: white;
}

.footerStyle {
  background-color: white;
}

.article-title {
  font-size: 2.5rem;
  margin-bottom: 10px;
}

.author {
  display: flex;
  align-items: center;
  gap: 10px; /* 控制头像和文本之间的间距 */
}

.author .ant-card {
  box-shadow: none !important;
  border: none !important; /* 同时去除边框 */
}
.container {
  max-width: 1100px;
  padding-top: 20px;
}
.actions {
  background-color: white;
  display: flex;
  padding-left: 200px;
  gap: 50px;
}
</style>
