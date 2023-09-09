import type { Route } from '../index.type'
import Layout from '@/layout/index.vue'
import { createNameComponent } from '../createNode'

const route: Route[] = [
  {
    path: '/dingTalk',
    component: Layout,
    redirect: '/dingTalk/dept',
    name: 'DingTalk',
    alwayShow: true,
    meta: {
      title: '钉钉',
      icon: 'sfont system-yonghu'
    },
    children: [
      {
        path: 'dept',
        component: createNameComponent(() => import('@/views/dingTalk/dept/dept.vue')),
        name: 'DeptList',
        meta: {
          title: '部门列表',
          icon: 'sfont system-component'
        }
      },
      {
        path: 'user',
        component: createNameComponent(() => import('@/views/dingTalk/user/user.vue')),
        name: 'UserList',
        meta: {
          title: '用户列表',
          icon: 'sfont system-xingmingyonghumingnicheng'
        }
      },
      {
        path: 'dingTalkList',
        component: createNameComponent(() => import('@/views/dingTalk/timesheet/dingtalkList.vue')),
        name: 'DingTalkList',
        meta: {
          title: '考勤列表',
          icon: 'sfont system-xingmingyonghumingnicheng'
        }
      },
      {
        path: 'projectSheet',
        component: createNameComponent(() => import('@/views/dingTalk/timesheet/projectSheet.vue')),
        name: 'ProjectSheet',
        meta: {
          title: '考勤汇总',
          icon: 'sfont system-xingmingyonghumingnicheng'
        }
      },
      {
        path: 'dingTalkSync',
        component: createNameComponent(() => import('@/views/dingTalk/timesheet/dingtalkSync.vue')),
        name: 'DingTalkSync',
        meta: {
          title: '同步数据',
          icon: 'sfont system-xingmingyonghumingnicheng'
        }
      }
    ]
  }
]

export default route
