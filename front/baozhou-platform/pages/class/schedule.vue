<template>
  <view class="container">
    <!-- 页面标题 -->
    <view class="title">📚 课程表</view>

    <!-- 统计信息 -->
    <view class="stats">
      <view class="stat-item">
        <view class="stat-value">{{ totalClasses }}</view>
        <view class="stat-label">总班级数</view>
      </view>
      <view class="stat-item">
        <view class="stat-value">{{ totalStudents }}</view>
        <view class="stat-label">在读学生</view>
      </view>
      <view class="stat-item">
        <view class="stat-value">{{ totalTeachers }}</view>
        <view class="stat-label">授课教师</view>
      </view>
    </view>

    <!-- 筛选栏 -->
    <view class="filter-bar">
      <text class="filter-label">星期：</text>
      <picker class="filter-picker" mode="selector" :range="weekdayOptions" @change="onWeekdayChange">
        <view class="filter-picker-view">
          {{ filters.weekday || '全部' }}
          <text class="arrow">›</text>
        </view>
      </picker>

      <text class="filter-label">学科：</text>
      <picker class="filter-picker" mode="selector" :range="subjectOptions" @change="onSubjectChange">
        <view class="filter-picker-view">
          {{ filters.subject || '全部' }}
          <text class="arrow">›</text>
        </view>
      </picker>

      <button class="btn-reset" @click="resetFilters">重置</button>
    </view>

    <!-- 课程表列表 -->
    <view class="schedule-list">
      <!-- 加载中 -->
      <view v-if="loading" class="loading">
        <text class="loading-text">⏳ 正在加载课程表...</text>
      </view>

      <!-- 错误提示 -->
      <view v-else-if="error" class="error">
        <text class="error-text">❌ {{ error }}</text>
      </view>

      <!-- 空数据 -->
      <view v-else-if="filteredSchedule.length === 0" class="empty">
        <text class="empty-text">📭 暂无课程数据</text>
      </view>

      <!-- 课程卡片 -->
      <view
        v-for="item in filteredSchedule"
        :key="item.classId"
        class="schedule-card"
      >
        <!-- 卡片头部 -->
        <view class="card-header">
          <view class="card-title">
            <text class="weekday-tag">{{ item.weekday }}</text>
            <text class="subject-tag">{{ item.subject }}</text>
            <text class="time-text">{{ item.timeSlot }}</text>
          </view>
          <view
            class="class-type-badge"
            :class="item.classType === '一对一' ? 'type-one-on-one' : 'type-one-on-three'"
          >
            {{ item.classType }}
          </view>
        </view>

        <!-- 卡片内容 -->
        <view class="card-content">
          <view class="info-row">
            <text class="info-icon">👨‍🏫</text>
            <text class="info-text">教师：{{ item.teacherName || '未分配' }}</text>
          </view>
          <view class="info-row">
            <text class="info-icon">🏫</text>
            <text class="info-text">教室：{{ item.classroom || '-' }} ({{ item.floor || '-' }})</text>
          </view>
          <view class="info-row">
            <text class="info-icon">👥</text>
            <text class="info-text">学生：{{ item.currentStudents || 0 }} / {{ item.maxStudents || 0 }}人</text>
          </view>
        </view>

        <!-- 学生列表 -->
        <view v-if="item.students && item.students.length > 0" class="student-section">
          <view class="student-title">📋 学生名单：</view>
          <view class="student-tags">
            <view
              v-for="student in item.students"
              :key="student.studentId"
              class="student-tag"
            >
              {{ student.studentName }} ({{ student.grade }}年级)
            </view>
          </view>
        </view>
        <view v-else class="student-section">
          <view class="student-empty">暂无学生</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      // 所有课程数据
      allScheduleData: [],

      // 加载状态
      loading: true,
      error: '',

      // 筛选条件
      filters: {
        weekday: '',
        subject: ''
      },

      // 筛选选项
      weekdayOptions: ['全部', '周一', '周二', '周三', '周四', '周五', '周六', '周日'],
      subjectOptions: ['全部', '数学', '英语', '语文', '物理', '化学', '政治', '历史', '地理']
    }
  },

  computed: {
    // 根据筛选条件过滤数据
    filteredSchedule() {
      let result = this.allScheduleData;

      if (this.filters.weekday && this.filters.weekday !== '全部') {
        result = result.filter(item => item.weekday === this.filters.weekday);
      }

      if (this.filters.subject && this.filters.subject !== '全部') {
        result = result.filter(item => item.subject === this.filters.subject);
      }

      return result;
    },

    // 统计数据
    totalClasses() {
      return this.filteredSchedule.length;
    },

    totalStudents() {
      return this.filteredSchedule.reduce((sum, item) => sum + (item.currentStudents || 0), 0);
    },

    totalTeachers() {
      const uniqueTeachers = new Set(this.filteredSchedule.map(item => item.teacherId));
      return uniqueTeachers.size;
    }
  },

  methods: {
    // 星期筛选变化
    onWeekdayChange(e) {
      const index = e.detail.value;
      this.filters.weekday = this.weekdayOptions[index];
    },

    // 学科筛选变化
    onSubjectChange(e) {
      const index = e.detail.value;
      this.filters.subject = this.subjectOptions[index];
    },

    // 重置筛选
    resetFilters() {
      this.filters = {
        weekday: '',
        subject: ''
      };
    },

    // 加载课程表数据
    async loadSchedule() {
      this.loading = true;
      this.error = '';

      try {
        const response = await fetch('http://localhost:8080/api/class/schedule');
        if (!response.ok) {
          throw new Error('网络响应失败');
        }
        const data = await response.json();
        this.allScheduleData = data;
      } catch (e) {
        console.error('加载失败:', e);
        this.error = '加载课程表失败，请确保后端服务已启动（http://localhost:8080）';
      } finally {
        this.loading = false;
      }
    }
  },

  // 页面加载时自动获取数据
  onLoad() {
    this.loadSchedule();
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.title {
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  color: white;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
}

.stats {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  padding: 15px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #667eea;
}

.stat-label {
  font-size: 12px;
  color: #666;
  margin-top: 5px;
}

.filter-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
  padding: 15px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.filter-label {
  font-size: 14px;
  color: #333;
  font-weight: bold;
}

.filter-picker {
  flex: 1;
  min-width: 120px;
}

.filter-picker-view {
  height: 36px;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 0 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  color: #333;
  background: white;
}

.arrow {
  color: #999;
  font-size: 18px;
}

.btn-reset {
  padding: 8px 16px;
  background: #f0f0f0;
  color: #333;
  border: none;
  border-radius: 5px;
  font-size: 14px;
}

.schedule-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.schedule-card {
  background: white;
  border-radius: 10px;
  padding: 15px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  transition: all 0.3s ease;
}

.schedule-card:active {
  transform: scale(0.98);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #667eea;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.weekday-tag {
  padding: 4px 12px;
  background: #667eea;
  color: white;
  border-radius: 5px;
  font-size: 12px;
  font-weight: bold;
}

.subject-tag {
  padding: 4px 12px;
  background: #FF9800;
  color: white;
  border-radius: 5px;
  font-size: 12px;
  font-weight: bold;
}

.time-text {
  font-size: 14px;
  color: #333;
  font-weight: bold;
}

.class-type-badge {
  padding: 5px 12px;
  border-radius: 15px;
  font-size: 12px;
  font-weight: bold;
}

.type-one-on-one {
  background: #4CAF50;
  color: white;
}

.type-one-on-three {
  background: #2196F3;
  color: white;
}

.card-content {
  margin-bottom: 15px;
}

.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
}

.info-icon {
  margin-right: 8px;
}

.info-text {
  color: #333;
}

.student-section {
  padding: 10px;
  background: #fff9e6;
  border-radius: 5px;
  border-left: 3px solid #ffc107;
}

.student-title {
  font-size: 13px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.student-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.student-tag {
  padding: 4px 10px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 12px;
  font-size: 12px;
  color: #555;
}

.student-empty {
  font-size: 12px;
  color: #999;
}

.loading, .error, .empty {
  text-align: center;
  padding: 40px;
  font-size: 16px;
}

.loading-text, .empty-text {
  color: white;
}

.error-text {
  color: #fff;
}
</style>
