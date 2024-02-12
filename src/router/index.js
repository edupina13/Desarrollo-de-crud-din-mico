import VueRouter from 'vue-router'
import Vue from 'vue'

Vue.use (VueRouter);

const routes = [
    { path: '/libros',
     component:()=> import('../components/Libros.vue') },
     { path: '/formulario',
     component:()=> import('../components/Formulario.vue') },
    
    
       
];

const router = new VueRouter({ routes,})
export default router;