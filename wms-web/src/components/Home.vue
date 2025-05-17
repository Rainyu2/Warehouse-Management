<script setup lang="ts">
import {computed, onMounted, onUnmounted, ref} from 'vue'
import {
  Iphone,
  StarFilled,
  Avatar,
  Pointer,
  User,
  Clock,
} from '@element-plus/icons-vue'
import type { ComponentSize } from 'element-plus'
import axios from 'axios'

const size = ref<ComponentSize>('default')
const currentTime = ref<string>('')
let timer: number
const formatTime = (date: Date) => {
  const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  return `${date.toLocaleDateString()} ${weekDays[date.getDay()]} ${date.toLocaleTimeString()}`
}

const cardMap = ref([
  {
    title: '用户数',
    value: 0,
    icon: 'User'
  },
  {
    title: '仓库数',
    value: 0,
    icon: 'House'
  },
  {
    title: '物品种类数',
    value: 0,
    icon: 'Goods'
  }
])

// 获取统计数据
const getStatistics = async () => {
  try {
    // 获取用户数
    const userResponse = await axios.get('/list')
    if (userResponse.data.code === 200) {
      cardMap.value[0].value = userResponse.data.data.length
    }

    // 获取仓库数
    const storageResponse = await axios.get('/storage/list')
    if (storageResponse.data.code === 200) {
      cardMap.value[1].value = storageResponse.data.data.length
    }

    // 获取物品种类数
    const goodsResponse = await axios.get('/goods/list')
    if (goodsResponse.data.code === 200) {
      cardMap.value[2].value = goodsResponse.data.data.length
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

const iconStyle = computed(() => {
  const marginMap = {
    large: '8px',
    default: '6px',
    small: '4px',
  }
  return {
    marginRight: marginMap[size.value] || marginMap.default,
  }
})

const token = ref<string | null>(null);
const userName = ref<string>('');
const phone = ref<string>('');
const age = ref<number>(0);
const sex = ref<number>(0);
const roleId = ref<number>(0);
onMounted(() => {
  token.value = sessionStorage.getItem('token');
  if (token.value) {
    try {
      // 直接解析token字符串为JSON对象
      const userData = JSON.parse(token.value);
      userName.value = userData.name || '未知用户';
      phone.value = userData.phone || '未知电话';
      age.value = userData.age || 0;
      sex.value = Number(userData.sex) || 0;
      roleId.value = Number(userData.roleId) || 0;
      console.log('当前用户:', userName.value);
    } catch (e) {
      console.error('token解析失败:', e);
      userName.value = '未知用户';
      phone.value = '未知电话';
      age.value = 0;
      sex.value = 0;
      roleId.value = 0;
    }
  }
  timer = setInterval(() => {
    currentTime.value = formatTime(new Date())
  },0)
  getStatistics()
});

onUnmounted(() => {
  clearInterval(timer)
})


</script>

<template>
  <h1>{{'欢迎你! '+userName}}</h1>
  <el-descriptions
      class="margin-top"
      title="用户信息"
      :column="3"
      :size="size"
      border
  >

    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon :style="iconStyle">
            <user />
          </el-icon>
          Username
        </div>
      </template>
      {{userName}}
    </el-descriptions-item>

    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon :style="iconStyle">
            <Avatar />
          </el-icon>
          Role
        </div>
      </template>
      <el-tag
          :type="roleId === 0 ? 'danger' : (roleId === 1 ? 'primary' : 'success')"
          disable-transitions
      >{{ roleId === 0 ? '超级管理员' : (roleId === 1 ? '管理员' : '用户')}}</el-tag>
    </el-descriptions-item>

    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon :style="iconStyle">
            <StarFilled />
          </el-icon>
          Age
        </div>
      </template>
      {{ age }}
    </el-descriptions-item>

    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon :style="iconStyle">
            <Pointer />
          </el-icon>
          Sex
        </div>
      </template>
      <el-tag
          :type="sex === 1 ? 'primary' : 'success'"
          disable-transitions
      >{{ sex === 1 ? '男' : '女'}}</el-tag>
    </el-descriptions-item>

    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon :style="iconStyle">
            <iphone />
          </el-icon>
          Telephone
        </div>
      </template>
      {{ phone }}
    </el-descriptions-item>

  </el-descriptions>
  <div v-if="currentTime" class="time-display">
    <el-icon :size="20" style="margin-right: 8px">
      <Clock />
    </el-icon>
    <span>{{ currentTime }}</span>
  </div>

  <div class="card" v-if="roleId != 2">
    <el-card class="card-item" shadow="always" v-for="(item, index) in cardMap" :key="index">
      <div class="card-content">
        <div class="card-title">{{ item.title }}</div>
        <div class="card-value">{{ item.value }}</div>
      </div>
    </el-card>
  </div>

</template>

<style scoped>
.el-descriptions {
  margin-top: 20px;
}
.cell-item {
  display: flex;
  align-items: center;
}
.margin-top {
  margin-top: 20px;
}
.time-display {
  margin-top: 30px;
  text-align: center;
  font-size: 1.2em;
  color: #606266;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
.card{
  margin-top: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  .card-item{
    width: 300px;
    height: 300px;
    border-radius: 10px;
    align-items: center;
    justify-content: center;
    background: rgba(255, 255, 255, 0.15);
    backdrop-filter: blur(8px);
    -webkit-backdrop-filter: blur(8px);
    border: 1px solid rgba(255, 255, 255, 0.18);
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
    position: relative;
    overflow: hidden;
    margin: 0 40px;
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(135deg, rgba(0, 0, 0, 0.1) 0%, rgba(0, 0, 0, 0.05) 100%);
      backdrop-filter: blur(5px);
      -webkit-backdrop-filter: blur(5px);
      z-index: 1;
    }
    .card-content {
      position: relative;
      z-index: 2;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: flex-start;
      width: 100%;
      padding-top: 40px;
      .card-title {
        font-size: 40px;
        color: #818186;
        margin-bottom: 20px;
        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
      }
      .card-value {
        font-size: 45px;
        font-weight: bold;
        margin-top: 10px;
        color: rgba(84, 79, 79, 0.2);
        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
      }
    }
  }
}

</style>