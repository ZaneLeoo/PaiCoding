<template>
  <a-modal
    :open="clicked"
    @cancel="closeRegisterDialog"
    title="Register"
    :width="900"
    :footer="null"
    class="custom-modal"
  >
    <a-layout class="container">
      <a-layout-sider width="380" class="left-panel">
        <!-- 调整左侧宽度 -->
        <div class="image-container">
          <!-- 图片容器优化 -->
          <img src="../../assets/static/img/LoginAndRegister.png" class="modal-image" />
        </div>
      </a-layout-sider>

      <a-layout>
        <a-layout-header :style="headerStyle">
          <div class="header-content">
            <a-typography-title :level="2" class="welcome-title">Welcome!</a-typography-title>
            <a-typography-title :level="4" class="sub-title"
              >Create your account now 🤠</a-typography-title
            >
          </div>
        </a-layout-header>

        <a-layout-content :style="contentStyle">
          <a-form ref="formRef" class="register-form" :model="formState" @finish="register">
            <a-typography-title :level="5">Name</a-typography-title>
            <a-form-item
              name="name"
              :rules="[{ required: true, min: 2, message: '请输入你的名字!' }]"
            >
              <a-input v-model:value="formState.name" placeholder="Name" size="large" />
            </a-form-item>

            <a-typography-title :level="5">Password</a-typography-title>
            <a-form-item
              name="password"
              :rules="[
                { required: true, message: '请输入你的密码!' },
                { min: 6, message: '密码应该大于6位' }
              ]"
            >
              <a-input-password
                v-model:value="formState.password"
                placeholder="Password"
                size="large"
              />
            </a-form-item>

            <!-- 新增确认密码表单项 -->
            <a-typography-title :level="5">Confirm Password</a-typography-title>
            <a-form-item
              name="confirmPassword"
              :rules="[
                { required: true, message: '请确认密码!' },
                { validator: validatePasswordMatch }
              ]"
            >
              <a-input-password
                v-model:value="formState.confirmPassword"
                placeholder="Confirm Password"
                size="large"
              />
            </a-form-item>

            <!-- 提交按钮添加loading状态 -->
            <a-button type="primary" html-type="submit" size="large" block :loading="loading">
              注册
            </a-button>
          </a-form>
          <a-typography-text class="footer-text">
            已有账号？
            <a-button type="link" @click="toLogin" style="padding: 0">登录</a-button>
          </a-typography-text>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </a-modal>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { inject } from 'vue'
import { REGISTER_URL } from '@/http/URL'
import { doPost } from '@/http/BackendRequests'
import { message } from 'ant-design-vue'

const openLoginDialog = inject('loginDialogClicked')
const closeRegisterDialog = inject('RegisterDialogClicked')
const props = defineProps(['clicked'])
const toLogin = () => {
  closeRegisterDialog()
  openLoginDialog()
}
const formRef = ref(null)
const loading = ref(false)
const formState = reactive({
  name: '',
  password: '',
  confirmPassword: ''
})

const validatePasswordMatch = (_rule, value) => {
  return new Promise((resolve, reject) => {
    if (value !== formState.password) {
      reject('两次密码输入不一致!')
    } else {
      resolve()
    }
  })
}

const register = () => {
  formRef.value
    .validate()
    .then(() => {
      loading.value = true
      doPost(REGISTER_URL, {
        username: formState.name,
        password: formState.password
      })
        .then((response) => {
          if (response.data.status.code === 200) {
            message.success('注册成功')
            formRef.value.resetFields()
            toLogin()
          } else {
            message.error(response.data.msg)
          }
        })
        .catch((error) => {
          message.error('请求错误，请稍后重试')
        })
        .finally(() => {
          loading.value = false
        })
    })
    .catch((error) => {
      console.log('❌ 完整错误对象:', error) // 打印完整错误对象
      console.log('错误字段:', error.errorFields) // 查看错误字段结构
      console.log('验证错误信息:', error.errorFields?.[0]?.errors)
    })
}

const headerStyle = {
  height: 'auto',
  padding: '10px 50px 20px',
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
