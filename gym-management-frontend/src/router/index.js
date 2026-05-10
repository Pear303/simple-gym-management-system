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

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  console.log('路由守卫触发，目标路径：', to.path)
  console.log('token: ' + localStorage.getItem('token'))
  console.log('adminInfo: ' + localStorage.getItem('adminInfo'))

  if (to.path !== '/login' && !token) {  // 非登录页面且无访问数据，重定向到登录页
    console.log('localStorage 中的 token：' + localStorage.getItem('token'))
    console.log('localStorage 中的 adminInfo：' + localStorage.getItem('adminInfo'))
    
    next('/login')
    
  } else if (to.path === '/login' && token) {  // 登录页面且有访问数据，重定向到首页
    next('/dashboard')
  } else {
    next()
  }
})

export default router