import { createRouter, createWebHistory } from 'vue-router'
import Login from '../components/Login.vue'
import Index from '../components/Index.vue'

const routes = [
    {
        path: '/',
        name: 'login',
        component: () => import('../components/Login.vue')
    },
    {
        path: '/Index',
        name: 'Index',
        component: () => import('../components/Index.vue'),
        children:[
            {
                path:'/Home',
                name:'Home',
                meta:{
                    title:'首页'
                },
                component:()=>import('../components/Home.vue')
            }/*,
            {
                path:'/Admin',
                name:'admin',
                meta:{
                    title:'管理员管理'
                },
                component:()=>import('../components/admin/AdminManager.vue')
            },
            {
                path:'/User',
                name:'user',
                meta:{
                    title:'用户管理'
                },
                component:()=>import('../components/user/UserManager.vue')
            }*/
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),  // 替代原来的mode配置
    routes
})


export default router