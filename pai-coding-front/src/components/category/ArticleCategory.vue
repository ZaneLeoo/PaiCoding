<template>
  <div class="container">
    <div class="category-container">
      <h2 class="title">Tags</h2>
      <div
        v-for="(row, rowIndex) in rows"
        :key="rowIndex"
        class="floating-row"
        :style="{
          top: rowIndex * 60 + 'px', // 每行的位置
          opacity: row.opacity
        }"
      >
        <div
          v-for="(tag, index) in row.tags"
          :key="index"
          class="floating-tag"
          :style="{
            transitionDelay: `${index * 100}ms`, // 控制每个标签的显示延迟
            opacity: tag.opacity
          }"
          @mouseenter="stopFloating(rowIndex, index)"
          @mouseleave="resumeFloating(rowIndex, index)"
          @click="navigateToTag(tag.name)"
        >
          <button class="tag-button">
            <TagOutlined />
            <i class="anticon anticon-tag"></i>
            {{ tag.name }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { TagOutlined } from '@ant-design/icons-vue'
import { doGet } from '@/http/BackendRequests.js'
import { TAG_LIST_URL } from '@/http/URL'

const router = useRouter()
const tagsPerRow = 7

// 使用响应式引用
const tagsData = ref([])
const tags = ref([])
const rows = ref([])

// 单独提取数据处理逻辑
const processTags = () => {
  tags.value = tagsData.value.map((name) => ({
    name,
    active: true,
    opacity: 0
  }))

  // 清空原有行数据
  rows.value = []
  const rowCount = Math.ceil(tags.value.length / tagsPerRow)

  for (let i = 0; i < rowCount; i++) {
    rows.value.push({
      tags: tags.value.slice(i * tagsPerRow, (i + 1) * tagsPerRow),
      opacity: 1
    })
  }
}

const showTags = () => {
  tags.value.forEach((tag, index) => {
    setTimeout(() => {
      tag.opacity = 1
    }, index * 200)
  })
}

onMounted(() => {
  doGet(TAG_LIST_URL)
    .then((response) => {
      // 更新响应式数据
      tagsData.value = response.data.result.map((item) => item.name)
      processTags() // 处理新的标签数据
      showTags() // 需要在这里调用显示动画
    })
    .catch((error) => {
      console.error('Failed to fetch tags:', error)
    })
})

// 路由跳转
const navigateToTag = (tagName) => {
  router.push(`/article/category/${tagName}`)
}
</script>

<style scoped>
.category-container {
  padding: 100px;
}

.container {
  position: relative;
  width: 1200px; /* 设置容器的固定宽度 */
  margin: 0 auto; /* 居中显示 */
  padding: 50px;
  background: #ffffff; /* 背景色改为白色 */
}

.floating-row {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  margin-bottom: 20px; /* 行与行之间的间距 */
  opacity: 0; /* 初始行不可见 */
  animation: fadeInRow 1s forwards; /* 每行淡入效果 */
}

.floating-tag {
  margin-right: 20px; /* 标签之间的间隔 */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1); /* 灰色阴影 */
  color: #000000; /* 黑色字体 */
  cursor: pointer;
  opacity: 0; /* 标签初始不可见 */
  transition: opacity 0.5s ease-in-out; /* 过渡效果 */
}

.tag-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 8px 16px;
  background-color: #e6f7ff; /* 更柔和的背景颜色 */
  border: none; /* 取消边框 */
  border-radius: 4px; /* 更小的圆角 */
  cursor: pointer;
  font-size: 16px;
  color: #000; /* 更深的文字颜色 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 更柔和的阴影 */
  transition:
    transform 0.3s ease,
    background-color 0.3s ease;
  white-space: nowrap;
}

.tag-button:hover {
  transform: scale(1.05);
  background-color: #4fa4d3; /* 鼠标悬停时背景颜色变化 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15); /* 鼠标悬停时阴影变化 */
}

.tag-button i {
  margin-right: 8px; /* 图标与文字的间距 */
}
.title {
  text-align: center; /* 水平居中 */
  font-size: 34px;
  margin: 50px 0; /* 上下外边距，调整间隔 */
}

:deep(.ant-tag) {
  font-size: 14px;
  padding: 10px 24px;
}

@keyframes fadeInRow {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}
</style>
