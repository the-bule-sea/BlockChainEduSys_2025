import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ELementPlus from 'element-plus';
import 'element-plus/dist/index.css';

createApp(App).use(router).use(ELementPlus).mount('#app');