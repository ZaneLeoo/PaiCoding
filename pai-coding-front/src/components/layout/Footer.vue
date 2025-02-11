<template>
  <a-layout-footer class="custom-footer">
    <div class="container">
      <!-- 关注我们 -->
      <a-col :xs="24" :sm="24" :md="24">
        <div class="footer-section">
          <h3 class="section-title">关注我们</h3>
          <div class="social-links">
            <a
              v-for="social in socialLinks"
              :key="social.icon"
              :href="social.url"
              :title="social.name"
              target="_blank"
              class="social-link"
            >
              <component
                :is="social.icon"
                :style="{ fontSize: '32px', color: social.color }"
                class="social-icon"
              />
            </a>
          </div>
        </div>
      </a-col>
    </div>
  </a-layout-footer>
</template>

<script>
// 按需引入图标
import {
  GithubOutlined,
  WechatOutlined,
  FacebookOutlined,
  InstagramOutlined,
  MailOutlined
} from '@ant-design/icons-vue'

export default {
  components: {
    GithubOutlined,
    WechatOutlined,
    FacebookOutlined,
    InstagramOutlined,
    MailOutlined
  },

  data() {
    return {
      socialLinks: [
        {
          name: 'GitHub',
          icon: 'GithubOutlined',
          url: 'https://github.com',
          color: '#333'
        },
        {
          name: '微信',
          icon: 'WechatOutlined',
          url: '#wechat',
          color: '#09b63d'
        },
        {
          name: '小红书',
          icon: 'FacebookOutlined',
          url: 'https://www.xiaohongshu.com',
          color: '#f72585'
        },
        {
          name: '抖音',
          icon: 'InstagramOutlined',
          url: 'https://www.douyin.com',
          color: '#00c2e8'
        },
        {
          name: 'Gmail',
          icon: 'MailOutlined',
          url: 'https://mail.google.com',
          color: '#ea4335'
        }
      ]
    }
  }
}
</script>

<style scoped>
.custom-footer {
  background-color: #fff; /* 设置背景为白色 */
  padding: 48px 0;
  color: rgba(0, 0, 0, 0.65);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.footer-section {
  text-align: center;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 16px;
}

.social-links {
  display: flex;
  justify-content: center;
  gap: 32px;
  flex-wrap: wrap;
}

.social-link {
  display: inline-block;
  transition: transform 0.3s;
}

.social-link:hover {
  transform: scale(1.1);
}

.social-icon {
  font-size: 36px;
  transition: opacity 0.3s;
}

.social-icon:hover {
  opacity: 0.8;
}
</style>
