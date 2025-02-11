<template>
  <a-skeleton :loading="isLoading" avatar :paragraph="{ rows: 4 }" active>
    <a-card :hoverable="true" :bodyStyle="{ padding: '16px' }">
      <div class="article-card">
        <!-- 头像 -->
        <a-avatar :src="article.articleAuthor.photo" :size="56" shape="square" />
        <!-- 文字区 -->
        <div class="article-content">
          <!-- Title -->
          <a class="article-title" :href="article.url" target="_blank">
            {{ rank + 1 }}. {{ article.articleShortTitle }}</a
          >
          <p class="article-description">{{ article.articleTitle }}</p>
          <!-- Tags -->
          <div class="tags-container">
            <TagOutlined />
            <span v-for="(tag, index) in article.articleTags" :key="tag" class="article-tag">
              <a href="#">{{ tag }}</a>
              <span v-if="index < article.articleTags.length - 1" class="separator">•</span>
            </span>
          </div>
        </div>
        <!-- 点赞、评论状态 -->
        <div class="article-stats">
          <div class="stats-item">
            <LikeOutlined :style="{ fontSize: '18px' }" />
            {{ article.articleLikeCount ? article.articleLikeCount : 0 }}
          </div>
          <div class="stats-item">
            <CommentOutlined :style="{ fontSize: '18px' }" />
            {{ article.articleCommentCount ? article.articleCommentCount : 0 }}
          </div>
        </div>
      </div>
    </a-card>
  </a-skeleton>
</template>

<script>
import { LikeOutlined, CommentOutlined, TagOutlined } from '@ant-design/icons-vue'

export default {
  components: {
    LikeOutlined,
    CommentOutlined,
    TagOutlined
  },
  props: {
    article: {
      type: Object,
      required: true
    },
    rank: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      isLoading: true
    }
  }
}
</script>

<style scoped>
.article-card {
  display: flex;
  gap: 12px;
  width: 100%;
}
.article-content {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 0; /* 防止内容溢出 */
  gap: 5px;
}
.article-title {
  font-size: 18px;
  font-weight: 600;
  line-height: 1.2;
}

.ant-card:hover .article-title {
  color: coral;
}
.article-description {
  font-size: 15px;
  color: #666;
  margin-bottom: 6px;
  line-height: 1.4;
}

.tags-container {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px 8px; /* 横向间隔 */
}

.article-tag {
  display: inline-flex;
  align-items: center;
}

.article-tag:hover {
  text-decoration: underline;
}

.article-tag a {
  font-size: 13px;
}

.separator {
  color: #999;
  margin-left: 8px;
}

.article-stats {
  display: flex;
  gap: 15px;
  margin-left: auto; /* 靠右对齐 */
  padding-left: 12px;
  font-size: 12px;
  color: #666;
}

.stats-item {
  display: flex;
  align-items: center;
  gap: 6px;
  white-space: nowrap;
  font-size: 13px;
  font-weight: bold;
}

.stats-item:hover {
  color: #249ae0;
}

.stats-item svg {
  font-size: 14px;
}

/* 保持整个卡片在 hover 时背景色变化 */
.ant-card:hover {
  background: #f2f4f7;
}
</style>
