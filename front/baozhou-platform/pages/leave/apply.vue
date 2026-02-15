<template>
  <view class="container">
    <!-- 页面标题 -->
    <view class="title">📝 学生请假申请</view>

    <!-- 学期选择 -->
    <view class="form">
      <view class="form-item">
        <text class="label">选择学期 <text class="required">*</text></text>
        <picker
          mode="selector"
          :range="semesterOptions"
          :range-key="'label'"
          :value="selectedSemesterIndex"
          @change="onSemesterChange"
        >
          <view class="picker">
            <text v-if="selectedSemester">{{ selectedSemester.semesterName }}</text>
            <text v-else class="placeholder">请选择学期</text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>

      <!-- 学生选择 -->
      <view class="form-item">
        <text class="label">选择学生 <text class="required">*</text></text>
        <picker
          mode="selector"
          :range="studentOptions"
          :range-key="'label'"
          :value="selectedStudentIndex"
          @change="onStudentChange"
        >
          <view class="picker">
            <text v-if="selectedStudent">{{ selectedStudent.studentName }}</text>
            <text v-else class="placeholder">请选择学生</text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>

      <!-- 请假日期 -->
      <view class="form-item">
        <text class="label">请假开始日期 <text class="required">*</text></text>
        <picker
          mode="date"
          :value="formData.startDate"
          @change="onStartDateChange"
        >
          <view class="picker">
            <text>{{ formData.startDate || '请选择开始日期' }}</text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="label">请假结束日期 <text class="required">*</text></text>
        <picker
          mode="date"
          :value="formData.endDate"
          @change="onEndDateChange"
        >
          <view class="picker">
            <text>{{ formData.endDate || '请选择结束日期' }}</text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>

      <!-- 请假类型 -->
      <view class="form-item">
        <text class="label">请假类型</text>
        <picker
          mode="selector"
          :range="leaveTypeOptions"
          @change="onLeaveTypeChange"
        >
          <view class="picker">
            <text>{{ formData.leaveType || '请选择请假类型' }}</text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>

      <!-- 请假原因 -->
      <view class="form-item">
        <text class="label">请假原因</text>
        <textarea
          class="textarea"
          v-model="formData.reason"
          placeholder="请输入请假原因"
          maxlength="200"
        ></textarea>
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="btn-group">
      <button class="btn btn-primary" @click="submit">提交申请</button>
      <button class="btn btn-secondary" @click="reset">重置</button>
    </view>

    <!-- 提示信息 -->
    <view class="tips">
      <text class="tips-text">提示：提交后等待管理员审核</text>
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

      // 所有学生
      allStudents: [],
      selectedStudent: null,
      selectedStudentIndex: -1,

      // 表单数据
      formData: {
        semesterId: null,
        studentId: null,
        startDate: '',
        endDate: '',
        leaveType: '其他',
        reason: ''
      },

      // 选项数据
      leaveTypeOptions: ['旅游', '探亲', '生病', '其他']
    }
  },

  computed: {
    semesterOptions() {
      return this.allSemesters.map(s => ({
        label: s.semesterName,
        value: s.semesterId
      }));
    },

    studentOptions() {
      return this.allStudents.map(s => ({
        label: s.studentName,
        value: s.studentId
      }));
    }
  },

  methods: {
    // 学期选择变化
    onSemesterChange(e) {
      const index = e.detail.value;
      this.selectedSemesterIndex = index;
      this.selectedSemester = this.allSemesters[index];
      this.formData.semesterId = this.selectedSemester.semesterId;
    },

    // 学生选择变化
    onStudentChange(e) {
      const index = e.detail.value;
      this.selectedStudentIndex = index;
      this.selectedStudent = this.allStudents[index];
      this.formData.studentId = this.selectedStudent.studentId;
    },

    // 开始日期变化
    onStartDateChange(e) {
      this.formData.startDate = e.detail.value;
    },

    // 结束日期变化
    onEndDateChange(e) {
      this.formData.endDate = e.detail.value;
    },

    // 请假类型变化
    onLeaveTypeChange(e) {
      const index = e.detail.value;
      this.formData.leaveType = this.leaveTypeOptions[index];
    },

    // 提交表单
    async submit() {
      // 验证表单
      if (!this.validate()) {
        return;
      }

      if (typeof uni !== 'undefined') {
        uni.showLoading({ title: '提交中...' });
      }

      try {
        const response = await fetch('http://localhost:8080/api/student-leave', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.formData)
        });

        if (!response.ok) {
          throw new Error('提交失败');
        }

        if (typeof uni !== 'undefined') {
          uni.hideLoading();
          uni.showToast({
            title: '提交成功',
            icon: 'success'
          });
        }

        this.reset();

      } catch (error) {
        console.error('提交失败:', error);
        if (typeof uni !== 'undefined') {
          uni.hideLoading();
          uni.showToast({
            title: '提交失败',
            icon: 'none'
          });
        }
      }
    },

    // 表单验证
    validate() {
      if (!this.formData.semesterId) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '请选择学期', icon: 'none' });
        }
        return false;
      }

      if (!this.formData.studentId) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '请选择学生', icon: 'none' });
        }
        return false;
      }

      if (!this.formData.startDate) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '请选择开始日期', icon: 'none' });
        }
        return false;
      }

      if (!this.formData.endDate) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '请选择结束日期', icon: 'none' });
        }
        return false;
      }

      if (this.formData.endDate < this.formData.startDate) {
        if (typeof uni !== 'undefined') {
          uni.showToast({ title: '结束日期不能早于开始日期', icon: 'none' });
        }
        return false;
      }

      return true;
    },

    // 重置表单
    reset() {
      this.formData = {
        semesterId: null,
        studentId: null,
        startDate: '',
        endDate: '',
        leaveType: '其他',
        reason: ''
      };
      this.selectedSemester = null;
      this.selectedSemesterIndex = -1;
      this.selectedStudent = null;
      this.selectedStudentIndex = -1;
    },

    // 加载学期列表
    async loadSemesters() {
      try {
        const response = await fetch('http://localhost:8080/api/semester');
        if (response.ok) {
          const data = await response.json();
          this.allSemesters = data;

          // 自动选择当前学期
          if (data && data.length > 0) {
            const current = data.find(s => s.status === '进行中');
            if (current) {
              const index = data.findIndex(s => s.semesterId === current.semesterId);
              this.onSemesterChange({ detail: { value: index } });
            }
          }
        }
      } catch (e) {
        console.error('加载学期失败:', e);
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
    }
  },

  onLoad() {
    this.loadSemesters();
    this.loadStudents();
  }
}
</script>

<style scoped>
.container {
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.form {
  background: white;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  margin-bottom: 20px;
}

.form-item {
  margin-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 15px;
}

.form-item:last-child {
  border-bottom: none;
}

.label {
  display: block;
  font-size: 14px;
  color: #333;
  font-weight: bold;
  margin-bottom: 10px;
}

.required {
  color: #ff4d4f;
}

.picker {
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 0 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  color: #333;
}

.placeholder {
  color: #999;
}

.arrow {
  color: #999;
  font-size: 20px;
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

.btn-group {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.btn {
  flex: 1;
  height: 44px;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  font-weight: bold;
}

.btn-primary {
  background: #FF9800;
  color: white;
}

.btn-secondary {
  background: #f0f0f0;
  color: #333;
}

.tips {
  text-align: center;
}

.tips-text {
  font-size: 12px;
  color: #999;
}
</style>
