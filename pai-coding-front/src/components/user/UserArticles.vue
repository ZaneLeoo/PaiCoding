<template>
  <div class="container">
    <div class="custom-section">
      <a-typography-title :level="2" class="section-title"> Published Articles </a-typography-title>
      <div class="top-article-list">
        <a-list item-layout="vertical" size="large" :data-source="articles">
          <template #renderItem="{ item }">
            <a-list-item key="item.articleId" @click="toDetail(item.articleId)">
              <template #actions>
                <p class="action-item"><LikeOutlined />{{ item.articleLikeCount }}</p>
                <p class="action-item">
                  <MessageOutlined />
                  {{ item.articleCommentCount }}
                </p>
              </template>
              <template #extra>
                <div style="margin-right: -110px">
                  <img width="100" alt="logo" :src="item.articleCoverUrl" />
                </div>
              </template>
              <a-list-item-meta>
                <template #title>
                  <a :href="item.href">{{ item.articleTitle }}</a>
                </template>
                <template #avatar><a-avatar :src="item.articleAuthor.photo" :size="45" /></template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </div>
    </div>
  </div>
</template>
<script>
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue'
import { doGet } from '@/http/BackendRequests.js'
import { BASE_URL } from '@/http/URL'
export default {
  components: {
    StarOutlined,
    MessageOutlined,
    LikeOutlined
  },
  data() {
    return {
      articles: []
    }
  },
  methods: {
    toDetail(articleId) {
      this.$router.push(`/article/page/${articleId}`)
    }
  },
  mounted() {
    doGet(BASE_URL + '/article/api/top').then((response) => {
      if (response.data.status.code === 200) {
        console.log(response)
        this.articles = response.data.result
      }
    })
  }
}
</script>

<style scoped>
.container {
  display: flex;
  background: white;
  border-radius: 1.5rem;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  padding: 2rem;
  max-width: 800px;
  max-height: 1010px;
  gap: 15px;
  overflow: auto; /* 仅在垂直方向上显示滚动条 */
  transition: transform 0.3s ease;
  margin-top: 30px;
}
/* 优化后的容器样式 */
.custom-section {
  border-radius: 8px;
  background: white;
  transition: all 0.2s ease;
}
.profile-card:hover {
  transform: translateY(-5px);
}
/* 悬停效果 */
.custom-section:hover {
  transform: translateY(-5px);
  border-color: #b5adad;
}

/* 标题样式优化 */
.section-title {
  text-align: center;
  padding: 20px 24px 16px;
  color: rgba(0, 0, 0, 0.85);
}

/* 列表容器调整 */
.top-article-list {
  border-radius: 0 0 8px 8px;
}

/* 穿透修改子组件样式 */
:deep(.ant-card) {
  border: none !important;
  border-radius: 15px !important;
  box-shadow: none !important;
}
.action-item {
  display: flex;
  gap: 5px;
  align-items: center;
}
</style>
