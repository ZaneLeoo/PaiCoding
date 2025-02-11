import { createApp, defineComponent, h } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
// 导入 css 样式
import 'ant-design-vue/dist/reset.css'
import '@/assets/style.css'
import App from './App.vue'
import router from '@/router/index.js'

// 创建一个 H2 组件映射到原生 h2 标签
const H2 = defineComponent({
  setup(_, { slots }) {
    return () => h('h2', slots.default ? slots.default() : [])
  }
})

const app = createApp(App)
const pinia = createPinia()

// 先创建 Pinia 实例并使用它
app.use(pinia)
// 然后继续使用其他插件
app.use(router)
app.use(Antd)

// 注册 H2 组件
app.component('H2', H2)

// 最后挂载应用
app.mount('#app')
