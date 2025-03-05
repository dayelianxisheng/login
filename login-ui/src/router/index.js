import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/login.vue';
import Layout from '../components/Layout.vue';

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login
  },
  {
    path: '/layout',
    name: 'Layout',
    component: Layout,
    redirect: '/layout/user-management',
    children: [
      {
        path: 'user-management',
        name: 'UserManagement',
        component: () => import('../views/UserManagement.vue')
      },
      {
        path: 'product-management',
        name: 'ProductManagement',
        component: () => import('../views/ProductManagement.vue')
      },
      {
        path: 'user-profile',
        name: 'UserProfile',
        component: () => import('../views/UserProfile.vue')
      }
    ]
  },
  {
    path: '/redirect/:path(.*)',
    component: () => import('../views/Redirect.vue')
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router; 