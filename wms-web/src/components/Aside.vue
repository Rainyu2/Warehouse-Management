<script setup lang="ts">
import {computed, defineProps, ref} from 'vue';
import { HomeFilled, Promotion } from '@element-plus/icons-vue';
import { ElTooltip } from 'element-plus';
import {useStore} from "vuex";

// 定义 props 接收 父组件元素
const props = defineProps<{
  isCollapse: boolean;
}>();


const store = useStore();

const menu = computed(() => {
    return store.state.menu;
})
</script>

<template>
  <el-scrollbar class="scroll">
    <el-menu
        class="menu"
        active-text-color="#2df4sa"
        text-color="#fff"
        background-color="transparent"
        :collapse="props.isCollapse"
        :collapse-transition="false"
        default-active="/Home"
        router
    >
      <el-menu-item index="/Home" class="logo">
        <el-tooltip placement="right" effect="dark">
          <template #content>
            <span slot="title">首页</span>
          </template>
          <el-icon><HomeFilled /></el-icon>
        </el-tooltip>
        <span>首页</span>
      </el-menu-item>

      <el-menu-item :index="'/'+item.menuClick" class="logo" v-for="(item,i) in menu" :key="i">
        <el-tooltip placement="right" effect="dark">
          <template #content>
            <span>{{item.menuName}}</span>
          </template>
          <el-icon><Promotion /></el-icon>
        </el-tooltip>
        <span>{{ item.menuName }}</span>
      </el-menu-item>


    </el-menu>
  </el-scrollbar>
</template>

<style scoped>
.scroll {
  height: 100%;
  .menu {
    border-right: none;
  }
}
</style>