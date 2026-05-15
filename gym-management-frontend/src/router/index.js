import { createRouter, createWebHistory } from 'vue-router'
import { useAdminStore } from '@/store/admin'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    component: () => import('../components/layout/AppLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
      },
      {
        path: '/members',
        name: 'Members',
        component: () => import('../views/Member/MemberList.vue')
      },
      {
        path: '/employees',
        name: 'Employees',
        component: () => import('../views/Employee/EmployeeList.vue')
      },
      {
        path: '/statistics',
        name: 'Statistics',
        component: () => import('../views/Statistics.vue')
      }
    ]
  }
]

// 创建路由器实例
const router = createRouter({
  history: createWebHistory(),
  routes  // ← 传入配置，Vue Router 会自动处理匹配逻辑
})

/* 工作原理：
  √ Vue Router 内部会监听浏览器 URL 变化
  √ 当 URL 变为 /members 时，Router 自动遍历 routes 数组
  √ 通过算法（类似正则匹配）找到 path: '/members' 的配置项
  × 不需要写任何匹配逻辑代码
*/

router.beforeEach((to) => {
  const token = localStorage.getItem('token')

  if (to.path !== '/login' && !token) {
    return '/login'
  }

  if (to.path === '/login' && token) {
    return '/dashboard'
  }
})

export default router