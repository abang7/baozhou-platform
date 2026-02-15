<template>
  <view class="container">
    <!-- 页面标题 -->
    <view class="title">👨‍🏫 教师课程表</view>

    <!-- 教师选择 -->
    <view class="teacher-selector">
      <text class="selector-label">选择教师：</text>
      <picker
        class="teacher-picker"
        mode="selector"
        :range="teacherOptions"
        :range-key="'label'"
        :value="selectedTeacherIndex"
        @change="onTeacherChange"
      >
        <view class="picker-view">
          <text v-if="selectedTeacher">{{ selectedTeacher.teacherName }}</text>
          <text v-else class="placeholder">请选择教师</text>
          <text class="arrow">›</text>
        </view>
      </picker>
    </view>

    <!-- 学期选择 -->
    <view class="semester-selector" v-if="selectedTeacher">
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
          <text v-else class="placeholder">全部学期</text>
          <text class="arrow">›</text>
        </view>
      </picker>
    </view>

    <!-- 加载中 -->
    <view v-if="loading" class="loading">
      <text class="loading-text">⏳ 正在加载课程表...</text>
    </view>

    <!-- 错误提示 -->
    <view v-else-if="error" class="error">
      <text class="error-text">❌ {{ error }}</text>
    </view>

    <!-- 未选择教师 -->
    <view v-else-if="!selectedTeacher" class="empty">
      <text class="empty-text">👈 请先选择教师</text>
    </view>

    <!-- 空数据 -->
    <view v-else-if="scheduleList.length === 0" class="empty">
      <text class="empty-text">📭 该教师暂无排课数据</text>
      <text class="empty-hint">请先在"排课管理"中生成课程表</text>
    </view>

    <!-- 课程列表 -->
    <view v-else class="schedule-list">
      <!-- 统计信息 -->
      <view class="stats">
        <view class="stat-item">
          <view class="stat-value">{{ scheduleList.length }}</view>
          <view class="stat-label">总课程数</view>
        </view>
        <view class="stat-item">
          <view class="stat-value">{{ totalStudents }}</view>
          <view class="stat-label">授课学生</view>
        </view>
        <view class="stat-item">
          <view class="stat-value">{{ uniqueDates }}</view>
          <view class="stat-label">上课天数</view>
        </view>
      </view>

      <!-- 按日期分组显示 -->
      <view
        v-for="(group, date) in groupedSchedules"
        :key="date"
        class="date-group"
      >
        <view class="date-header">
          <text class="date-title">{{ formatDate(date) }}</text>
          <text class="course-count">{{ group.length }}节课</text>
        </view>

        <!-- 课程卡片 -->
        <view
          v-for="item in group"
          :key="item.scheduleId"
          class="schedule-card"
        >
          <!-- 卡片头部 -->
          <view class="card-header">
            <view class="card-title">
              <text class="subject-tag">{{ item.subject }}</text>
              <text class="time-text">{{ item.startTime }}-{{ item.endTime }}</text>
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
              <text class="info-icon">🏫</text>
              <text class="info-text">教室：{{ item.classroom || '-' }} ({{ item.floor || '-' }})</text>
            </view>
            <view class="info-row" v-if="item.sourceWeekday">
              <text class="info-icon">📅</text>
              <text class="info-text">来自{{ item.sourceWeekday }}课表</text>
            </view>
          </view>

          <!-- 学生列表 -->
          <view v-if="item.students && item.students.length > 0" class="student-section">
            <view class="student-title">📋 学生名单：</view>
            <view class="student-list">
              <view
                v-for="student in item.students"
                :key="student.studentId"
                class="student-item"
                :class="{ 'on-leave': student.isOnLeave }"
              >
                <view class="student-info-left">
                  <view class="student-name">{{ student.studentName }}</view>
                  <view class="student-info">{{ student.grade }}年级</view>
                </view>
                <view v-if="student.isOnLeave" class="leave-badge">
                  <text class="leave-icon">🏖️</text>
                  <text class="leave-text">请假</text>
                </view>
              </view>
            </view>
          </view>
          <view v-else class="student-section">
            <view class="student-empty">暂无学生</view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      // 所有教师列表
      allTeachers: [],

      // 所有学期列表
      allSemesters: [],

      // 选中的教师
      selectedTeacher: null,
      selectedTeacherIndex: -1,

      // 选中的学期
      selectedSemester: null,
      selectedSemesterIndex: 0,

      // 课程数据
      scheduleList: [],

      // 加载状态
      loading: false,
      error: ''
    }
  },

  computed: {
    // 教师选项（用于picker）
    teacherOptions() {
      return this.allTeachers.map(t => ({
        label: t.teacherName,
        value: t.teacherId
      }));
    },

    // 学期选项
    semesterOptions() {
      const options = [{ label: '全部学期', value: null }];
      this.allSemesters.forEach(s => {
        options.push({
          label: s.semesterName,
          value: s.semesterId
        });
      });
      return options;
    },

    // 统计学生数
    totalStudents() {
      const studentIds = new Set();
      this.scheduleList.forEach(item => {
        if (item.students) {
          item.students.forEach(s => studentIds.add(s.studentId));
        }
      });
      return studentIds.size;
    },

    // 唯一日期数
    uniqueDates() {
      const dates = new Set(this.scheduleList.map(item => item.courseDate));
      return dates.size;
    },

    // 按日期分组
    groupedSchedules() {
      const groups = {};
      this.scheduleList.forEach(item => {
        const date = item.courseDate;
        if (!groups[date]) {
          groups[date] = [];
        }
        groups[date].push(item);
      });
      return groups;
    }
  },

  methods: {
    // 教师选择变化
    onTeacherChange(e) {
      const index = e.detail.value;
      this.selectedTeacherIndex = index;
      this.selectedTeacher = this.allTeachers[index];
      console.log('选择教师:', this.selectedTeacher.teacherName);

      // 加载该教师的课程表
      this.loadSchedule();
    },

    // 学期选择变化
    onSemesterChange(e) {
      const index = e.detail.value;
      this.selectedSemesterIndex = index;

      if (index === 0) {
        // 全部学期
        this.selectedSemester = null;
      } else {
        this.selectedSemester = this.allSemesters[index - 1];
      }

      // 重新加载课程表
      this.loadSchedule();
    },

    // 加载教师列表
    async loadTeachers() {
      try {
        const response = await fetch('http://localhost:8080/api/teacher');
        if (!response.ok) {
          throw new Error('加载教师列表失败');
        }
        const data = await response.json();
        this.allTeachers = data;
      } catch (e) {
        console.error('加载教师列表失败:', e);
      }
    },

    // 加载学期列表
    async loadSemesters() {
      try {
        const response = await fetch('http://localhost:8080/api/semester');
        if (!response.ok) {
          throw new Error('加载学期列表失败');
        }
        const data = await response.json();
        this.allSemesters = data;

        // 自动选择当前学期
        const current = data.find(s => s.status === '进行中');
        if (current) {
          const index = data.findIndex(s => s.semesterId === current.semesterId);
          this.selectedSemesterIndex = index + 1; // +1 因为第一个是"全部学期"
          this.selectedSemester = current;
        }
      } catch (e) {
        console.error('加载学期列表失败:', e);
      }
    },

    // 加载课程表数据
    async loadSchedule() {
      if (!this.selectedTeacher) {
        return;
      }

      this.loading = true;
      this.error = '';

      try {
        // 构建查询参数
        let url = `http://localhost:8080/api/class-schedule/teacher-data/${this.selectedTeacher.teacherId}`;
        if (this.selectedSemester) {
          url += `?semesterId=${this.selectedSemester.semesterId}`;
        }

        const response = await fetch(url);
        if (!response.ok) {
          throw new Error('网络响应失败');
        }
        const data = await response.json();
        this.scheduleList = data;
      } catch (e) {
        console.error('加载失败:', e);
        this.error = '加载课程表失败，请确保后端服务已启动且已生成排课';
      } finally {
        this.loading = false;
      }
    },

    // 格式化日期
    formatDate(dateStr) {
      const date = new Date(dateStr);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
      const weekday = weekdays[date.getDay()];

      return `${year}-${month}-${day} ${weekday}`;
    }
  },

  // 页面加载时自动获取数据
  onLoad() {
    this.loadTeachers();
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

.teacher-selector, .semester-selector {
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

.teacher-picker, .semester-picker {
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
  color: #FF9800;
}

.stat-label {
  font-size: 12px;
  color: #666;
  margin-top: 5px;
}

.schedule-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.date-group {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  padding: 15px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.date-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #FF9800;
}

.date-title {
  font-size: 18px;
  font-weight: bold;
  color: #FF9800;
}

.course-count {
  font-size: 14px;
  color: #666;
}

.schedule-card {
  background: white;
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 15px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
}

.schedule-card:last-child {
  margin-bottom: 0;
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
  margin-bottom: 10px;
}

.student-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.student-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background: white;
  border-radius: 5px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
}

.student-item.on-leave {
  background: #fff5f5;
  border-color: #ffcdd2;
  opacity: 0.85;
}

.student-info-left {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.student-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.student-info {
  font-size: 12px;
  color: #666;
}

.leave-badge {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 10px;
  background: #FFEBEE;
  border-radius: 12px;
}

.leave-icon {
  font-size: 14px;
}

.leave-text {
  font-size: 12px;
  color: #F44336;
  font-weight: bold;
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
  display: block;
  margin-bottom: 10px;
}

.empty-hint {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}

.error-text {
  color: #fff;
}
</style>