<template>
  <!-- 评论区容器 -->
  <div class="comments-container">
    <!-- 主评论输入框 -->
    <a-comment>
      <template #avatar>
        <a-avatar :src="userStore.avatar" />
      </template>
      <template #content>
        <a-form-item>
          <a-textarea v-model:value="newComment" :rows="4" placeholder="写下你的评论..." />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="addComment" :disabled="!newComment.trim()">
            发表评论
          </a-button>
        </a-form-item>
      </template>
    </a-comment>

    <!-- 评论列表 -->
    <a-list item-layout="vertical" :data-source="comments" :locale="{ emptyText: '暂无评论' }">
      <template #renderItem="{ item }">
        <comment-item
          :comment="item"
          :current-user="currentUser"
          @reply="handleReply"
          @like="handleLike"
        />
      </template>
    </a-list>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import CommentItem from './CommentItem.vue'
import { doPost } from '@/http/BackendRequests.js'
import { COMMENT_SUBMIT_URL } from '@/http/URL'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/global.js'
const userStore = useUserStore()
const props = defineProps({
  comments: {
    type: Array,
    default: () => []
  },
  articleId: {
    type: String,
    required: true
  },
  currentUser: {
    type: Object,
    required: true
  }
})
// 评完完立即更新的问题待解决
const newComment = ref('')
const addComment = () => {
  if (newComment.value.trim()) {
    console.log(props.articleId)

    doPost(COMMENT_SUBMIT_URL, {
      articleId: props.articleId,
      content: newComment.value,
      parentCommentId: null
    })
      .then((response) => {
        if (response.data.status.code === 200) {
          message.success('添加评论成功!✌️')
          newComment.value = ''
        } else if (response.data.status.code === 200500004) {
          message.error('未发布文章不可探讨!😐')
          newComment.value = ''
        }
      })
      .catch((error) => {
        message.error('未发布文章不可探讨!😐')
      })
  }
}
</script>

<style scoped>
.comments-container {
  min-width: 800px;
  margin: 0 auto;
  padding: 20px;
}
</style>
