import { createApp } from 'vue'
import App from './App.vue'
// 引入 Element Plus
import ElementPlus from 'element-plus'
// 引入 Element Plus 样式
import 'element-plus/dist/index.css'
import './assets/global.css'
import axios from 'axios';
import router from './router';
import store from './store';

// 设置 axios 默认基础 URL
axios.defaults.baseURL = 'http://localhost:8090';

const app = createApp(App)
app.use(ElementPlus)
app.use(router)
app.use(store)
// 挂载 axios 到全局属性 $axios
app.config.globalProperties.$axios = axios;

app.mount('#app')