<template>
  <a-comment
    :class="['comment-item', { 'nested-comment': depth > 0 }]"
    :style="{ marginLeft: `${depth * 40}px` }"
  >
    <!-- 作者信息 -->
    <template #avatar>
      <a-avatar :src="comment.authorAvatar" />
    </template>
    <template #author>
      <span>{{ comment.authorName }}</span>
    </template>
    <!-- 内容区 -->
    <!-- <template #datetime>
      <a-tooltip :title="comment.time">
        <span>{{ formatTime(comment.time) }}</span>
      </a-tooltip>
    </template> -->
    <template #content>
      <div class="comment-content">
        <p>{{ comment.content }}</p>
        <!-- 操作按钮 -->
        <div class="actions">
          <a-button type="text" @click="toggleLike" :class="{ liked: comment.isLiked }">
            <template #icon>
              <LikeOutlined />
            </template>
            {{ comment.likes || '点赞' }}
          </a-button>
          <a-button type="text" @click="toggleReply"> 回复 </a-button>
        </div>

        <!-- 回复输入框 -->
        <div v-if="showReply" class="reply-box">
          <a-textarea
            v-model:value="replyText"
            :rows="2"
            placeholder="写下你的回复..."
            :auto-size="{ minRows: 2, maxRows: 5 }"
            class="custom-textarea"
          />
          <div class="reply-actions">
            <a-button
              type="primary"
              size="small"
              @click="submitReply"
              :disabled="!replyText.trim()"
            >
              提交
            </a-button>
            <a-button size="small" @click="cancelReply"> 取消 </a-button>
          </div>
        </div>

        <!-- 子评论 -->
        <div v-if="comment.replies?.length" class="replies">
          <comment-item
            v-for="reply in comment.replies"
            :key="reply.id"
            :comment="reply"
            :current-user="currentUser"
            :depth="depth + 1"
            @reply="handleChildReply"
            @like="$emit('like', $event)"
          />
        </div>
      </div>
    </template>
  </a-comment>
</template>

<script setup>
import { ref } from 'vue'
import { LikeOutlined } from '@ant-design/icons-vue'
import dayjs from 'dayjs'
import { doPost } from '@/http/BackendRequests.js'
import { COMMENT_SUBMIT_URL } from '@/http/URL'
import { message } from 'ant-design-vue'
import { useRoute } from 'vue-router'
const route = useRoute()
const props = defineProps({
  comment: Object,
  currentUser: Object,
  depth: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['reply', 'like'])

const showReply = ref(false)
const replyText = ref('')

const formatTime = (time) => dayjs(time).format('YYYY-MM-DD HH:mm')

const toggleLike = () => {
  emit('like', {
    commentId: props.comment.id,
    isLike: !props.comment.isLiked
  })
}

const toggleReply = () => {
  showReply.value = !showReply.value
  if (!showReply.value) replyText.value = ''
}

const submitReply = () => {
  if (replyText.value.trim()) {
    console.log(route.params.articleId)
    doPost(COMMENT_SUBMIT_URL, {
      articleId: route.params.articleId,
      content: replyText.value,
      parentCommentId: props.comment.id
    }).then((response) => {
      message.success('添加评论成功!✌️')
      replyText.value = ''
    })
  }
}

const cancelReply = () => {
  showReply.value = false
  replyText.value = ''
}

const handleChildReply = (payload) => {
  emit('reply', {
    ...payload,
    parentId: props.comment.id
  })
}
</script>

<style scoped>
.comment-item {
  transition: background-color 0.3s;
}

.comment-item:hover {
  background-color: #fafafa;
}

.nested-comment {
  border-left: 2px solid #eee;
  padding-left: 12px;
}

.actions {
  margin-top: 8px;
}

.actions :deep(.ant-btn) {
  padding: 0 8px;
}

.liked {
  color: #1890ff;
}

.reply-box {
  margin-top: 12px;
}

.reply-actions {
  margin-top: 8px;
  display: flex;
  gap: 8px;
}

.replies {
  margin-top: 16px;
}

/* 美化输入框 */
.custom-textarea {
  border-radius: 8px; /* 圆角 */
  border: 1px solid #d9d9d9; /* 边框颜色 */
  padding: 10px; /* 内边距 */
  transition: all 0.3s ease; /* 平滑过渡 */
}

.custom-textarea:focus {
  border-color: #1890ff; /* 聚焦时的边框颜色 */
  box-shadow: 0 0 5px rgba(24, 144, 255, 0.3); /* 聚焦时的阴影效果 */
}

.custom-textarea::placeholder {
  color: #a0a0a0; /* 占位符颜色 */
}

.custom-textarea:disabled {
  background-color: #f5f5f5; /* 禁用时的背景色 */
}
</style>
