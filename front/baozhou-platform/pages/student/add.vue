<template>
  <view class="container">
    <!-- 页面标题 -->
    <view class="title">📝 录入学生信息</view>

    <!-- 表单区域 -->
    <view class="form">
      <!-- 学生姓名 -->
      <view class="form-item">
        <text class="label">学生姓名 <text class="required">*</text></text>
        <input
          class="input"
          v-model="formData.studentName"
          placeholder="请输入学生姓名"
        />
      </view>

      <!-- 性别 -->
      <view class="form-item">
        <text class="label">性别</text>
        <radio-group @change="onGenderChange">
          <label class="radio">
            <radio value="男" :checked="formData.sex === '男'" />
            <text>男</text>
          </label>
          <label class="radio">
            <radio value="女" :checked="formData.sex === '女'" />
            <text>女</text>
          </label>
        </radio-group>
      </view>

      <!-- 学段（必选，先选学段再选年级） -->
      <view class="form-item">
        <text class="label">学段 <text class="required">*</text></text>
        <picker
          mode="selector"
          :range="levelOptions"
          @change="onLevelChange"
        >
          <view class="picker">
            {{ formData.level || '请选择学段' }}
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>

      <!-- 年级（必选，根据学段显示对应年级） -->
      <view class="form-item">
        <text class="label">年级 <text class="required">*</text></text>
        <picker
          mode="selector"
          :range="gradeOptions"
          :disabled="!formData.level"
          @change="onGradeChange"
        >
          <view class="picker" :class="{ disabled: !formData.level }">
            {{ gradeDisplayText }}
            <text class="arrow">›</text>
          </view>
        </picker>
        <text class="hint" v-if="!formData.level">请先选择学段</text>
      </view>

      <!-- 家长姓名 -->
      <view class="form-item">
        <text class="label">家长姓名</text>
        <input
          class="input"
          v-model="formData.parentName"
          placeholder="请输入家长姓名"
        />
      </view>

      <!-- 家长手机号 -->
      <view class="form-item">
        <text class="label">家长手机号</text>
        <input
          class="input"
          v-model="formData.parentPhone"
          type="number"
          maxlength="11"
          placeholder="请输入手机号"
        />
      </view>

      <!-- 状态 -->
      <view class="form-item">
        <text class="label">状态</text>
        <picker
          mode="selector"
          :range="statusOptions"
          @change="onStatusChange"
        >
          <view class="picker">
            {{ formData.status || '请选择状态' }}
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="btn-group">
      <button class="btn btn-primary" @click="submit">提交</button>
      <button class="btn btn-secondary" @click="reset">重置</button>
    </view>

    <!-- 提示信息 -->
    <view class="tips">
      <text class="tips-text">提示：带 * 的为必填项</text>
    </view>

    <!-- 调试信息 -->
    <view class="debug-info">
      <text class="debug-title">调试信息：</text>
      <text class="debug-text">{{ debugInfo }}</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      // 表单数据
      formData: {
        studentName: '',      // 学生姓名
        sex: '',             // 性别
        grade: '',           // 年级
        level: '',           // 学段
        parentName: '',      // 家长姓名
        parentPhone: '',     // 家长手机号
        status: '在读'       // 状态
      },

      // 学段选项
      levelOptions: ['小学', '初中', '高中'],

      // 年级选项（根据学段动态生成）
      gradeOptionsMap: {
        '小学': [
          { label: '1年级', value: 1 },
          { label: '2年级', value: 2 },
          { label: '3年级', value: 3 },
          { label: '4年级', value: 4 },
          { label: '5年级', value: 5 },
          { label: '6年级', value: 6 }
        ],
        '初中': [
          { label: '7年级', value: 7 },
          { label: '8年级', value: 8 },
          { label: '9年级', value: 9 }
        ],
        '高中': [
          { label: '高一', value: 10 },
          { label: '高二', value: 11 },
          { label: '高三', value: 12 }
        ]
      },

      // 状态选项
      statusOptions: ['在读', '停课', '毕业', '退费'],

      // 调试信息
      debugInfo: '准备就绪'
    }
  },

  computed: {
    // 根据学段获取年级选项（只返回标签数组用于显示）
    gradeOptions() {
      if (!this.formData.level) {
        return [];
      }
      const options = this.gradeOptionsMap[this.formData.level] || [];
      return options.map(item => item.label);
    },

    // 年级显示文本
    gradeDisplayText() {
      if (!this.formData.level) {
        return '请先选择学段';
      }

      // 找到当前年级对应的标签
      if (this.formData.grade) {
        const options = this.gradeOptionsMap[this.formData.level] || [];
        const found = options.find(item => item.value === this.formData.grade);
        return found ? found.label : '请选择年级';
      }

      return '请选择年级';
    }
  },

  methods: {
    // 性别选择变化
    onGenderChange(e) {
      console.log('性别选择:', e.detail.value);
      this.formData.sex = e.detail.value;
    },

    // 学段选择变化
    onLevelChange(e) {
      console.log('学段选择索引:', e.detail.value);
      const selectedLevel = this.levelOptions[e.detail.value];
      this.formData.level = selectedLevel;

      // 清空年级（因为学段变了，年级需要重新选择）
      this.formData.grade = '';
      console.log('学段已选择:', selectedLevel, '年级已重置');
    },

    // 年级选择变化
    onGradeChange(e) {
      console.log('年级选择索引:', e.detail.value);
      const selectedIndex = e.detail.value;
      const options = this.gradeOptionsMap[this.formData.level] || [];
      const selected = options[selectedIndex];

      if (selected) {
        this.formData.grade = selected.value; // 保存数字值
        console.log('年级已选择:', selected.label, '值:', selected.value);
      }
    },

    // 状态选择变化
    onStatusChange(e) {
      console.log('状态选择索引:', e.detail.value);
      this.formData.status = this.statusOptions[e.detail.value];
    },

    // 提交表单
    async submit() {
      console.log('===== 开始提交 =====');
      console.log('表单数据:', JSON.stringify(this.formData, null, 2));

      this.debugInfo = '开始验证...';

      // 1. 表单验证
      if (!this.validate()) {
        this.debugInfo = '验证失败';
        console.log('验证失败，停止提交');
        return;
      }

      this.debugInfo = '验证通过，准备提交...';
      console.log('验证通过');

      // 2. 显示加载提示
      // H5环境使用普通提示
      if (typeof uni !== 'undefined') {
        uni.showLoading({
          title: '提交中...'
        });
      }

      try {
        this.debugInfo = '正在调用API...';
        console.log('开始调用后端API');

        // 3. 调用后端API
        const result = await this.saveStudent();
        console.log('API返回结果:', result);

        this.debugInfo = 'API调用成功';

        // 4. 提交成功
        if (typeof uni !== 'undefined') {
          uni.hideLoading();
          uni.showToast({
            title: '录入成功',
            icon: 'success'
          });
        } else {
          alert('录入成功！');
        }

        this.debugInfo = '录入成功！';

        // 5. 重置表单
        this.reset();

      } catch (error) {
        console.error('保存失败:', error);
        this.debugInfo = '错误: ' + error.message;

        if (typeof uni !== 'undefined') {
          uni.hideLoading();
          uni.showToast({
            title: '录入失败: ' + error.message,
            icon: 'none',
            duration: 3000
          });
        } else {
          alert('录入失败: ' + error.message);
        }
      }

      console.log('===== 提交结束 =====');
    },

    // 保存学生（调用API）
    async saveStudent() {
      const apiUrl = 'http://localhost:8080/api/student';
      console.log('API URL:', apiUrl);
      console.log('请求数据:', JSON.stringify(this.formData, null, 2));

      try {
        const response = await fetch(apiUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.formData)
        });

        console.log('响应状态:', response.status);
        console.log('响应头:', response.headers);

        if (!response.ok) {
          const errorText = await response.text();
          console.error('响应失败:', errorText);
          throw new Error(`HTTP ${response.status}: ${errorText || '网络请求失败'}`);
        }

        const data = await response.json();
        console.log('响应数据:', data);

        if (!data) {
          throw new Error('服务器返回空数据');
        }

        return data;

      } catch (error) {
        console.error('fetch错误:', error);

        // 检查是否是网络错误
        if (error.message.includes('Failed to fetch')) {
          throw new Error('无法连接到服务器，请确保后端服务已启动（http://localhost:8080）');
        }

        throw error;
      }
    },

    // 表单验证
    validate() {
      console.log('开始验证表单...');

      // 验证姓名
      if (!this.formData.studentName || this.formData.studentName.trim() === '') {
        console.log('❌ 姓名验证失败');
        if (typeof uni !== 'undefined') {
          uni.showToast({
            title: '请输入学生姓名',
            icon: 'none'
          });
        } else {
          alert('请输入学生姓名');
        }
        return false;
      }
      console.log('✓ 姓名验证通过');

      // 验证学段
      if (!this.formData.level || this.formData.level.trim() === '') {
        console.log('❌ 学段验证失败');
        if (typeof uni !== 'undefined') {
          uni.showToast({
            title: '请选择学段',
            icon: 'none'
          });
        } else {
          alert('请选择学段');
        }
        return false;
      }
      console.log('✓ 学段验证通过');

      // 验证年级
      if (!this.formData.grade && this.formData.grade !== 0) {
        console.log('❌ 年级验证失败');
        if (typeof uni !== 'undefined') {
          uni.showToast({
            title: '请选择年级',
            icon: 'none'
          });
        } else {
          alert('请选择年级');
        }
        return false;
      }
      console.log('✓ 年级验证通过');

      // 验证手机号格式
      if (this.formData.parentPhone && this.formData.parentPhone.trim() !== '') {
        const phoneReg = /^1[3-9]\d{9}$/;
        if (!phoneReg.test(this.formData.parentPhone)) {
          console.log('❌ 手机号验证失败');
          if (typeof uni !== 'undefined') {
            uni.showToast({
              title: '手机号格式不正确',
              icon: 'none'
            });
          } else {
            alert('手机号格式不正确');
          }
          return false;
        }
        console.log('✓ 手机号验证通过');
      }

      console.log('✓ 所有验证通过');
      return true;
    },

    // 重置表单
    reset() {
      console.log('重置表单');
      this.formData = {
        studentName: '',
        sex: '',
        grade: '',
        level: '',
        parentName: '',
        parentPhone: '',
        status: '在读'
      };
      this.debugInfo = '表单已重置';
    }
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

.input {
  width: 100%;
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 0 10px;
  font-size: 14px;
  box-sizing: border-box;
}

.radio {
  display: inline-block;
  margin-right: 20px;
}

.radio text {
  margin-left: 5px;
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

.arrow {
  color: #999;
  font-size: 20px;
}

.picker.disabled {
  background: #f5f5f5;
  color: #999;
  cursor: not-allowed;
}

.hint {
  display: block;
  font-size: 12px;
  color: #ff4d4f;
  margin-top: 5px;
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
  background: #007AFF;
  color: white;
}

.btn-secondary {
  background: #f0f0f0;
  color: #333;
}

.tips {
  text-align: center;
  margin-bottom: 20px;
}

.tips-text {
  font-size: 12px;
  color: #999;
}

.debug-info {
  background: #fff3cd;
  border: 1px solid #ffc107;
  border-radius: 5px;
  padding: 10px;
  margin-top: 20px;
}

.debug-title {
  display: block;
  font-size: 12px;
  font-weight: bold;
  color: #856404;
  margin-bottom: 5px;
}

.debug-text {
  display: block;
  font-size: 11px;
  color: #856404;
  line-height: 1.6;
  word-break: break-all;
}
</style>
