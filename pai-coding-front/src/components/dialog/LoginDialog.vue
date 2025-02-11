<template>
  <a-modal
    :open="clicked"
    @cancel="closeLoginDialog"
    :width="900"
    :footer="null"
    class="custom-modal"
  >
    <a-layout class="container">
      <a-layout-sider width="380" class="left-panel">
        <!-- 调整左侧宽度 -->
        <div class="image-container">
          <!-- 图片容器 -->
          <img src="../../assets/static/img/LoginAndRegister.png" class="modal-image" />
        </div>
      </a-layout-sider>

      <a-layout class="right-panel">
        <a-layout-header :style="headerStyle">
          <div class="header-content">
            <a-typography-title :level="2" class="welcome-title">Welcome!</a-typography-title>
            <a-typography-title :level="4" class="sub-title">
              Learn at paicoding 📖
            </a-typography-title>
          </div>
        </a-layout-header>

        <a-layout-content :style="contentStyle">
          <a-form class="register-form" :model="formState" @finish="login" ref="formRef">
            <a-typography-title :level="5">Name</a-typography-title>
            <a-form-item name="username" :rules="[{ required: true, message: '请输入名字!' }]">
              <a-input v-model:value="formState.username" placeholder="Name" size="large" />
            </a-form-item>
            <a-typography-title :level="5">Password</a-typography-title>
            <a-form-item name="password" :rules="[{ required: true, message: '请输入密码!' }]">
              <a-input-password
                v-model:value="formState.password"
                placeholder="Password"
                size="large"
              />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" html-type="submit" size="large" block>登录</a-button>
            </a-form-item>
          </a-form>
          <a-typography-text class="footer-text">
            未有账号？
            <a-button type="link" @click="toRegister" style="padding: 0">注册</a-button>
          </a-typography-text>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </a-modal>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { doGet, doPost } from '@/http/BackendRequests'
import { LOGIN_USER_NAME_URL } from '@/http/URL'
import { inject } from 'vue'
import { useUserStore } from '@/stores/global.js'
import { message } from 'ant-design-vue'
const props = defineProps(['clicked'])
// 关闭 Modal
const closeLoginDialog = inject('loginDialogClicked')
// 注册按钮相关
const RegisterDialogClicked = inject('RegisterDialogClicked')
const toRegister = () => {
  if (props.clicked) {
    closeLoginDialog()
  }
  RegisterDialogClicked()
}

// 创建表单实例的 ref
const formRef = ref(null)
const formState = reactive({
  username: '',
  password: ''
})

// global
const userStore = useUserStore()

// 提交表单的逻辑
const login = () => {
  doPost(LOGIN_USER_NAME_URL, {
    username: formState.username,
    password: formState.password
  })
    .then((response) => {
      if (response.data.status.code === 200) {
        message.success('登录成功')
        userStore.login(response.data.result.avatar)
        closeLoginDialog()
        // refreshPage() // 刷新页面
      }
    })
    .catch((error) => {
      // some error handle
    })
}

// 样式配置
const headerStyle = {
  height: 'auto',
  padding: '40px 50px 20px',
  backgroundColor: '#ffffff'
}

const contentStyle = {
  padding: '0 50px',
  backgroundColor: '#ffffff'
}
</script>

<style scoped>
/* 弹窗样式调整 */

.custom-modal {
  top: 20px; /* 增加顶部间距 */
}

.custom-modal :deep(.ant-modal-body) {
  padding: 0; /* 去除默认内边距 */
  max-height: 70vh; /* 控制最大高度 */
  overflow-y: auto; /* 允许内容滚动 */
}

/* 容器调整 */
.container {
  min-height: 500px; /* 固定最小高度 */
  background: #ffffff;
  display: flex;
}

.left-panel {
  background: #ffffff !important;
  border-right: 1px solid #f0f0f0;
  padding: 20px;
}

/* 图片区域优化 */
.image-container {
  height: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 添加外阴影 */
}

.modal-image {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 保持图片比例 */
}

/* 表单元素紧凑布局 */
.register-form {
  max-width: 100%; /* 使用全部可用宽度 */
}

.ant-typography-title {
  margin-bottom: 8px !important; /* 缩小标题间距 */
  font-size: 14px; /* 调小标签字号 */
  color: #666;
}

.ant-form-item {
  margin-bottom: 16px; /* 减小表单项间距 */
}
</style>
