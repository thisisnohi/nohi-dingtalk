/*##############################################################
   Sheet:    人员部门信息
##############################################################*/


/*==============================================================
   Table:    DT_DEPT;
==============================================================*/
DROP TABLE IF EXISTS DT_DEPT;
CREATE TABLE DT_DEPT
(
	 ID VARCHAR(32) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,DT_DEPT_ID VARCHAR(50) NOT NULL  COMMENT '部门ID'
	,DT_DEPT_NAME VARCHAR(50) NOT NULL  COMMENT '部门名称'
	,EN_DEPT_NO VARCHAR(50) COMMENT '企业部门编号'
	,DT_PAR_DEPT_ID VARCHAR(50) COMMENT '父部门ID'
	,PRIMARY KEY (ID)
)  COMMENT '组织部门';


/*==============================================================
   Table:    DT_USER;
==============================================================*/
DROP TABLE IF EXISTS DT_USER;
CREATE TABLE DT_USER
(
	 ID VARCHAR(32) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,DT_USERID VARCHAR(50) COMMENT '员工标识'
	,DT_USERNAME VARCHAR(100) COMMENT '名称'
	,DT_UNIONID VARCHAR(50) COMMENT 'UNIONID'
	,DT_ACTIVE VARCHAR(10) COMMENT '是否激活： Y-是  N-否'
	,GET_PRI_IND VARCHAR(10) COMMENT '是否允许获取数据： Y-是  N-否'
	,EN_USERNO VARCHAR(20) COMMENT '企业员工编号'
	,PRIMARY KEY (ID)
)  COMMENT '人员表';


/*==============================================================
   Table:    DT_USER_DEPT_REL;
==============================================================*/
DROP TABLE IF EXISTS DT_USER_DEPT_REL;
CREATE TABLE DT_USER_DEPT_REL
(
	 ID VARCHAR(32) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,DT_USERID VARCHAR(50) COMMENT '员工标识'
	,DT_DEPT_ID VARCHAR(50) COMMENT '部门ID'
	,PRIMARY KEY (ID)
)  COMMENT '部门人员关联表';


/*##############################################################
   Sheet:    打卡数据
##############################################################*/


/*==============================================================
   Table:    DT_KQ_GROUP;
==============================================================*/
DROP TABLE IF EXISTS DT_KQ_GROUP;
CREATE TABLE DT_KQ_GROUP
(
	 ID VARCHAR(50) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,GROUP_NAME VARCHAR(50) COMMENT '考勤组名称'
	,CLASS_LIST VARCHAR(1000) COMMENT '工作日'
	,TYPE VARCHAR(10) COMMENT '考勤类型。
FIXED为固定排班
TURN为轮班排班
NONE为无班次'
	,MEMBER_COUNT INT COMMENT '成员人数'
	,OWNER_USER_ID VARCHAR(50) COMMENT '考勤组主负责人'
	,PRIMARY KEY (ID)
)  COMMENT '考勤组';


/*==============================================================
   Table:    DT_KQ_INFO;
==============================================================*/
DROP TABLE IF EXISTS DT_KQ_INFO;
CREATE TABLE DT_KQ_INFO
(
	 ID VARCHAR(50) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,DT_USERID VARCHAR(50) COMMENT '员工标识'
	,WORK_DATE DATE COMMENT '工作日'
	,RECORD_ID VARCHAR(50) COMMENT '打卡记录ID'
	,GROUP_ID VARCHAR(50) COMMENT '考勤组ID'
	,SOURCE_TYPE VARCHAR(10) COMMENT '数据来源
ATM：考勤机
BEACON：IBeacon
DING_ATM：钉钉考勤机
USER：用户打卡
BOSS：老板改签
APPROVE：审批系统
SYSTEM：考勤系统
AUTO_CHECK：自动打卡'
	,USERCHECK_TIME DATETIME COMMENT '实际打卡时间'
	,BASE_CHECK_TIME DATETIME COMMENT '计算迟到和早退，基准时间；也可作为排班打卡时间'
	,TIME_RESULT VARCHAR(20) COMMENT '打卡结果 Normal：正常
  Early：早退
  Late：迟到
  SeriousLate：严重迟到
  Absenteeism：旷工迟到
  NotSigned：未打卡'
	,LOCATION_RESULT VARCHAR(20) COMMENT '位置结果 Normal：范围内
      Outside：范围外
      NotSigned：未打卡
      checkType	String	OnDuty	考勤类型：
      OnDuty：上班
      OffDuty：下班'
	,CHECK_TYPE VARCHAR(10) COMMENT '考勤类型：
OnDuty：上班
OffDuty：下班'
	,PROC_INST_ID VARCHAR(50) COMMENT '关联的审批实例ID，当该字段非空时，表示打卡记录与请假、加班等审批有关。可以与获取审批实例详情配合使用。'
	,APPROVE_ID VARCHAR(50) COMMENT '关联的审批ID，当该字段非空时，表示打卡记录与请假、加班等审批有关。'
	,PLAN_ID VARCHAR(50) COMMENT '排班ID'
	,PRIMARY KEY (ID)
)  COMMENT '考勤数据';


/*==============================================================
   Table:    DT_KQ_DETAIL;
==============================================================*/
DROP TABLE IF EXISTS DT_KQ_DETAIL;
CREATE TABLE DT_KQ_DETAIL
(
	 ID VARCHAR(50) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,USER_ACCURACY VARCHAR(50) COMMENT '用户打卡定位精度'
	,USEZR_LATITUDE VARCHAR(50) COMMENT '用户打卡纬度'
	,USER_LONGITUDE VARCHAR(50) COMMENT '用户打卡经度'
	,USER_ADDRESS VARCHAR(200) COMMENT '用户打卡地址'
	,DEVICE_ID VARCHAR(50) COMMENT '打卡设备ID'
	,LOCATION_METHOD VARCHAR(50) COMMENT '定位方法'
	,IS_LEGAL VARCHAR(20) COMMENT '是否合法'
	,USERCHECK_TIME DATETIME COMMENT '实际打卡时间'
	,BASE_CHECK_TIME DATETIME COMMENT '计算迟到和早退，基准时间；也可作为排班打卡时间'
	,TIME_RESULT VARCHAR(20) COMMENT '打卡结果 Normal：正常
  Early：早退
  Late：迟到
  SeriousLate：严重迟到
  Absenteeism：旷工迟到
  NotSigned：未打卡'
	,LOCATION_RESULT VARCHAR(20) COMMENT '位置结果 Normal：范围内
      Outside：范围外
      NotSigned：未打卡
      checkType	String	OnDuty	考勤类型：
      OnDuty：上班
      OffDuty：下班'
	,SOURCE_TYPE VARCHAR(20) COMMENT '数据来源
ATM：考勤机
BEACON：IBeacon
DING_ATM：钉钉考勤机
USER：用户打卡
BOSS：老板改签
APPROVE：审批系统
SYSTEM：考勤系统
AUTO_CHECK：自动打卡'
	,USER_ID VARCHAR(50) COMMENT '打卡人的userid'
	,WORK_DATE DATE COMMENT '工作日'
	,GROUP_ID VARCHAR(50) COMMENT '考勤组ID'
	,USER_SSID VARCHAR(50) COMMENT '用户打卡wifi SSID'
	,USER_MAC_ADDR VARCHAR(50) COMMENT '用户打卡wifi Mac地址'
	,BASE_ADDRESS VARCHAR(200) COMMENT '基准地址'
	,BASE_LONGITUDE VARCHAR(50) COMMENT '基准经度'
	,BASE_LATITUDE VARCHAR(50) COMMENT '基准纬度'
	,BASE_ACCURACY VARCHAR(50) COMMENT '基准定位精度'
	,BASE_SSID VARCHAR(50) COMMENT '基准wifi ssid'
	,BASE_MAC_ADDR VARCHAR(50) COMMENT '基准Mac地址'
	,GMT_CREATE DATETIME COMMENT '打卡创建时间'
	,OUTSIDE_REMARK VARCHAR(1000) COMMENT '打卡备注'
	,DEVICE_SN VARCHAR(50) COMMENT '打卡设备序列号'
	,PLAN_ID VARCHAR(50) COMMENT '排班ID'
	,BIZ_ID VARCHAR(50) COMMENT '关联的业务ID'
	,PRIMARY KEY (ID)
)  COMMENT '考勤打卡详情';


/*##############################################################
   Sheet:    审批
##############################################################*/


/*==============================================================
   Table:    DT_APR_LEAVE;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_LEAVE;
CREATE TABLE DT_APR_LEAVE
(
	 ID VARCHAR(50) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,DT_USERID VARCHAR(50) COMMENT '员工标识'
	,DURATION_UNIT VARCHAR(20) COMMENT '请假单位：
percent_day：表示天
percent_hour：表示小时'
	,DURATION_PERCENT DECIMAL(10) COMMENT '假期时长*100，例如用户请假时长为1天，该值就等于100。'
	,MEMBER_COUNT INT COMMENT '成员人数'
	,OWNER_USER_ID VARCHAR(50) COMMENT '考勤组主负责人'
	,START_TIME DATETIME COMMENT '请假开始时间，Unix时间戳。'
	,END_TIME DATETIME COMMENT '请假结束时间，Unix时间戳。'
	,PRIMARY KEY (ID)
)  COMMENT '请假信息';


/*==============================================================
   Table:    DT_APR_PROC_INST;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_PROC_INST;
CREATE TABLE DT_APR_PROC_INST
(
	 ID VARCHAR(50) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,DT_USERID VARCHAR(50) COMMENT '员工标识'
	,TITLE VARCHAR(200) COMMENT '审批实例标题'
	,CREATE_TIME DATETIME COMMENT '开始时间'
	,FINISH_TIME DATETIME COMMENT '结束时间'
	,ORIGINATOR_USERID VARCHAR(50) COMMENT '发起人的userid'
	,ORIGINATOR_DEPT_ID VARCHAR(50) COMMENT '发起人的部门'
	,STATUS VARCHAR(20) COMMENT '审批状态：
NEW：新创建
RUNNING：审批中
TERMINATED：被终止
COMPLETED：完成
CANCELED：取消'
	,RESULT VARCHAR(20) COMMENT '审批结果：
agree：同意
refuse：拒绝'
	,BUSINESS_ID VARCHAR(50) COMMENT '审批实例业务编号'
	,ORIGINATOR_DEPT_NAME VARCHAR(50) COMMENT '发起部门'
	,BIZ_ACTION VARCHAR(50) COMMENT '审批实例业务动作：
MODIFY：表示该审批实例是基于原来的实例修改而来
REVOKE：表示该审批实例是由原来的实例撤销后重新发起的
NONE表示正常发起'
	,MAIN_PROCESS_INSTANCE_ID VARCHAR(50) COMMENT '主流程实例标识'
	,PRIMARY KEY (ID)
)  COMMENT '审批实例详情';


/*==============================================================
   Table:    DT_APR_PROC_SUB_INST;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_PROC_SUB_INST;
CREATE TABLE DT_APR_PROC_SUB_INST
(
	 ID VARCHAR(50) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,PROC_INST_ID VARCHAR(50) COMMENT '流程实例ID'
	,PROC_INST_SUB_ID VARCHAR(50) COMMENT '附属流程实例ID'
	,PRIMARY KEY (ID)
)  COMMENT '审批附属实例列表';


/*==============================================================
   Table:    DT_APR_OPT_RECORD;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_OPT_RECORD;
CREATE TABLE DT_APR_OPT_RECORD
(
	 ID VARCHAR(50) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,PROC_INST_ID VARCHAR(50) COMMENT '流程实例ID'
	,DT_USERID VARCHAR(50) COMMENT '操作人'
	,DATE DATETIME COMMENT '操作时间'
	,OPERATION_TYPE VARCHAR(30) COMMENT '操作类型：
EXECUTE_TASK_NORMAL：正常执行任务
EXECUTE_TASK_AGENT：代理人执行任务
APPEND_TASK_BEFORE：前加签任务
APPEND_TASK_AFTER：后加签任务
REDIRECT_TASK：转交任务
START_PROCESS_INSTANCE：发起流程实例
TERMINATE_PROCESS_INSTANCE：终止(撤销)流程实例
FINISH_PROCESS_INSTANCE：结束流程实例
ADD_REMARK：添加评论
redirect_process：审批退回'
	,OPERATION_RESULT VARCHAR(30) COMMENT '操作结果：
AGREE：同意
REFUSE：拒绝
NONE'
	,REMARK VARCHAR(2000) COMMENT '评论内容。审批操作附带评论时才返回该字段。'
	,PRIMARY KEY (ID)
)  COMMENT '操作记录列表';


/*==============================================================
   Table:    DT_APR_OPT_ATTACHMENT;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_OPT_ATTACHMENT;
CREATE TABLE DT_APR_OPT_ATTACHMENT
(
	 ID VARCHAR(50) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,PROC_INST_ID VARCHAR(50) COMMENT '流程实例ID'
	,OPT_RECORD_ID VARCHAR(50) COMMENT '操作记录ID'
	,FILE_NAME VARCHAR(200) COMMENT '附件名称'
	,FILE_SIZE VARCHAR(30) COMMENT '附件大小'
	,FILE_ID VARCHAR(30) COMMENT '附件ID'
	,FILE_TYPE VARCHAR(2000) COMMENT '附件类型'
	,PRIMARY KEY (ID)
)  COMMENT '操作记录评论附件';


/*==============================================================
   Table:    DT_APR_TASK;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_TASK;
CREATE TABLE DT_APR_TASK
(
	 ID VARCHAR(50) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,PROC_INST_ID VARCHAR(50) COMMENT '流程实例ID'
	,TASK_STATUS VARCHAR(50) COMMENT '任务状态：
	NEW：未启动
	RUNNING：处理中
	PAUSED：暂停
	CANCELED：取消
	COMPLETED：完成
	TERMINATED：终止'
	,TASK_RESULT VARCHAR(20) COMMENT '结果：
	AGREE：同意
	REFUSE：拒绝
	REDIRECTED：转交'
	,CREATE_TIME DATETIME COMMENT '开始时间'
	,FINISH_TIME DATETIME COMMENT '结束时间'
	,TASKID VARCHAR(200) COMMENT '任务节点ID'
	,URL VARCHAR(200) COMMENT '任务URL'
	,PRIMARY KEY (ID)
)  COMMENT '任务列表';


/*==============================================================
   Table:    DT_APR_FORM_INFO;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_FORM_INFO;
CREATE TABLE DT_APR_FORM_INFO
(
	 ID VARCHAR(50) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,PROC_INST_ID VARCHAR(50) COMMENT '流程实例ID'
	,NAME VARCHAR(200) COMMENT '标签名'
	,VALUE VARCHAR(1000) COMMENT '标签值'
	,EXT_VALUE VARCHAR(2000) COMMENT '标签扩展值'
	,COMPONENT_ID VARCHAR(50) COMMENT '组件ID'
	,COMPONENT_TYPE VARCHAR(50) COMMENT '组件类型'
	,PRIMARY KEY (ID)
)  COMMENT '表单详情列表';


/*##############################################################
   Sheet:    假期
##############################################################*/


/*==============================================================
   Table:    DT_VOCATION_TYPE;
==============================================================*/
DROP TABLE IF EXISTS DT_VOCATION_TYPE;
CREATE TABLE DT_VOCATION_TYPE
(
	 ID VARCHAR(50) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,LEAVE_CODE VARCHAR(50) COMMENT '假期规则唯一标识，同主键ID'
	,LEAVE_NAME VARCHAR(20) COMMENT '假期名称'
	,LEAVE_VIEW_UNIT VARCHAR(20) COMMENT '请假单位：percent_day：表示天；percent_hour：表示小时'
	,BIZ_TYPE VARCHAR(20) COMMENT '假期类型。general_leave：普通假期; lieu_leave：加班转调休'
	,NATURAL_DAY_LEAVE VARCHAR(20) COMMENT '是否按照自然日统计请假时长。 true：按照自然日统计请假时长; false：不按照自然日统计请假时长'
	,VALIDITY_TYPE VARCHAR(20) COMMENT '有效类型。absolute_time：绝对时间；relative_time：相对时间'
	,VALIDITY_VALUE VARCHAR(20) COMMENT '延长日期。当validity_type为absolute_time该值不为空且满足“yy-mm”格式。当validity_type为relative_time该值为大于1的整数。'
	,HOURS_IN_PER_DAY VARCHAR(20) COMMENT '每天折算的工作时长，百分之一。例如：1天=10小时=1000。'
	,SOURCE VARCHAR(20) COMMENT '假期来源。external：开放接口自定义的；inner：oa后台新建的'
	,PRIMARY KEY (ID)
)  COMMENT '假期类型';


/*##############################################################
   Sheet:    机构网点
##############################################################*/


/*==============================================================
   Table:    C_ORG;
==============================================================*/
DROP TABLE IF EXISTS C_ORG;
CREATE TABLE C_ORG
(
	 ID VARCHAR(50) NOT NULL  COMMENT '主键'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录创建者'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录创建时间'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '记录最后更新者'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '记录最后更新时间'
	,ORG_NO VARCHAR(20) NOT NULL  COMMENT '机构号'
	,ORG_NAME VARCHAR(100) NOT NULL  COMMENT '机构名'
	,PAR_ORG_NO VARCHAR(20) COMMENT '上级机构号'
	,PAR_ORG_NAME VARCHAR(100) COMMENT '上级机构名'
	,ORG_LVL VARCHAR(20) COMMENT '机构级别'
	,ORG_LVL_ONE VARCHAR(20) COMMENT '省分行'
	,ORG_LVL_TWO VARCHAR(20) COMMENT '市分行'
	,ORG_LVL_THR VARCHAR(20) COMMENT '支行'
	,ORG_LVL_FOUR VARCHAR(20) COMMENT '网点'
	,PROV VARCHAR(20) COMMENT '省'
	,CITY VARCHAR(20) COMMENT '市'
	,REGION VARCHAR(20) COMMENT '区县'
	,ORG_ADDR VARCHAR(500) COMMENT '机构地址'
	,ORG_PHONE VARCHAR(20) COMMENT '机构电话'
	,LINKMAN_NAME VARCHAR(200) COMMENT '联系人名称'
	,LINKMAN_NO VARCHAR(20) COMMENT '联系人员工号'
	,LINKMAN_PHONE VARCHAR(20) COMMENT '联系人手机'
	,PRIMARY KEY (ID)
)  COMMENT '机构表';


