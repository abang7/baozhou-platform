<template>
  <view class="container">
    <!-- 页面标题 -->
    <view class="title">📅 学生日历</view>

    <!-- 学生选择 -->
    <view class="selector-row">
      <text class="label">选择学生：</text>
      <picker
        class="picker"
        mode="selector"
        :range="studentOptions"
        :range-key="'label'"
        :value="selectedStudentIndex"
        @change="onStudentChange"
      >
        <view class="picker-view">
          <text v-if="selectedStudent">{{ selectedStudent.studentName }}</text>
          <text v-else class="placeholder">请选择学生</text>
          <text class="arrow">›</text>
        </view>
      </picker>
    </view>

    <!-- 学期选择 -->
    <view class="selector-row">
      <text class="label">选择学期：</text>
      <picker
        class="picker"
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

    <!-- 加载中 -->
    <view v-if="loading" class="loading">
      <text class="loading-text">⏳ 正在加载课程表...</text>
    </view>

    <!-- 错误提示 -->
    <view v-else-if="error" class="error">
      <text class="error-text">❌ {{ error }}</text>
    </view>

    <!-- 未选择学生 -->
    <view v-else-if="!selectedStudent" class="empty">
      <text class="empty-text">👈 请先选择学生</text>
    </view>

    <!-- 空数据 -->
    <view v-else-if="calendarData.length === 0" class="empty">
      <text class="empty-text">📭 该学生暂无课程安排</text>
    </view>

    <!-- 日历视图 -->
    <view v-else class="calendar-container">
      <!-- 统计信息 -->
      <view class="stats">
        <view class="stat-item">
          <view class="stat-value">{{ calendarData.length }}</view>
          <view class="stat-label">总课程数</view>
        </view>
        <view class="stat-item">
          <view class="stat-value">{{ leaveCount }}</view>
          <view class="stat-label">请假天数</view>
        </view>
        <view class="stat-item">
          <view class="stat-value">{{ uniqueDates }}</view>
          <view class="stat-label">上课天数</view>
        </view>
      </view>

      <!-- 按日期分组 -->
      <view
        v-for="(group, date) in groupedCalendar"
        :key="date"
        class="date-group"
      >
        <view class="date-header">
          <text class="date-title">{{ formatDate(date) }}</text>
        </view>

        <!-- 课程卡片列表 -->
        <view class="course-list">
          <view
            v-for="course in group"
            :key="course.scheduleId"
            class="course-card"
            :class="{ 'on-leave': course.isOnLeave }"
            @click="handleCourseClick(course)"
          >
            <!-- 时间和学科 -->
            <view class="course-header">
              <text class="time-text">{{ course.startTime }}-{{ course.endTime }}</text>
              <text class="subject-tag">{{ course.subject }}</text>
            </view>

            <!-- 教师和教室 -->
            <view class="course-info">
              <text class="info-text">👨‍🏫 {{ course.teacherName }}</text>
              <text class="info-text">🏫 {{ course.classroom }}({{ course.floor }})</text>
            </view>

            <!-- 请假状态 -->
            <view v-if="course.isOnLeave" class="leave-badge">
              <text class="leave-icon">🏖️</text>
              <text class="leave-text">已请假：{{ course.leaveReason || '无原因' }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 请假弹窗 -->
    <view v-if="showLeaveModal" class="modal-overlay" @click="closeModal">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">设置请假</text>
          <text class="modal-close" @click="closeModal">✕</text>
        </view>

        <view class="modal-body">
          <view class="course-detail">
            <text class="detail-label">日期：</text>
            <text class="detail-value">{{ formatDate(selectedCourse?.courseDate) }}</text>
          </view>
          <view class="course-detail">
            <text class="detail-label">时间：</text>
            <text class="detail-value">{{ selectedCourse?.startTime }}-{{ selectedCourse?.endTime }}</text>
          </view>
          <view class="course-detail">
            <text class="detail-label">课程：</text>
            <text class="detail-value">{{ selectedCourse?.subject }}</text>
          </view>
        </view>

        <view class="modal-form">
          <view class="form-item">
            <text class="form-label">请假类型：</text>
            <picker
              mode="selector"
              :range="leaveTypeOptions"
              @change="onLeaveTypeChange"
            >
              <view class="picker-input">
                <text>{{ leaveForm.leaveType || '请选择' }}</text>
                <text class="arrow">›</text>
              </view>
            </picker>
          </view>

          <view class="form-item">
            <text class="form-label">请假原因：</text>
            <textarea
              class="textarea"
              v-model="leaveForm.reason"
              placeholder="请输入请假原因"
              maxlength="200"
            ></textarea>
          </view>
        </view>

        <view class="modal-actions">
          <button class="btn btn-cancel" @click="closeModal">取消</button>
          <button
            v-if="!selectedCourse?.isOnLeave"
            class="btn btn-confirm"
            @click="submitLeave"
          >
            确认请假
          </button>
          <button
            v-else
            class="btn btn-remove"
            @click="cancelLeave"
          >
            取消请假
          </button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      // 所有学生
      allStudents: [],
      selectedStudent: null,
      selectedStudentIndex: -1,

      // 所有学期
      allSemesters: [],
      selectedSemester: null,
      selectedSemesterIndex: -1,

      // 日历数据
      calendarData: [],

      // 加载状态
      loading: false,
      error: '',

      // 请假弹窗
      showLeaveModal: false,
      selectedCourse: null,
      leaveForm: {
        leaveType: '其他',
        reason: ''
      },
      leaveTypeOptions: ['旅游', '探亲', '生病', '其他']
    }
  },

  computed: {
    studentOptions() {
      return this.allStudents.map(s => ({
        label: s.studentName,
        value: s.studentId
      }));
    },

    semesterOptions() {
      return this.allSemesters.map(s => ({
        label: s.semesterName,
        value: s.semesterId
      }));
    },

    // 请假课程数
    leaveCount() {
      return this.calendarData.filter(c => c.isOnLeave).length;
    },

    // 唯一日期数
    uniqueDates() {
      const dates = new Set(this.calendarData.map(c => c.courseDate));
      return dates.size;
    },

    // 按日期分组
    groupedCalendar() {
      const groups = {};
      this.calendarData.forEach(course => {
        const date = course.courseDate;
        if (!groups[date]) {
          groups[date] = [];
        }
        groups[date].push(course);
      });
      return groups;
    }
  },

  methods: {
    // 学生选择变化
    onStudentChange(e) {
      const index = e.detail.value;
      this.selectedStudentIndex = index;
      this.selectedStudent = this.allStudents[index];
      this.loadCalendar();
    },

    // 学期选择变化
    onSemesterChange(e) {
      const index = e.detail.value;
      this.selectedSemesterIndex = index;
      this.selectedSemester = this.allSemesters[index];
      this.loadCalendar();
    },

    // 请假类型选择
    onLeaveTypeChange(e) {
      const index = e.detail.value;
      this.leaveForm.leaveType = this.leaveTypeOptions[index];
    },

    // 点击课程
    handleCourseClick(course) {
      this.selectedCourse = course;

      if (course.isOnLeave) {
        // 已请假，显示取消请假确认
        this.showLeaveModal = true;
      } else {
        // 未请假，显示请假表单
        this.leaveForm = {
          leaveType: '其他',
          reason: ''
        };
        this.showLeaveModal = true;
      }
    },

    // 关闭弹窗
    closeModal() {
      this.showLeaveModal = false;
      this.selectedCourse = null;
    },

    // 提交请假
    async submitLeave() {
      if (!this.leaveForm.leaveType) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '请选择请假类型', icon: 'none' });
        }
        return;
      }

      if (typeof uni !== 'undefined') {
        uni.showLoading({ title: '提交中...' });
      }

      try {
        const leaveData = {
          studentId: this.selectedStudent.studentId,
          semesterId: this.selectedSemester.semesterId,
          startDate: this.selectedCourse.courseDate,
          endDate: this.selectedCourse.courseDate,
          leaveType: this.leaveForm.leaveType,
          reason: this.leaveForm.reason,
          status: '待审核'
        };

        const response = await fetch('http://localhost:8080/api/student-leave', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(leaveData)
        });

        if (!response.ok) {
          throw new Error('提交失败');
        }

        if (typeof uni !== 'undefined') {
          uni.hideLoading();
          uni.showToast({ title: '请假成功', icon: 'success' });
        }

        this.closeModal();
        this.loadCalendar();

      } catch (error) {
        console.error('请假失败:', error);
        if (typeof uni !== 'undefined') {
          uni.hideLoading();
          uni.showToast({ title: '请假失败', icon: 'none' });
        }
      }
    },

    // 取消请假
    async cancelLeave() {
      if (typeof uni !== 'undefined') {
        uni.showModal({
          title: '确认',
          content: '确定要取消请假吗？',
          success: async (res) => {
            if (res.confirm) {
              // TODO: 调用取消请假API
              if (typeof uni !== 'undefined') {
                uni.showToast({ title: '功能开发中', icon: 'none' });
              }
            }
          }
        });
      }
    },

    // 加载日历数据
    async loadCalendar() {
      if (!this.selectedStudent || !this.selectedSemester) {
        return;
      }

      this.loading = true;
      this.error = '';

      try {
        const url = `http://localhost:8080/api/class-schedule/student-data/${this.selectedStudent.studentId}?semesterId=${this.selectedSemester.semesterId}`;

        const response = await fetch(url);
        if (!response.ok) {
          throw new Error('网络响应失败');
        }

        const data = await response.json();
        this.calendarData = data;

      } catch (e) {
        console.error('加载失败:', e);
        this.error = '加载课程表失败';
      } finally {
        this.loading = false;
      }
    },

    // 加载学生列表
    async loadStudents() {
      try {
        const response = await fetch('http://localhost:8080/api/student');
        if (response.ok) {
          const data = await response.json();
          this.allStudents = data;
        }
      } catch (e) {
        console.error('加载学生失败:', e);
      }
    },

    // 加载学期列表
    async loadSemesters() {
      try {
        const response = await fetch('http://localhost:8080/api/semester');
        if (response.ok) {
          const data = await response.json();
          this.allSemesters = data;

          // 自动选择当前学期
          const current = data.find(s => s.status === '进行中');
          if (current) {
            const index = data.findIndex(s => s.semesterId === current.semesterId);
            this.selectedSemesterIndex = index;
            this.selectedSemester = current;
          }
        }
      } catch (e) {
        console.error('加载学期失败:', e);
      }
    },

    // 格式化日期
    formatDate(dateStr) {
      const date = new Date(dateStr);
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
      const weekday = weekdays[date.getDay()];

      return `${month}-${day} ${weekday}`;
    }
  },

  onLoad() {
    this.loadStudents();
    this.loadSemesters();
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  padding-bottom: 100px;
}

.title {
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  color: white;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
}

.selector-row {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 15px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  display: flex;
  align-items: center;
}

.label {
  font-size: 14px;
  color: #333;
  font-weight: bold;
  margin-right: 10px;
  white-space: nowrap;
}

.picker {
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
  gap: 10px;
  margin-bottom: 15px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-value {
  font-size: 22px;
  font-weight: bold;
  color: #FF9800;
}

.stat-label {
  font-size: 11px;
  color: #666;
  margin-top: 3px;
}

.calendar-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.date-group {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  padding: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.date-header {
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 2px solid #FF9800;
}

.date-title {
  font-size: 16px;
  font-weight: bold;
  color: #FF9800;
}

.course-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.course-card {
  background: white;
  border-radius: 8px;
  padding: 12px;
  border-left: 4px solid #4CAF50;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
}

.course-card:active {
  transform: scale(0.98);
}

.course-card.on-leave {
  border-left-color: #FF5722;
  background: #fff5f5;
  opacity: 0.8;
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.time-text {
  font-size: 15px;
  font-weight: bold;
  color: #333;
}

.subject-tag {
  padding: 3px 8px;
  background: #FF9800;
  color: white;
  border-radius: 4px;
  font-size: 12px;
}

.course-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-bottom: 8px;
}

.info-text {
  font-size: 13px;
  color: #666;
}

.leave-badge {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 6px 10px;
  background: #FFEBEE;
  border-radius: 5px;
}

.leave-icon {
  font-size: 14px;
}

.leave-text {
  font-size: 12px;
  color: #F44336;
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal-content {
  width: 85%;
  max-width: 400px;
  background: white;
  border-radius: 15px;
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  background: #FF9800;
}

.modal-title {
  font-size: 18px;
  font-weight: bold;
  color: white;
}

.modal-close {
  font-size: 24px;
  color: white;
  padding: 0 5px;
}

.modal-body {
  padding: 15px 20px;
}

.course-detail {
  display: flex;
  margin-bottom: 12px;
  font-size: 14px;
}

.detail-label {
  width: 60px;
  color: #666;
  font-weight: bold;
}

.detail-value {
  flex: 1;
  color: #333;
}

.modal-form {
  margin-top: 15px;
}

.form-item {
  margin-bottom: 15px;
}

.form-label {
  display: block;
  font-size: 14px;
  color: #333;
  font-weight: bold;
  margin-bottom: 8px;
}

.picker-input {
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 0 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  background: white;
}

.textarea {
  width: 100%;
  min-height: 80px;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 10px;
  font-size: 14px;
  box-sizing: border-box;
}

.modal-actions {
  display: flex;
  gap: 10px;
  padding: 15px 20px;
  border-top: 1px solid #eee;
}

.btn {
  flex: 1;
  height: 40px;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  font-weight: bold;
}

.btn-cancel {
  background: #f0f0f0;
  color: #666;
}

.btn-confirm {
  background: #FF9800;
  color: white;
}

.btn-remove {
  background: #F44336;
  color: white;
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
