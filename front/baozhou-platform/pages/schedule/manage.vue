<template>
  <view class="container">
    <!-- 页面标题 -->
    <view class="title">📅 寒假排课管理</view>

    <!-- 学期选择 -->
    <view class="semester-selector">
      <text class="selector-label">选择学期：</text>
      <picker
        class="semester-picker"
        mode="selector"
        :range="semesterOptions"
        :range-key="'label'"
        :value="selectedSemesterIndex"
        @change="onSemesterChange"
      >
        <view class="picker-view">
          <text v-if="selectedSemester">{{ selectedSemester.semesterName }}</text>
          <text v-else class="placeholder">请选择学期</text>
          <text class="arrow">›</text>
        </view>
      </picker>
    </view>

    <!-- 操作按钮 -->
    <view class="action-bar" v-if="selectedSemester">
      <button class="btn btn-generate" @click="generateSchedule">
        🎯 生成排课表
      </button>
      <button class="btn btn-view" @click="viewSchedule">
        📋 查看排课表
      </button>
    </view>

    <!-- 排课统计 -->
    <view class="stats" v-if="selectedSemester && stats.total > 0">
      <view class="stat-item">
        <view class="stat-value">{{ stats.total }}</view>
        <view class="stat-label">总课程数</view>
      </view>
      <view class="stat-item">
        <view class="stat-value">{{ stats.saturday }}</view>
        <view class="stat-label">来自周六</view>
      </view>
      <view class="stat-item">
        <view class="stat-value">{{ stats.sunday }}</view>
        <view class="stat-label">来自周日</view>
      </view>
    </view>

    <!-- 说明信息 -->
    <view class="info-box" v-if="selectedSemester && selectedSemester.semesterType === '寒假班'">
      <view class="info-title">📖 寒假排课规则</view>
      <view class="info-content">
        <text class="info-item">• 第1,3,5天（一三五）：使用周六课表</text>
        <text class="info-item">• 第2,4,6天（二四六）：使用周日课表</text>
        <text class="info-item">• 第7天（周日）：休息</text>
        <text class="info-item">• 生成前请先完成学生请假申请</text>
      </view>
    </view>

    <!-- 排课列表 -->
    <view class="schedule-list" v-if="schedules.length > 0">
      <view class="list-title">排课明细（共{{ schedules.length }}节）</view>
      <view
        v-for="item in schedules"
        :key="item.scheduleId"
        class="schedule-item"
      >
        <view class="item-header">
          <text class="date-text">{{ formatDate(item.courseDate) }}</text>
          <text class="weekday-tag">{{ item.weekday }}</text>
          <text class="source-tag">来自{{ item.sourceWeekday }}</text>
        </view>
        <view class="item-content">
          <text class="time-text">⏰ {{ item.startTime }} - {{ item.endTime }}</text>
          <text class="classroom-text">🏫 教室：{{ item.classroom || '待定' }}</text>
        </view>
      </view>
    </view>

    <!-- 空数据提示 -->
    <view class="empty" v-if="selectedSemester && schedules.length === 0 && !loading">
      <text class="empty-text">📭 该学期还未生成排课表</text>
      <text class="empty-hint">点击上方"生成排课表"按钮开始排课</text>
    </view>

    <!-- 加载中 -->
    <view class="loading" v-if="loading">
      <text class="loading-text">⏳ 正在处理...</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      // 所有学期
      allSemesters: [],
      selectedSemester: null,
      selectedSemesterIndex: -1,

      // 排课数据
      schedules: [],

      // 统计数据
      stats: {
        total: 0,
        saturday: 0,
        sunday: 0
      },

      // 加载状态
      loading: false
    }
  },

  computed: {
    semesterOptions() {
      return this.allSemesters.map(s => ({
        label: s.semesterName,
        value: s.semesterId
      }));
    }
  },

  methods: {
    // 学期选择变化
    onSemesterChange(e) {
      const index = e.detail.value;
      this.selectedSemesterIndex = index;
      this.selectedSemester = this.allSemesters[index];

      // 加载该学期的排课数据
      this.loadSchedule();
    },

    // 生成排课表
    async generateSchedule() {
      if (!this.selectedSemester) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '请先选择学期', icon: 'none' });
        }
        return;
      }

      // 确认对话框
      const confirmed = await this.confirm('确定要生成排课表吗？\n\n生成前请确保：\n1. 学生请假已提交\n2. 班级信息已设置');
      if (!confirmed) {
        return;
      }

      this.loading = true;

      try {
        const response = await fetch(`http://localhost:8080/api/class-schedule/generate/${this.selectedSemester.semesterId}`, {
          method: 'POST'
        });

        if (!response.ok) {
          throw new Error('生成失败');
        }

        const result = await response.json();

        if (typeof uni !== 'undefined') {
          uni.hideLoading();
          uni.showToast({
            title: result.message || '生成成功',
            icon: 'success',
            duration: 2000
          });
        }

        // 重新加载排课数据
        this.loadSchedule();

      } catch (error) {
        console.error('生成失败:', error);
        if (typeof uni !== 'undefined') {
          uni.hideLoading();
          uni.showToast({
            title: '生成失败',
            icon: 'none'
          });
        }
      } finally {
        this.loading = false;
      }
    },

    // 查看排课表
    viewSchedule() {
      if (this.schedules.length === 0) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '请先生成排课表', icon: 'none' });
        }
        return;
      }

      // 可以跳转到详细的排课表页面
      if (typeof uni !== 'undefined') {
        uni.showToast({
          title: '排课表已在下方显示',
          icon: 'none'
        });
      }
    },

    // 加载排课数据
    async loadSchedule() {
      if (!this.selectedSemester) {
        return;
      }

      this.loading = true;

      try {
        const response = await fetch(`http://localhost:8080/api/class-schedule/semester/${this.selectedSemester.semesterId}`);

        if (!response.ok) {
          throw new Error('加载失败');
        }

        const data = await response.json();
        this.schedules = data;

        // 统计数据
        this.calculateStats();

      } catch (error) {
        console.error('加载失败:', error);
        if (typeof uni !== 'undefined') {
          uni.showToast({
            title: '加载失败',
            icon: 'none'
          });
        }
      } finally {
        this.loading = false;
      }
    },

    // 计算统计数据
    calculateStats() {
      this.stats.total = this.schedules.length;
      this.stats.saturday = this.schedules.filter(s => s.sourceWeekday === '周六').length;
      this.stats.sunday = this.schedules.filter(s => s.sourceWeekday === '周日').length;
    },

    // 确认对话框
    confirm(message) {
      return new Promise((resolve) => {
        if (typeof uni !== 'undefined') {
          uni.showModal({
            title: '确认操作',
            content: message,
            success: (res) => {
              resolve(res.confirm);
            }
          });
        } else {
          resolve(window.confirm(message));
        }
      });
    },

    // 格式化日期
    formatDate(dateStr) {
      const date = new Date(dateStr);
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
      const weekday = weekdays[date.getDay()];

      return `${month}-${day} ${weekday}`;
    },

    // 加载学期列表
    async loadSemesters() {
      try {
        const response = await fetch('http://localhost:8080/api/semester');
        if (response.ok) {
          const data = await response.json();
          this.allSemesters = data;

          // 自动选择寒假班
          if (data && data.length > 0) {
            const winter = data.find(s => s.semesterType === '寒假班');
            if (winter) {
              const index = data.findIndex(s => s.semesterId === winter.semesterId);
              this.onSemesterChange({ detail: { value: index } });
            }
          }
        }
      } catch (e) {
        console.error('加载学期失败:', e);
      }
    }
  },

  onLoad() {
    this.loadSemesters();
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

.semester-selector {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 15px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  display: flex;
  align-items: center;
}

.selector-label {
  font-size: 14px;
  color: #333;
  font-weight: bold;
  margin-right: 10px;
  white-space: nowrap;
}

.semester-picker {
  flex: 1;
}

.picker-view {
  height: 40px;
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

.placeholder {
  color: #999;
}

.arrow {
  color: #999;
  font-size: 18px;
}

.action-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.btn {
  flex: 1;
  height: 50px;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: bold;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.btn-generate {
  background: #FF9800;
  color: white;
}

.btn-view {
  background: rgba(255, 255, 255, 0.95);
  color: #667eea;
}

.stats {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
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
  color: #FF9800;
}

.stat-label {
  font-size: 12px;
  color: #666;
  margin-top: 5px;
}

.info-box {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 15px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.info-title {
  font-size: 16px;
  font-weight: bold;
  color: #FF9800;
  margin-bottom: 10px;
}

.info-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-item {
  font-size: 13px;
  color: #666;
  line-height: 1.6;
}

.schedule-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  padding: 15px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.list-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #FF9800;
}

.schedule-item {
  padding: 12px;
  background: #f9f9f9;
  border-radius: 8px;
  margin-bottom: 10px;
  border-left: 3px solid #FF9800;
}

.item-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  flex-wrap: wrap;
}

.date-text {
  font-size: 15px;
  font-weight: bold;
  color: #333;
}

.weekday-tag {
  padding: 3px 8px;
  background: #667eea;
  color: white;
  border-radius: 4px;
  font-size: 12px;
}

.source-tag {
  padding: 3px 8px;
  background: #4CAF50;
  color: white;
  border-radius: 4px;
  font-size: 12px;
}

.item-content {
  display: flex;
  flex-direction: column;
  gap: 5px;
  font-size: 13px;
  color: #666;
}

.time-text, .classroom-text {
  line-height: 1.6;
}

.empty, .loading {
  text-align: center;
  padding: 40px;
  font-size: 16px;
}

.empty-text, .loading-text {
  color: white;
  display: block;
  margin-bottom: 10px;
}

.empty-hint {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}
</style>
