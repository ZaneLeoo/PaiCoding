<template>
  <div class="profile-container">
    <div class="profile-card">
      <section class="action">
        <a-button type="primary">Profile</a-button>
        <a-button type="primary">Edit Profile</a-button>
      </section>
      <!-- 个人信息部分 -->
      <section class="profile-section">
        <div class="user-info">
          <img
            src="http://localhost:8080/images/Snipaste_2025-02-10_15-06-39.png"
            alt="头像"
            class="avatar"
          />
          <div>
            <div class="user-details">
              <div class="details-item">
                <h2>John Doe</h2>
                <a-typography-paragraph>前端工程师</a-typography-paragraph>
              </div>
            </div>
            <div class="skill">
              <a-button type="primary" shape="round" :size="size">
                <template #icon>
                  <DownloadOutlined />
                </template>
                Download
              </a-button>
              <a-button type="primary" shape="round" :size="size">
                <template #icon>
                  <DownloadOutlined />
                </template>
                Download
              </a-button>
              <a-button type="primary" shape="round" :size="size">
                <template #icon>
                  <DownloadOutlined />
                </template>
                Download
              </a-button>
            </div>
          </div>
        </div>
        <div style="display: flex; flex-direction: column">
          <div style="display: flex; gap: 10px">
            <EnvironmentOutlined />
            <a-typography-paragraph>Live in</a-typography-paragraph>
          </div>
          <div style="display: flex; gap: 10px">
            <CoffeeOutlined />
            <a-typography-paragraph>已加入999天</a-typography-paragraph>
          </div>
        </div>
      </section>
      <a-divider />
      <!-- 数据统计部分 -->
      <section class="stats-section">
        <div class="stats-grid">
          <div class="stat-card">
            <div class="stat-icon">
              <BarChartOutlined />
            </div>
            <div class="stat-content">
              <h3>1</h3>
              <p>发布文章数</p>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">
              <EyeOutlined />
            </div>
            <div class="stat-content">
              <h3>1</h3>
              <p>文章观看数</p>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">
              <LikeOutlined />
            </div>
            <div class="stat-content">
              <h3>1</h3>
              <p>文章点赞数</p>
            </div>
          </div>
        </div>
      </section>
      <a-divider />
      <section class="stats-section">
        <div class="stats-grid">
          <div class="stat-card">
            <div class="stat-icon">
              <TeamOutlined />
            </div>
            <div class="stat-content">
              <h3>1</h3>
              <p>你的粉丝们</p>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">
              <TeamOutlined />
            </div>
            <div class="stat-content">
              <h3>1</h3>
              <p>关注的作者</p>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">
              <LikeOutlined />
            </div>
            <div class="stat-content">
              <h3>1</h3>
              <p>点赞的文章</p>
            </div>
          </div>
        </div>
      </section>
      <a-divider />
      <!-- 日历部分 -->
      <section class="calendar-section">
        <div class="months-grid">
          <div
            v-for="month in months"
            :key="month.name"
            class="month-cell"
            :style="{ backgroundColor: getMonthColor(month.count) }"
          >
            <span class="month-name">{{ month.name }}</span>
            <span class="article-count">{{ month.count }} 篇</span>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import {
  BarChartOutlined,
  EyeOutlined,
  LikeOutlined,
  TeamOutlined,
  EnvironmentOutlined,
  CoffeeOutlined
} from '@ant-design/icons-vue'

const months = ref(
  Array.from({ length: 12 }, (_, i) => ({
    name: `${i + 1}月`,
    count: Math.floor(Math.random() * 20)
  }))
)

const getMonthColor = (count) => {
  const intensity = Math.min(0.2 + (count / 20) * 0.8, 1)
  return `rgba(255, 152, 0, ${intensity})`
}
</script>

<style scoped>
.profile-container {
  display: grid;
  place-items: center;
  padding: 2rem;
}

.profile-card {
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 1.5rem;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  padding: 2rem;
  max-width: 800px;
  width: 100%;
  height: 1000px;
  gap: 15px;
  transition: transform 0.3s ease;
}

.profile-card:hover {
  transform: translateY(-5px);
}

.skill {
  display: flex;
  gap: 10px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.user-info .avatar {
  width: 150px;
  height: 150px;
  border-radius: 15%;
  object-fit: cover;
}

.user-info h2 {
  margin: 0;
  font-size: 2rem;
  color: #333;
}
.user-details {
  display: flex;
  gap: 50px;
}

.user-info {
  color: #666;
  margin: 0.5rem 0 1.5rem;
}

.edit-btn {
  background: #ff6b35;
  color: white;
  border: none;
  padding: 0.8rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.2s;
}

.edit-btn:hover {
  background: darken(#ff6b35, 10%);
}

.stats-grid {
  display: flex;
  gap: 1.5rem;
}

.stats-grid .stat-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 12px;
}

.stats-grid .stat-card .stat-icon {
  width: 28px;
  height: 48px;
  border-radius: 8px;
  display: grid;
  place-items: center;
}

.stats-grid .stat-card .stat-icon span {
  color: #da552f;
  font-size: 24px;
}

.stats-grid .stat-card .stat-content h3 {
  margin: 0;
  font-size: 1.5rem;
}

.stats-grid .stat-card .stat-content {
  margin: 0.25rem 0 0;
  color: #666;
  font-size: 0.9rem;
}

.months-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 1rem;
}

.months-grid .month-cell {
  padding: 1rem;
  border-radius: 8px;
  color: white;
  position: relative;
  min-height: 80px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  transition: transform 0.2s;
}

.details-item {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.months-grid .month-cell:hover {
  transform: scale(1.05);
  z-index: 1;
}

.months-grid .month-cell .month-name {
  font-size: 0.9rem;
  font-weight: 500;
}

.months-grid .month-cell .article-count {
  font-size: 0.8rem;
  opacity: 0.9;
}

.action {
  display: flex;
  justify-content: space-between;
}

@media (max-width: 768px) {
  .months-grid {
    grid-template-columns: repeat(3, 1fr);
  }

  .profile-card {
    margin: 1rem;
    padding: 1.5rem;
  }

  .user-info {
    flex-direction: column;
    text-align: center;
  }
}
</style>
