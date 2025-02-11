<template>
  <div class="container">
    <h4>Some Comments</h4>
    <a-list
      class="demo-loadmore-list"
      :loading="initLoading"
      item-layout="horizontal"
      :data-source="list"
      :split="false"
    >
      <!-- 内容部分 -->
      <template #renderItem="{ item }">
        <a-list-item>
          <a-skeleton avatar :title="false" :loading="!!item.loading" active>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a href="https://www.antdv.com/">{{ item.name }}</a>
              </template>
              <template #avatar>
                <a-avatar :src="item.avatar" :size="45" />
              </template>
            </a-list-item-meta>
          </a-skeleton>
        </a-list-item>
      </template>
    </a-list>
    <a-divider />
    <div class="recommend">
      <h4>Recommended Tags</h4>
      <div class="tag-list">
        <a-tag v-for="tag in topics" :key="tag" class="custom-tag"
          ><router-link :to="tag.link">{{ tag.name }}</router-link></a-tag
        >
      </div>
      <div class="see-more">
        <router-link to="/article/category">See more tags</router-link>
      </div>
    </div>
    <a-divider />
    <div class="author">
      <h4>Recommended Author</h4>
      <a-list
        class="demo-loadmore-list"
        :loading="initLoading"
        item-layout="horizontal"
        :data-source="recommendAuthor"
        :split="false"
      >
        <template #renderItem="{ item }">
          <a-list-item>
            <template #actions> </template>
            <a-skeleton avatar :title="false" :loading="!!item.loading" active>
              <a-list-item-meta :description="item.description">
                <template #title>
                  <a href="https://www.antdv.com/">{{ item.name }}</a>
                </template>
                <template #avatar>
                  <a-avatar :src="item.avatar" :size="45" />
                </template>
              </a-list-item-meta>
            </a-skeleton>
          </a-list-item>
        </template>
        <div class="see-more">
          <a href="#">See more author</a>
        </div>
      </a-list>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { BASE_URL } from '@/http/URL'
const initLoading = ref(false)
const list = ref([
  {
    name: '以己',
    avatar: BASE_URL + '/images/1738829915676_myself.jpg',
    description: 'Paicoding 整体的设计很不错!'
  },
  {
    name: 'Tom',
    avatar: BASE_URL + '/images/Snipaste_2025-02-10_15-05-10.png',
    description: 'Paicoding 是不错的论坛,文章水平不错！😎✌️'
  },
  {
    name: 'Lucy',
    avatar: BASE_URL + '/images/Snipaste_2025-02-10_15-06-39.png',
    description: 'I love there! It is a great place!'
  },
  {
    name: '哪吒',
    avatar: BASE_URL + '/images/Snipaste_2025-02-10_15-10-59.png',
    description: '我命由我不由天!🤠'
  },
  {
    name: 'Julie',
    avatar: BASE_URL + '/images/Snipaste_2025-02-10_15-08-37.png',
    description: '大家好! Paicoding 的各位朋友!😊😊'
  }
])
const topics = ref([
  { name: 'Front', link: '/article/category/Front' },
  { name: 'Web Design', link: '/article/category/WebDesign' },
  { name: 'Linux', link: '/article/category/Linux' },
  { name: 'Java', link: '/article/category/Java' },
  { name: 'Spring Boot', link: '/article/category/SpringBoot' },
  { name: 'Database', link: '/article/category/Database' },
  { name: 'Python', link: '/article/category/Python' }
])
const recommendAuthor = ref([
  {
    name: '杰斯卡',
    avatar: BASE_URL + '/images/Snipaste_2025-02-11_20-12-20.png',
    description: 'Java、Web Design 领域的作家'
  },
  {
    name: 'Ethan Walker',
    avatar: BASE_URL + '/images/Snipaste_2025-02-11_20-35-41.png',
    description: 'Ethan is a database architect with extensive experience in SQL and NoSQL systems'
  },
  {
    name: '张婷',
    avatar: BASE_URL + '/images/Snipaste_2025-02-11_20-32-31.png',
    description: '擅长使用Vue.js、React等现代前端技术构建用户友好的Web应用'
  },
  {
    name: 'Sophia Brown',
    avatar: BASE_URL + '/images/Snipaste_2025-02-11_20-33-37.png',
    description:
      'Sophia is a Linux systems engineer with a deep understanding of server administration, automation, and security.'
  }
])
</script>
<style scoped>
.demo-loadmore-list {
  min-height: 350px;
  max-width: 420px;
}
.container {
  background-color: white;
  padding-top: 45px;
  max-height: 600px;
  position: fixed;
}
.demo-loadmore-list .ant-list-item {
  border-right: none !important; /* 移除右边框 */
  padding-right: 0 !important; /* 移除右内边距 */
}
.recommend {
  max-width: 420px;
}
.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}
.see-more {
  margin-top: 16px; /* 调整 "See more topics" 链接的间距 */
}
.custom-tag {
  background-color: #f0f0f0;
  font-size: 15px;
  color: #333;
  font-weight: c;
  border: none;
  padding: 10px;
  border-radius: 18px;
  cursor: pointer;
}
.author {
  max-width: 430px;
}
</style>
