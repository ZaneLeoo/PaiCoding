<template>
  <div class="container">
    <a-layout class="main-layout">
      <a-layout>
        <a-layout-content class="content">
          <TopArticles></TopArticles>
        </a-layout-content>
        <a-layout-sider width="420" class="siderStyle" style="border-left: 1px solid #f2f2f2">
          <Sider></Sider>
        </a-layout-sider>
      </a-layout>
      <Footer class="footer"></Footer>
    </a-layout>
  </div>
</template>

<script setup>
import { onMounted, provide, reactive, ref } from 'vue'
import Footer from '@/components/layout/Footer.vue'
import Sider from '@/components/layout/Sider.vue'
import TopArticles from '@/components/article/TopArticles.vue'
</script>

<style scoped>
.main-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: white;
  max-width: 100%; /* 确保在小屏幕上不会超出 */
}

.container {
  width: 100%;
  max-width: 1300px; /* 设置最大宽度 */
  margin: auto; /* 保证居中 */
}

/* 页脚固定在页面底部 */
.footer {
  margin-top: auto; /* 保证页脚在底部 */
}

.siderStyle {
  line-height: '120px';
  background-color: white;
}

.content {
  background-color: white;
}
</style>
