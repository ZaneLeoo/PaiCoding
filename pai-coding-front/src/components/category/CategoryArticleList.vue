<template>
  <div class="custom-section">
    <a-typography-title :level="2" class="section-title">
      {{ $route.params.tagName }} Top Articles
    </a-typography-title>
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
              <img width="150" alt="logo" :src="item.articleCoverUrl" />
            </template>
            <a-list-item-meta :description="item.articleSummary">
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
</template>
<script>
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue'
import { doGet } from '@/http/BackendRequests.js'
import { BASE_URL } from '@/http/URL'
import { useRoute } from 'vue-router'
export default {
  components: {
    StarOutlined,
    MessageOutlined,
    LikeOutlined
  },
  data() {
    return {
      articles: [],
      theme: ''
    }
  },
  methods: {
    toDetail(id) {
      this.$router.push(`/article/page/${id}`)
    }
  },
  mounted() {
    const route = useRoute()
    doGet(BASE_URL + '/article/api/category', {
      tag: route.params.tagName
    }).then((response) => {
      if (response.data.status.code === 200) {
        console.log(response)
        this.articles = response.data.result
      }
    })
  }
}
</script>

<style scoped>
/* 优化后的容器样式 */
.custom-section {
  border-radius: 8px;
  background: white;
  transition: all 0.2s ease;
  padding: 26px;
  max-width: 1200px;
  margin: auto;
}

/* 悬停效果 */
.custom-section:hover {
  border-color: #b5adad;
}

/* 标题样式优化 */
.section-title {
  text-align: center;
  margin: auto;
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
