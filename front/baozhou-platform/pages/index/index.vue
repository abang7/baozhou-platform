 <template>
    <view class="content">
      <image class="logo" src="/static/logo.png"></image>
      <view class="text-area">
        <text class="title">{{title}}</text>
      </view>

      <!-- 新增：跳转到学生录入页面的按钮 -->
      <view class="btn-group">
        <button class="btn btn-primary" @click="goToAddStudent">
          📝 录入学生信息
        </button>
        <button class="btn btn-add-class" @click="goToAddClass">
          📚 录入课程
        </button>
        <button class="btn btn-secondary" @click="goToSchedule">
          📖 查看课程表
        </button>
        <button class="btn btn-teacher" @click="goToTeacherSchedule">
          👨‍🏫 教师课程表
        </button>
        <button class="btn btn-calendar" @click="goToStudentCalendar">
          📆 学生日历
        </button>
        <button class="btn btn-leave" @click="goToLeaveApply">
          🏖️ 学生请假
        </button>
        <button class="btn btn-schedule" @click="goToScheduleManage">
          📅 排课管理
        </button>
      </view>
    </view>
  </template>

  <script>
  export default {
    data() {
      return {
        title: '宝周教育平台'
      }
    },
    onLoad() {

    },
    methods: {
      // 跳转到学生录入页面
      goToAddStudent() {
        uni.navigateTo({
          url: '/pages/student/add'
        });
      },

      // 跳转到录入课程页面
      goToAddClass() {
        uni.navigateTo({
          url: '/pages/class/add'
        });
      },

      // 跳转到课程表页面
      goToSchedule() {
        uni.navigateTo({
          url: '/pages/class/schedule'
        });
      },

      // 跳转到教师课程表页面
      goToTeacherSchedule() {
        uni.navigateTo({
          url: '/pages/teacher/schedule'
        });
      },

      // 跳转到学生日历页面
      goToStudentCalendar() {
        uni.navigateTo({
          url: '/pages/student/calendar'
        });
      },

      // 跳转到学生请假页面
      goToLeaveApply() {
        uni.navigateTo({
          url: '/pages/leave/apply'
        });
      },

      // 跳转到排课管理页面
      goToScheduleManage() {
        uni.navigateTo({
          url: '/pages/schedule/manage'
        });
      }
    }
  }
  </script>

  <style scoped>
  .content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    min-height: 100vh;
    padding: 20px;
  }

  .logo {
    height: 200rpx;
    width: 200rpx;
    margin-top: 200rpx;
    margin-left: auto;
    margin-right: auto;
    margin-bottom: 50rpx;
    border-radius: 20rpx;
    background: white;
    padding: 20rpx;
  }

  .text-area {
    display: flex;
    justify-content: center;
  }

  .title {
    font-size: 36rpx;
    color: #ffffff;
    font-weight: bold;
    text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
  }

  .btn-group {
    margin-top: 100rpx;
    display: flex;
    flex-direction: column;
    gap: 30rpx;
    width: 500rpx;
  }

  .btn {
    height: 88rpx;
    border: none;
    border-radius: 10rpx;
    font-size: 32rpx;
    font-weight: bold;
    box-shadow: 0 4px 12px rgba(0,0,0,0.2);
  }

  .btn-primary {
    background: #ffffff;
    color: #667eea;
  }

  .btn-add-class {
    background: #2196F3;
    color: white;
  }

  .btn-secondary {
    background: rgba(255, 255, 255, 0.9);
    color: #764ba2;
  }

  .btn-teacher {
    background: #FF9800;
    color: white;
  }

  .btn-leave {
    background: #FF5722;
    color: white;
  }

  .btn-calendar {
    background: #4CAF50;
    color: white;
  }

  .btn-schedule {
    background: #9C27B0;
    color: white;
  }
  </style>

