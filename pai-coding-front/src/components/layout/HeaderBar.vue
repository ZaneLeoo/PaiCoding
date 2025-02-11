<template>
  <header class="product-hunt-header">
    <div class="container">
      <div class="logo">
        <a href="/">
          <img src="../../assets/static/img/logo.png" alt="Your Logo" />
        </a>
      </div>
      <div class="search-box">
        <input type="text" placeholder="Search products..." v-model="searchTerm" />
        <button @click="search">
          <i class="fas fa-search">Search</i>
        </button>
      </div>
      <nav class="main-nav">
        <router-link to="/article/category">Categories</router-link>
      </nav>
      <div class="user-actions">
        <router-link class="submit-button" to="/article/edit">Add Article</router-link>
        <div v-if="!user.isLogin">
          <button class="login-button" @click="openLogin">Login</button>
        </div>
        <div v-else>
          <a-dropdown>
            <a-avatar :size="64" :src="user.avatar">
              <template #icon><UserOutlined /></template>
            </a-avatar>
            <template #overlay>
              <a-card style="width: 200px; height: 130px">
                <div class="card-item">
                  <UserOutlined />
                  <router-link class="link">Home</router-link>
                </div>
                <div class="card-item">
                  <LogoutOutlined />
                  <button class="logout-btn" @click="logout">Logout</button>
                </div>
              </a-card>
            </template>
          </a-dropdown>
        </div>
      </div>
    </div>
  </header>
</template>
<script setup>
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/global.js'
import { inject, reactive, ref } from 'vue'
import { LogoutOutlined, UserOutlined } from '@ant-design/icons-vue'
import { LOGOUT_URL } from '@/http/URL'
import { doGet } from '@/http/BackendRequests.js'
import { useRouter } from 'vue-router'
const user = useUserStore()
const router = useRouter()
const loginDialogClicked = inject('loginDialogClicked')
const searchTerm = ref('')
const search = () => {
  // 在这里执行搜索操作
  console.log('Searching for:', this.searchTerm)
}
const openLogin = () => {
  loginDialogClicked()
}
const logout = () => {
  doGet(LOGOUT_URL).then(() => {
    message.success('退出登录成功!✌️')
    setTimeout(() => {
      router.push('/') // 跳转到首页
      window.location.reload() // 刷新页面
    }, 500) // 延迟 1 秒
  })
}
</script>
<style scoped>
.product-hunt-header {
  background-color: #fff;
  padding: 1rem 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100; /* 确保导航栏置于其他元素上方 */
}

.container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.logo img {
  height: 40px; /* 调整logo高度 */
}

.search-box {
  display: flex;
  flex: 1; /* 搜索框占据剩余空间 */
  margin: 0 20px;
  margin-right: 400px;
}

.search-box input {
  flex: 1;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.search-box button {
  background-color: transparent;
  border: none;
  padding: 0.5rem;
  cursor: pointer;
}

.main-nav ul {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
}

.main-nav li {
  margin-left: 20px;
}

.main-nav a {
  text-decoration: none;
  color: #333;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.submit-button,
.login-button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-left: 10px;
}

.submit-button {
  background-color: #da552f;
  color: #fff;
}

.login-button {
  background-color: #fff;
  border: 1px solid #ccc;
  color: #333;
}
.dropdown-item {
  padding: 10px;
}
/* 卡片项的布局 */
.card-item {
  display: flex;
  align-items: center; /* 垂直居中 */
  margin-bottom: 16px; /* 每项之间有点间距 */
  gap: 10px;
}

/* 取消按钮的边框 */
.logout-btn {
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 16px;
}
.link {
  cursor: pointer;
  font-size: 16px;
}
.card-item:hover {
  color: #ff4d4f; /* 悬停时链接变色 */
}

.logout-btn:hover {
  color: #ff4d4f; /* 悬停时按钮文字变色 */
}
</style>
