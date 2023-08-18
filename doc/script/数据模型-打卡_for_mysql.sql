/*##############################################################
   Sheet:    ��Ա������Ϣ
##############################################################*/


/*==============================================================
   Table:    DT_DEPT;
==============================================================*/
DROP TABLE IF EXISTS DT_DEPT;
CREATE TABLE DT_DEPT
(
	 ID VARCHAR(32) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,DT_DEPT_ID VARCHAR(50) NOT NULL  COMMENT '����ID'
	,DT_DEPT_NAME VARCHAR(50) NOT NULL  COMMENT '��������'
	,EN_DEPT_NO VARCHAR(50) COMMENT '��ҵ���ű��'
	,DT_PAR_DEPT_ID VARCHAR(50) COMMENT '������ID'
	,PRIMARY KEY (ID)
)  COMMENT '��֯����';


/*==============================================================
   Table:    DT_USER;
==============================================================*/
DROP TABLE IF EXISTS DT_USER;
CREATE TABLE DT_USER
(
	 ID VARCHAR(32) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,DT_USERID VARCHAR(50) COMMENT 'Ա����ʶ'
	,DT_USERNAME VARCHAR(100) COMMENT '����'
	,DT_UNIONID VARCHAR(50) COMMENT 'UNIONID'
	,DT_ACTIVE VARCHAR(10) COMMENT '�Ƿ񼤻 Y-��  N-��'
	,GET_PRI_IND VARCHAR(10) COMMENT '�Ƿ������ȡ���ݣ� Y-��  N-��'
	,EN_USERNO VARCHAR(20) COMMENT '��ҵԱ�����'
	,PRIMARY KEY (ID)
)  COMMENT '��Ա��';


/*==============================================================
   Table:    DT_USER_DEPT_REL;
==============================================================*/
DROP TABLE IF EXISTS DT_USER_DEPT_REL;
CREATE TABLE DT_USER_DEPT_REL
(
	 ID VARCHAR(32) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,DT_USERID VARCHAR(50) COMMENT 'Ա����ʶ'
	,DT_DEPT_ID VARCHAR(50) COMMENT '����ID'
	,PRIMARY KEY (ID)
)  COMMENT '������Ա������';


/*##############################################################
   Sheet:    ������
##############################################################*/


/*==============================================================
   Table:    DT_KQ_GROUP;
==============================================================*/
DROP TABLE IF EXISTS DT_KQ_GROUP;
CREATE TABLE DT_KQ_GROUP
(
	 ID VARCHAR(50) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,GROUP_NAME VARCHAR(50) COMMENT '����������'
	,CLASS_LIST VARCHAR(1000) COMMENT '������'
	,TYPE VARCHAR(10) COMMENT '�������͡�
FIXEDΪ�̶��Ű�
TURNΪ�ְ��Ű�
NONEΪ�ް��'
	,MEMBER_COUNT INT COMMENT '��Ա����'
	,OWNER_USER_ID VARCHAR(50) COMMENT '��������������'
	,PRIMARY KEY (ID)
)  COMMENT '������';


/*==============================================================
   Table:    DT_KQ_INFO;
==============================================================*/
DROP TABLE IF EXISTS DT_KQ_INFO;
CREATE TABLE DT_KQ_INFO
(
	 ID VARCHAR(50) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,DT_USERID VARCHAR(50) COMMENT 'Ա����ʶ'
	,WORK_DATE DATE COMMENT '������'
	,RECORD_ID VARCHAR(50) COMMENT '�򿨼�¼ID'
	,GROUP_ID VARCHAR(50) COMMENT '������ID'
	,SOURCE_TYPE VARCHAR(10) COMMENT '������Դ
ATM�����ڻ�
BEACON��IBeacon
DING_ATM���������ڻ�
USER���û���
BOSS���ϰ��ǩ
APPROVE������ϵͳ
SYSTEM������ϵͳ
AUTO_CHECK���Զ���'
	,USERCHECK_TIME DATETIME COMMENT 'ʵ�ʴ�ʱ��'
	,BASE_CHECK_TIME DATETIME COMMENT '����ٵ������ˣ���׼ʱ�䣻Ҳ����Ϊ�Ű��ʱ��'
	,TIME_RESULT VARCHAR(20) COMMENT '�򿨽�� Normal������
  Early������
  Late���ٵ�
  SeriousLate�����سٵ�
  Absenteeism�������ٵ�
  NotSigned��δ��'
	,LOCATION_RESULT VARCHAR(20) COMMENT 'λ�ý�� Normal����Χ��
      Outside����Χ��
      NotSigned��δ��
      checkType	String	OnDuty	�������ͣ�
      OnDuty���ϰ�
      OffDuty���°�'
	,CHECK_TYPE VARCHAR(10) COMMENT '�������ͣ�
OnDuty���ϰ�
OffDuty���°�'
	,PROC_INST_ID VARCHAR(50) COMMENT '����������ʵ��ID�������ֶηǿ�ʱ����ʾ�򿨼�¼����١��Ӱ�������йء��������ȡ����ʵ���������ʹ�á�'
	,APPROVE_ID VARCHAR(50) COMMENT '����������ID�������ֶηǿ�ʱ����ʾ�򿨼�¼����١��Ӱ�������йء�'
	,PLAN_ID VARCHAR(50) COMMENT '�Ű�ID'
	,PRIMARY KEY (ID)
)  COMMENT '��������';


/*==============================================================
   Table:    DT_KQ_DETAIL;
==============================================================*/
DROP TABLE IF EXISTS DT_KQ_DETAIL;
CREATE TABLE DT_KQ_DETAIL
(
	 ID VARCHAR(50) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,USER_ACCURACY VARCHAR(50) COMMENT '�û��򿨶�λ����'
	,USEZR_LATITUDE VARCHAR(50) COMMENT '�û���γ��'
	,USER_LONGITUDE VARCHAR(50) COMMENT '�û��򿨾���'
	,USER_ADDRESS VARCHAR(200) COMMENT '�û��򿨵�ַ'
	,DEVICE_ID VARCHAR(50) COMMENT '���豸ID'
	,LOCATION_METHOD VARCHAR(50) COMMENT '��λ����'
	,IS_LEGAL VARCHAR(20) COMMENT '�Ƿ�Ϸ�'
	,USERCHECK_TIME DATETIME COMMENT 'ʵ�ʴ�ʱ��'
	,BASE_CHECK_TIME DATETIME COMMENT '����ٵ������ˣ���׼ʱ�䣻Ҳ����Ϊ�Ű��ʱ��'
	,TIME_RESULT VARCHAR(20) COMMENT '�򿨽�� Normal������
  Early������
  Late���ٵ�
  SeriousLate�����سٵ�
  Absenteeism�������ٵ�
  NotSigned��δ��'
	,LOCATION_RESULT VARCHAR(20) COMMENT 'λ�ý�� Normal����Χ��
      Outside����Χ��
      NotSigned��δ��
      checkType	String	OnDuty	�������ͣ�
      OnDuty���ϰ�
      OffDuty���°�'
	,SOURCE_TYPE VARCHAR(20) COMMENT '������Դ
ATM�����ڻ�
BEACON��IBeacon
DING_ATM���������ڻ�
USER���û���
BOSS���ϰ��ǩ
APPROVE������ϵͳ
SYSTEM������ϵͳ
AUTO_CHECK���Զ���'
	,USER_ID VARCHAR(50) COMMENT '���˵�userid'
	,WORK_DATE DATE COMMENT '������'
	,GROUP_ID VARCHAR(50) COMMENT '������ID'
	,USER_SSID VARCHAR(50) COMMENT '�û���wifi SSID'
	,USER_MAC_ADDR VARCHAR(50) COMMENT '�û���wifi Mac��ַ'
	,BASE_ADDRESS VARCHAR(200) COMMENT '��׼��ַ'
	,BASE_LONGITUDE VARCHAR(50) COMMENT '��׼����'
	,BASE_LATITUDE VARCHAR(50) COMMENT '��׼γ��'
	,BASE_ACCURACY VARCHAR(50) COMMENT '��׼��λ����'
	,BASE_SSID VARCHAR(50) COMMENT '��׼wifi ssid'
	,BASE_MAC_ADDR VARCHAR(50) COMMENT '��׼Mac��ַ'
	,GMT_CREATE DATETIME COMMENT '�򿨴���ʱ��'
	,OUTSIDE_REMARK VARCHAR(1000) COMMENT '�򿨱�ע'
	,DEVICE_SN VARCHAR(50) COMMENT '���豸���к�'
	,PLAN_ID VARCHAR(50) COMMENT '�Ű�ID'
	,BIZ_ID VARCHAR(50) COMMENT '������ҵ��ID'
	,PRIMARY KEY (ID)
)  COMMENT '���ڴ�����';


/*##############################################################
   Sheet:    ����
##############################################################*/


/*==============================================================
   Table:    DT_APR_LEAVE;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_LEAVE;
CREATE TABLE DT_APR_LEAVE
(
	 ID VARCHAR(50) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,DT_USERID VARCHAR(50) COMMENT 'Ա����ʶ'
	,DURATION_UNIT VARCHAR(20) COMMENT '��ٵ�λ��
percent_day����ʾ��
percent_hour����ʾСʱ'
	,DURATION_PERCENT DECIMAL(10) COMMENT '����ʱ��*100�������û����ʱ��Ϊ1�죬��ֵ�͵���100��'
	,MEMBER_COUNT INT COMMENT '��Ա����'
	,OWNER_USER_ID VARCHAR(50) COMMENT '��������������'
	,START_TIME DATETIME COMMENT '��ٿ�ʼʱ�䣬Unixʱ�����'
	,END_TIME DATETIME COMMENT '��ٽ���ʱ�䣬Unixʱ�����'
	,PRIMARY KEY (ID)
)  COMMENT '�����Ϣ';


/*==============================================================
   Table:    DT_APR_PROC_INST;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_PROC_INST;
CREATE TABLE DT_APR_PROC_INST
(
	 ID VARCHAR(50) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,DT_USERID VARCHAR(50) COMMENT 'Ա����ʶ'
	,TITLE VARCHAR(200) COMMENT '����ʵ������'
	,CREATE_TIME DATETIME COMMENT '��ʼʱ��'
	,FINISH_TIME DATETIME COMMENT '����ʱ��'
	,ORIGINATOR_USERID VARCHAR(50) COMMENT '�����˵�userid'
	,ORIGINATOR_DEPT_ID VARCHAR(50) COMMENT '�����˵Ĳ���'
	,STATUS VARCHAR(20) COMMENT '����״̬��
NEW���´���
RUNNING��������
TERMINATED������ֹ
COMPLETED�����
CANCELED��ȡ��'
	,RESULT VARCHAR(20) COMMENT '���������
agree��ͬ��
refuse���ܾ�'
	,BUSINESS_ID VARCHAR(50) COMMENT '����ʵ��ҵ����'
	,ORIGINATOR_DEPT_NAME VARCHAR(50) COMMENT '������'
	,BIZ_ACTION VARCHAR(50) COMMENT '����ʵ��ҵ������
MODIFY����ʾ������ʵ���ǻ���ԭ����ʵ���޸Ķ���
REVOKE����ʾ������ʵ������ԭ����ʵ�����������·����
NONE��ʾ��������'
	,MAIN_PROCESS_INSTANCE_ID VARCHAR(50) COMMENT '������ʵ����ʶ'
	,PRIMARY KEY (ID)
)  COMMENT '����ʵ������';


/*==============================================================
   Table:    DT_APR_PROC_SUB_INST;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_PROC_SUB_INST;
CREATE TABLE DT_APR_PROC_SUB_INST
(
	 ID VARCHAR(50) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,PROC_INST_ID VARCHAR(50) COMMENT '����ʵ��ID'
	,PROC_INST_SUB_ID VARCHAR(50) COMMENT '��������ʵ��ID'
	,PRIMARY KEY (ID)
)  COMMENT '��������ʵ���б�';


/*==============================================================
   Table:    DT_APR_OPT_RECORD;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_OPT_RECORD;
CREATE TABLE DT_APR_OPT_RECORD
(
	 ID VARCHAR(50) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,PROC_INST_ID VARCHAR(50) COMMENT '����ʵ��ID'
	,DT_USERID VARCHAR(50) COMMENT '������'
	,DATE DATETIME COMMENT '����ʱ��'
	,OPERATION_TYPE VARCHAR(30) COMMENT '�������ͣ�
EXECUTE_TASK_NORMAL������ִ������
EXECUTE_TASK_AGENT��������ִ������
APPEND_TASK_BEFORE��ǰ��ǩ����
APPEND_TASK_AFTER�����ǩ����
REDIRECT_TASK��ת������
START_PROCESS_INSTANCE����������ʵ��
TERMINATE_PROCESS_INSTANCE����ֹ(����)����ʵ��
FINISH_PROCESS_INSTANCE����������ʵ��
ADD_REMARK���������
redirect_process�������˻�'
	,OPERATION_RESULT VARCHAR(30) COMMENT '���������
AGREE��ͬ��
REFUSE���ܾ�
NONE'
	,REMARK VARCHAR(2000) COMMENT '�������ݡ�����������������ʱ�ŷ��ظ��ֶΡ�'
	,PRIMARY KEY (ID)
)  COMMENT '������¼�б�';


/*==============================================================
   Table:    DT_APR_OPT_ATTACHMENT;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_OPT_ATTACHMENT;
CREATE TABLE DT_APR_OPT_ATTACHMENT
(
	 ID VARCHAR(50) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,PROC_INST_ID VARCHAR(50) COMMENT '����ʵ��ID'
	,OPT_RECORD_ID VARCHAR(50) COMMENT '������¼ID'
	,FILE_NAME VARCHAR(200) COMMENT '��������'
	,FILE_SIZE VARCHAR(30) COMMENT '������С'
	,FILE_ID VARCHAR(30) COMMENT '����ID'
	,FILE_TYPE VARCHAR(2000) COMMENT '��������'
	,PRIMARY KEY (ID)
)  COMMENT '������¼���۸���';


/*==============================================================
   Table:    DT_APR_TASK;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_TASK;
CREATE TABLE DT_APR_TASK
(
	 ID VARCHAR(50) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,PROC_INST_ID VARCHAR(50) COMMENT '����ʵ��ID'
	,TASK_STATUS VARCHAR(50) COMMENT '����״̬��
	NEW��δ����
	RUNNING��������
	PAUSED����ͣ
	CANCELED��ȡ��
	COMPLETED�����
	TERMINATED����ֹ'
	,TASK_RESULT VARCHAR(20) COMMENT '�����
	AGREE��ͬ��
	REFUSE���ܾ�
	REDIRECTED��ת��'
	,CREATE_TIME DATETIME COMMENT '��ʼʱ��'
	,FINISH_TIME DATETIME COMMENT '����ʱ��'
	,TASKID VARCHAR(200) COMMENT '����ڵ�ID'
	,URL VARCHAR(200) COMMENT '����URL'
	,PRIMARY KEY (ID)
)  COMMENT '�����б�';


/*==============================================================
   Table:    DT_APR_FORM_INFO;
==============================================================*/
DROP TABLE IF EXISTS DT_APR_FORM_INFO;
CREATE TABLE DT_APR_FORM_INFO
(
	 ID VARCHAR(50) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,PROC_INST_ID VARCHAR(50) COMMENT '����ʵ��ID'
	,NAME VARCHAR(200) COMMENT '��ǩ��'
	,VALUE VARCHAR(1000) COMMENT '��ǩֵ'
	,EXT_VALUE VARCHAR(2000) COMMENT '��ǩ��չֵ'
	,COMPONENT_ID VARCHAR(50) COMMENT '���ID'
	,COMPONENT_TYPE VARCHAR(50) COMMENT '�������'
	,PRIMARY KEY (ID)
)  COMMENT '�������б�';


/*##############################################################
   Sheet:    ����
##############################################################*/


/*==============================================================
   Table:    DT_VOCATION_TYPE;
==============================================================*/
DROP TABLE IF EXISTS DT_VOCATION_TYPE;
CREATE TABLE DT_VOCATION_TYPE
(
	 ID VARCHAR(50) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,LEAVE_CODE VARCHAR(50) COMMENT '���ڹ���Ψһ��ʶ��ͬ����ID'
	,LEAVE_NAME VARCHAR(20) COMMENT '��������'
	,LEAVE_VIEW_UNIT VARCHAR(20) COMMENT '��ٵ�λ��percent_day����ʾ�죻percent_hour����ʾСʱ'
	,BIZ_TYPE VARCHAR(20) COMMENT '�������͡�general_leave����ͨ����; lieu_leave���Ӱ�ת����'
	,NATURAL_DAY_LEAVE VARCHAR(20) COMMENT '�Ƿ�����Ȼ��ͳ�����ʱ���� true��������Ȼ��ͳ�����ʱ��; false����������Ȼ��ͳ�����ʱ��'
	,VALIDITY_TYPE VARCHAR(20) COMMENT '��Ч���͡�absolute_time������ʱ�䣻relative_time�����ʱ��'
	,VALIDITY_VALUE VARCHAR(20) COMMENT '�ӳ����ڡ���validity_typeΪabsolute_time��ֵ��Ϊ�������㡰yy-mm����ʽ����validity_typeΪrelative_time��ֵΪ����1��������'
	,HOURS_IN_PER_DAY VARCHAR(20) COMMENT 'ÿ������Ĺ���ʱ�����ٷ�֮һ�����磺1��=10Сʱ=1000��'
	,SOURCE VARCHAR(20) COMMENT '������Դ��external�����Žӿ��Զ���ģ�inner��oa��̨�½���'
	,PRIMARY KEY (ID)
)  COMMENT '��������';


/*##############################################################
   Sheet:    ��������
##############################################################*/


/*==============================================================
   Table:    C_ORG;
==============================================================*/
DROP TABLE IF EXISTS C_ORG;
CREATE TABLE C_ORG
(
	 ID VARCHAR(50) NOT NULL  COMMENT '����'
	,CREATED_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼������'
	,CREATED_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼����ʱ��'
	,LAST_UPD_BY VARCHAR(32) DEFAULT 'admin' NOT NULL  COMMENT '��¼��������'
	,LAST_UPD_TS DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL  COMMENT '��¼������ʱ��'
	,ORG_NO VARCHAR(20) NOT NULL  COMMENT '������'
	,ORG_NAME VARCHAR(100) NOT NULL  COMMENT '������'
	,PAR_ORG_NO VARCHAR(20) COMMENT '�ϼ�������'
	,PAR_ORG_NAME VARCHAR(100) COMMENT '�ϼ�������'
	,ORG_LVL VARCHAR(20) COMMENT '��������'
	,ORG_LVL_ONE VARCHAR(20) COMMENT 'ʡ����'
	,ORG_LVL_TWO VARCHAR(20) COMMENT '�з���'
	,ORG_LVL_THR VARCHAR(20) COMMENT '֧��'
	,ORG_LVL_FOUR VARCHAR(20) COMMENT '����'
	,PROV VARCHAR(20) COMMENT 'ʡ'
	,CITY VARCHAR(20) COMMENT '��'
	,REGION VARCHAR(20) COMMENT '����'
	,ORG_ADDR VARCHAR(500) COMMENT '������ַ'
	,ORG_PHONE VARCHAR(20) COMMENT '�����绰'
	,LINKMAN_NAME VARCHAR(200) COMMENT '��ϵ������'
	,LINKMAN_NO VARCHAR(20) COMMENT '��ϵ��Ա����'
	,LINKMAN_PHONE VARCHAR(20) COMMENT '��ϵ���ֻ�'
	,PRIMARY KEY (ID)
)  COMMENT '������';


