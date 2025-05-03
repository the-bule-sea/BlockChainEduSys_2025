import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import Dashboard from '../components/Dashboard.vue';
import AdminLayout from '../layout/AdminLayout.vue';
import UserLayout from '../layout/UserLayout.vue';
import ManageDegree from '../components/ManageDegree.vue';

const routes = [
  {
    path: '/',
    redirect: '/login',
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
  },
  {
    path: '/user',
    name: 'UserLayout',
    component: UserLayout,
    children: [
      {
        path: '',
        name: 'UserDashboard',
        component: Dashboard,
      }
    ]
  },
  {
    path: '/admin',
    name: 'Layout',
    component: AdminLayout,
    children: [
      {
        path: '',
        name: 'ManageDegree',
        component: ManageDegree,
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;