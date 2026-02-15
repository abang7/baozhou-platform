<template>
  <view class="container">
    <view class="title">📚 录入课程</view>

    <!-- 表单 -->
    <view class="form">
      <!-- 教师选择 -->
      <view class="form-item">
        <text class="label">任课教师</text>
        <picker
          mode="selector"
          :range="teacherOptions"
          :range-key="'label'"
          :value="selectedTeacherIndex"
          @change="onTeacherChange"
        >
          <view class="picker-view">
            <text v-if="selectedTeacher" class="picker-text">{{ selectedTeacher.teacherName }}</text>
            <text v-else class="placeholder">请选择教师</text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>

      <!-- 星期选择 -->
      <view class="form-item">
        <text class="label">上课时间</text>
        <picker
          mode="selector"
          :range="weekdayOptions"
          @change="onWeekdayChange"
        >
          <view class="picker-view">
            <text class="picker-text">{{ form.weekday || '请选择' }}</text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>

      <!-- 时段选择 -->
      <view class="form-item">
        <text class="label">时间段</text>
        <picker
          mode="selector"
          :range="timeSlotOptions"
          @change="onTimeSlotChange"
        >
          <view class="picker-view">
            <text class="picker-text">{{ form.timeSlot || '请选择' }}</text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>

      <!-- 学科选择 -->
      <view class="form-item">
        <text class="label">学科</text>
        <picker
          mode="selector"
          :range="subjectOptions"
          @change="onSubjectChange"
        >
          <view class="picker-view">
            <text class="picker-text">{{ form.subject || '请选择' }}</text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>

      <!-- 班级类型 -->
      <view class="form-item">
        <text class="label">班级类型</text>
        <picker
          mode="selector"
          :range="classTypeOptions"
          @change="onClassTypeChange"
        >
          <view class="picker-view">
            <text class="picker-text">{{ form.classType || '请选择' }}</text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>

      <!-- 最大学生数 -->
      <view class="form-item">
        <text class="label">最大学生数</text>
        <input
          class="input"
          type="number"
          v-model="form.maxStudents"
          placeholder="根据班级类型自动设置"
          disabled
        />
      </view>

      <!-- 提交按钮 -->
      <button class="btn-submit" @click="submitForm">
        ✅ 确认录入
      </button>

      <!-- 返回按钮 -->
      <button class="btn-back" @click="goBack">
        ← 返回首页
      </button>
    </view>

    <!-- 已录入课程列表 -->
    <view class="class-list" v-if="classList.length > 0">
      <view class="list-title">已录入课程 ({{ classList.length }})</view>
      <view
        v-for="item in classList"
        :key="item.classId"
        class="class-card"
      >
        <view class="card-header">
          <text class="subject">{{ item.subject }}</text>
          <text class="weekday">{{ item.weekday }}</text>
        </view>
        <view class="card-body">
          <view class="info-row">
            <text class="info-label">教师：</text>
            <text class="info-value">{{ getTeacherName(item.teacherId) }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">时间：</text>
            <text class="info-value">{{ item.timeSlot }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">类型：</text>
            <text class="info-value">{{ item.classType }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">学生：</text>
            <text class="info-value">{{ item.currentStudents || 0 }}/{{ item.maxStudents }}</text>
          </view>
        </view>
        <view class="card-footer">
          <view
            class="status-badge"
            :class="{ active: item.isActive, inactive: !item.isActive }"
          >
            {{ item.isActive ? '生效中' : '已停用' }}
          </view>
          <view class="footer-buttons">
            <button
              v-if="item.isActive"
              class="btn-add-student"
              size="mini"
              @click="showAddStudentModal(item)"
            >
              + 添加学生
            </button>
            <button
              v-if="item.isActive"
              class="btn-delete"
              size="mini"
              @click="deleteClass(item.classId)"
            >
              删除
            </button>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-else class="empty">
      <text class="empty-text">暂无课程记录</text>
    </view>

    <!-- 添加学生弹窗 -->
    <view v-if="showStudentModal" class="modal-overlay" @click="closeStudentModal">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">添加学生到班级</text>
          <text class="modal-close" @click="closeStudentModal">✕</text>
        </view>

        <view class="modal-body">
          <view class="class-info">
            <text class="info-label">班级：</text>
            <text class="info-value">{{ selectedClass?.subject }} - {{ selectedClass?.weekday }} {{ selectedClass?.timeSlot }}</text>
          </view>
          <view class="class-info">
            <text class="info-label">教师：</text>
            <text class="info-value">{{ getTeacherName(selectedClass?.teacherId) }}</text>
          </view>
          <view class="class-info">
            <text class="info-label">容量：</text>
            <text class="info-value">{{ selectedClass?.currentStudents || 0 }}/{{ selectedClass?.maxStudents }}人</text>
          </view>
        </view>

        <view class="student-list-section">
          <view class="section-title">选择学生</view>
          <view v-if="availableStudents.length === 0" class="empty-students">
            <text class="empty-text">暂无可添加的学生</text>
          </view>
          <view v-else class="student-list">
            <view
              v-for="student in availableStudents"
              :key="student.studentId"
              class="student-item-modal"
              @click="addStudentToClass(student)"
            >
              <view class="student-info-modal">
                <text class="student-name-modal">{{ student.studentName }}</text>
                <text class="student-detail">{{ student.grade }}年级</text>
              </view>
              <text class="add-icon">+</text>
            </view>
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
      selectedTeacher: null,
      selectedTeacherIndex: -1,

      // 表单数据
      form: {
        teacherId: null,
        weekday: '周六',
        timeSlot: '07:40-09:40',
        subject: '数学',
        classType: '一对一',
        maxStudents: 1,
        currentStudents: 0,
        isActive: true
      },

      // 选项数据
      weekdayOptions: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
      timeSlotOptions: [
        '07:40-09:40',
        '09:40-11:40',
        '12:30-14:30',
        '14:30-16:30',
        '16:30-18:30',
        '18:50-20:50'
      ],
      subjectOptions: ['数学', '英语', '语文', '物理', '化学', '政治', '历史', '地理'],
      classTypeOptions: ['一对一', '一对三'],

      // 已录入的课程列表
      classList: [],

      // 所有学生列表
      allStudents: [],

      // 添加学生弹窗
      showStudentModal: false,
      selectedClass: null
    }
  },

  computed: {
    teacherOptions() {
      return this.allTeachers.map(t => ({
        label: t.teacherName,
        value: t.teacherId
      }));
    },

    // 可添加的学生列表（排除已经在该班级的学生）
    availableStudents() {
      if (!this.selectedClass) {
        return [];
      }

      // 获取该班级已有的学生ID列表
      const classStudents = this.classList.find(c => c.classId === this.selectedClass.classId)?.students || [];
      const existingStudentIds = classStudents.map(s => s.studentId);

      // 过滤掉已在班级中的学生
      return this.allStudents.filter(s => !existingStudentIds.includes(s.studentId));
    }
  },

  methods: {
    // 教师选择变化
    onTeacherChange(e) {
      const index = e.detail.value;
      this.selectedTeacherIndex = index;
      this.selectedTeacher = this.allTeachers[index];
      this.form.teacherId = this.selectedTeacher.teacherId;
    },

    // 星期选择变化
    onWeekdayChange(e) {
      const index = e.detail.value;
      this.form.weekday = this.weekdayOptions[index];
    },

    // 时段选择变化
    onTimeSlotChange(e) {
      const index = e.detail.value;
      this.form.timeSlot = this.timeSlotOptions[index];
    },

    // 学科选择变化
    onSubjectChange(e) {
      const index = e.detail.value;
      this.form.subject = this.subjectOptions[index];
    },

    // 班级类型选择变化
    onClassTypeChange(e) {
      const index = e.detail.value;
      this.form.classType = this.classTypeOptions[index];

      // 根据班级类型自动设置最大学生数
      this.form.maxStudents = this.form.classType === '一对一' ? 1 : 3;
    },

    // 提交表单
    async submitForm() {
      // 验证表单
      if (!this.form.teacherId) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '请选择教师', icon: 'none' });
        }
        return;
      }

      if (!this.form.weekday) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '请选择上课时间', icon: 'none' });
        }
        return;
      }

      if (!this.form.timeSlot) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '请选择时间段', icon: 'none' });
        }
        return;
      }

      if (!this.form.subject) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '请选择学科', icon: 'none' });
        }
        return;
      }

      if (!this.form.classType) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '请选择班级类型', icon: 'none' });
        }
        return;
      }

      if (typeof uni !== 'undefined') {
        uni.showLoading({ title: '提交中...' });
      }

      try {
        const response = await fetch('http://localhost:8080/api/class', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.form)
        });

        if (!response.ok) {
          throw new Error('提交失败');
        }

        const result = await response.json();

        if (typeof uni !== 'undefined') {
          uni.hideLoading();

          if (result.success) {
            uni.showToast({ title: '录入成功', icon: 'success' });

            // 重置表单
            this.resetForm();

            // 重新加载课程列表
            this.loadClassList();
          } else {
            // 显示错误信息
            uni.showToast({ title: result.message || '录入失败', icon: 'none' });
          }
        }

      } catch (error) {
        console.error('录入失败:', error);
        if (typeof uni !== 'undefined') {
          uni.hideLoading();
          uni.showToast({ title: '录入失败', icon: 'none' });
        }
      }
    },

    // 重置表单
    resetForm() {
      this.form = {
        teacherId: null,
        weekday: '周六',
        timeSlot: '07:40-09:40',
        subject: '数学',
        classType: '一对一',
        maxStudents: 1,
        currentStudents: 0,
        isActive: true
      };
      this.selectedTeacher = null;
      this.selectedTeacherIndex = -1;
    },

    // 删除课程
    async deleteClass(classId) {
      if (typeof uni !== 'undefined') {
        uni.showModal({
          title: '确认删除',
          content: '确定要删除这个课程吗？',
          success: async (res) => {
            if (res.confirm) {
              try {
                const response = await fetch(`http://localhost:8080/api/class/${classId}`, {
                  method: 'DELETE'
                });

                if (!response.ok) {
                  throw new Error('删除失败');
                }

                if (typeof uni !== 'undefined') {
                  uni.showToast({ title: '删除成功', icon: 'success' });
                }

                // 重新加载课程列表
                this.loadClassList();

              } catch (error) {
                console.error('删除失败:', error);
                if (typeof uni !== 'undefined') {
                  uni.showToast({ title: '删除失败', icon: 'none' });
                }
              }
            }
          }
        });
      }
    },

    // 加载教师列表
    async loadTeachers() {
      try {
        const response = await fetch('http://localhost:8080/api/teacher');
        if (response.ok) {
          const data = await response.json();
          this.allTeachers = data;
        }
      } catch (e) {
        console.error('加载教师列表失败:', e);
      }
    },

    // 加载课程列表
    async loadClassList() {
      try {
        // 使用 schedule 接口获取包含学生信息的完整数据
        const response = await fetch('http://localhost:8080/api/class/schedule');
        if (response.ok) {
          const data = await response.json();

          // 转换 ScheduleDTO 为 ClassInfo 格式，并添加学生列表
          this.classList = data.map(item => ({
            classId: item.classId,
            teacherId: item.teacherId,
            weekday: item.weekday,
            timeSlot: item.timeSlot,
            subject: item.subject,
            classType: item.classType,
            maxStudents: item.maxStudents,
            currentStudents: item.currentStudents,
            isActive: item.isActive,
            students: item.students || [] // 添加学生列表
          }));
        }
      } catch (e) {
        console.error('加载课程列表失败:', e);
      }
    },

    // 获取教师名称
    getTeacherName(teacherId) {
      const teacher = this.allTeachers.find(t => t.teacherId === teacherId);
      return teacher ? teacher.teacherName : '未知';
    },

    // 返回首页
    goBack() {
      if (typeof uni !== 'undefined') {
        uni.navigateBack();
      }
    },

    // 显示添加学生弹窗
    showAddStudentModal(classInfo) {
      // 检查班级是否已满
      if (classInfo.currentStudents >= classInfo.maxStudents) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '班级人数已满', icon: 'none' });
        }
        return;
      }

      this.selectedClass = classInfo;
      this.showStudentModal = true;
    },

    // 关闭添加学生弹窗
    closeStudentModal() {
      this.showStudentModal = false;
      this.selectedClass = null;
    },

    // 添加学生到班级
    async addStudentToClass(student) {
      if (!this.selectedClass) {
        return;
      }

      if (typeof uni !== 'undefined') {
        uni.showLoading({ title: '添加中...' });
      }

      try {
        const classStudentData = {
          classId: this.selectedClass.classId,
          studentId: student.studentId,
          status: '在读'
        };

        const response = await fetch('http://localhost:8080/api/class-student', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(classStudentData)
        });

        if (!response.ok) {
          throw new Error('添加失败');
        }

        const result = await response.json();

        if (typeof uni !== 'undefined') {
          uni.hideLoading();

          if (result.success) {
            uni.showToast({ title: '添加成功', icon: 'success' });

            // 关闭弹窗
            this.closeStudentModal();

            // 重新加载课程列表（会更新学生数量）
            this.loadClassList();
          } else {
            // 显示错误信息
            uni.showToast({ title: result.message || '添加失败', icon: 'none' });
          }
        }

      } catch (error) {
        console.error('添加学生失败:', error);
        if (typeof uni !== 'undefined') {
          uni.hideLoading();
          uni.showToast({ title: '添加失败', icon: 'none' });
        }
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
        console.error('加载学生列表失败:', e);
      }
    }
  },

  onLoad() {
    this.loadTeachers();
    this.loadStudents();
    this.loadClassList();
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  padding-bottom: 40px;
}

.title {
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  color: white;
  margin-bottom: 30px;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
}

.form {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 15px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.form-item {
  margin-bottom: 20px;
}

.label {
  display: block;
  font-size: 14px;
  color: #333;
  font-weight: bold;
  margin-bottom: 10px;
}

.picker-view {
  height: 45px;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 0 15px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 15px;
  background: white;
}

.picker-text {
  color: #333;
}

.placeholder {
  color: #999;
}

.arrow {
  color: #999;
  font-size: 20px;
}

.input {
  height: 45px;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 0 15px;
  font-size: 15px;
  background: #f5f5f5;
  color: #666;
}

.btn-submit {
  width: 100%;
  height: 50px;
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: bold;
  margin-top: 10px;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.3);
}

.btn-submit:active {
  opacity: 0.8;
}

.btn-back {
  width: 100%;
  height: 45px;
  background: transparent;
  color: #666;
  border: 1px solid #ddd;
  border-radius: 10px;
  font-size: 15px;
  margin-top: 10px;
}

.class-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.list-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #2196F3;
}

.class-card {
  background: white;
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 15px;
  border-left: 4px solid #2196F3;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.subject {
  font-size: 16px;
  font-weight: bold;
  color: #2196F3;
}

.weekday {
  font-size: 14px;
  color: #FF9800;
  font-weight: bold;
  padding: 3px 8px;
  background: #FFF3E0;
  border-radius: 4px;
}

.card-body {
  margin-bottom: 12px;
}

.info-row {
  display: flex;
  margin-bottom: 6px;
  font-size: 14px;
}

.info-label {
  color: #666;
  width: 60px;
}

.info-value {
  color: #333;
  font-weight: 500;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #eee;
}

.status-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
}

.status-badge.active {
  background: #E8F5E9;
  color: #4CAF50;
}

.status-badge.inactive {
  background: #FFEBEE;
  color: #F44336;
}

.btn-delete {
  background: #F44336;
  color: white;
  border: none;
  font-size: 12px;
}

.empty {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 15px;
  padding: 40px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.empty-text {
  font-size: 16px;
  color: #999;
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
  max-width: 500px;
  max-height: 80vh;
  background: white;
  border-radius: 15px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
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
  background: #f5f5f5;
}

.class-info {
  display: flex;
  margin-bottom: 10px;
  font-size: 14px;
}

.info-label {
  color: #666;
  width: 50px;
  font-weight: bold;
}

.info-value {
  flex: 1;
  color: #333;
}

.student-list-section {
  flex: 1;
  overflow-y: auto;
  padding: 0 20px 20px;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
  padding: 10px 0;
  border-bottom: 2px solid #2196F3;
}

.student-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.student-item-modal {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 15px;
  background: white;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
}

.student-item-modal:active {
  background: #f5f5f5;
  transform: scale(0.98);
}

.student-info-modal {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.student-name-modal {
  font-size: 15px;
  font-weight: bold;
  color: #333;
}

.student-detail {
  font-size: 13px;
  color: #666;
}

.add-icon {
  font-size: 24px;
  color: #2196F3;
  font-weight: bold;
}

.empty-students {
  text-align: center;
  padding: 40px 20px;
}

.footer-buttons {
  display: flex;
  gap: 8px;
}

.btn-add-student {
  background: #4CAF50;
  color: white;
  border: none;
  font-size: 12px;
}
</style>
