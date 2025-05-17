<template>
  <el-container class="layout-container-demo" style="height: 100vh; width: 100vw">
    <!--侧边栏-->
    <el-aside :width="aside_width">
      <Aside :is-collapse="isCollapse"></Aside>
    </el-aside>
    <el-container>

      <!--头部-->
      <el-header style="text-align: right; font-size: 12px; height: 60px">
        <Header @collapse="handleCollapse" :icon="icon"></Header>
      </el-header>

      <!--主体-->
      <el-main>
<!--        <Main></Main>-->
        <router-view></router-view>
      </el-main>

    </el-container>
  </el-container>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import Aside from '@/components/Aside.vue';
import Main from "@/components/Main.vue";
import Header from "@/components/Header.vue";


// 定义 isCollapse 响应式变量
const isCollapse = ref(false);
const aside_width = ref('200px');
const icon = ref('Fold');

// 处理 Header 组件发出的 collapse 事件
const handleCollapse = (shouldCollapse: boolean) => {
  isCollapse.value = shouldCollapse;
  if (shouldCollapse) {
    // 折叠
    aside_width.value = '64px';
    icon.value = 'Fold';
  }else {
    // 展开
    aside_width.value = '200px';
    icon.value = 'Expand';
  }
};

</script>

<style scoped>
.layout-container-demo .el-header {
  position: relative;
  background:
      linear-gradient(to bottom,transparent,#fff 100%),
      linear-gradient(to right,#9a9c9f,#7f8286,#606468) ;
  color: var(--el-text-color-primary);
  border-bottom:1px solid #ccc;
}
.layout-container-demo .el-aside {
  color: var(--el-text-color-primary);
  background:
      linear-gradient(to bottom,transparent,#fff 100%),
      linear-gradient(to right,#9a9c9f,#7f8286,#606468) ;
}
.layout-container-demo .el-menu {
  border-right: none;
}
.layout-container-demo .el-main {
  padding: 5px;
}

</style>