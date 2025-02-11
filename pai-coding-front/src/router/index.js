import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ArticleEdit from '@/views/article/ArticleEdit.vue'
import ArticlePage from '@/views/article/ArticlePage.vue'
import ArticleCategory from '@/components/category/ArticleCategory.vue'
import CategoryArticleList from '@/components/category/CategoryArticleList.vue'
import UserHome from '@/views/user/UserHome.vue'
import NotFound from '@/views/error/NotFound.vue'
import { useUserStore } from '@/stores/global.js'
import { message } from 'ant-design-vue'
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: HomeView
    },
    {
      path: '/index',
      name: 'index',
      component: HomeView
    },
    {
      path: '/article/edit',
      component: ArticleEdit,
      beforeEnter: (to, from, next) => {
        const userStore = useUserStore()
        if (!userStore.isLogin) {
          message.warning('你必须登录才可以编写文章!😢')
          next('/')
        }
        next()
      }
    },
    {
      path: '/article/page/:articleId',
      component: ArticlePage
    },
    {
      path: '/article/category',
      component: ArticleCategory
    },
    {
      path: '/article/category/:tagName',
      component: CategoryArticleList
    },
    {
      path: '/user/home',
      component: UserHome
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: NotFound
    }
  ]
})

export default router
