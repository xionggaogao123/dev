package com.sys.constants;

import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    /**
     * chat 站点设置名称
     */
    public static final String APP_CHAT_SET = "cs";

    public static final String UTF_8 = "utf-8";

    public static final String GBK = "GBK";

    public static final String ISO = "ISO8859-1";

    public static final String SEPARATOR = "\\.";

    public static final String POINT = ".";

    public static final String SMALL_POINT = "'";

    public static final String START = "*";

    public static final String AT = "@";
    //冒号
    public static final String COLON = ":";
    //分号
    public static final String SEMICOLON = ";";
    //下划线
    public static final String UNDERLINE = "_";
    //分隔线
    public static final String SEPARATE_LINE = "-";
    //问号
    public static final String ASK = "?";
    //并且
    public static final String AND = "&";
    //等于
    public static final String EQUALS = "=";
    //逗号
    public static final String COMMA = ",";
    //有效的
    public static final int VALID = 1;
    //无效的
    public static final int INVALID = 0;
    //char ','
    public static final char CHAR_COMMA = ',';
    //换行符
    public static final String NEW_ROWS = "\n";
    //空字符
    public static final String EMPTY = "";
    public static final String EMPTY_STR="EMPTY";
    public static final String DEFAULT_POINT="Point";
    //一个空格
    public static final String SPACE = " ";

    public static final String BASE_PATH = "/";
    public static final String STRING_NEGATIVE_ONE = "-1";

    //2秒
    public static final int SESSION_TWO_SECONDS = 2;
    //1分钟
    public static final int SESSION_ONE_MINUTE = 60;
    //2分钟
    public static final int SESSION_TWO_MINUTE = 2 * 60;
    //5分钟
    public static final int SESSION_FIVE_MINUTE = 5 * 60;
    //10分钟
    public static final int SESSION_TEN_MINUTE = 10 * 60;
    //http session时长，30分钟
    public static final long SESSION_TIMEOUT_SECOND = 30 * 60;
    public static final int SESSION_TIMEOUT_SECOND_INT = 30 * 60;
    //一小时
    public static final int SECONDS_IN_HOUR = 60 * 60;
    //一小时
    public static final long MS_SECONDS_IN_HOUR = 60 * 60 * 1000L;
    //一天
    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    //缓存5天
    public static final int SECONDS_IN_FIVE_DAY = 60 * 60 * 24 * 5;
    //一天
    public static final long MS_SECONDS_IN_DAY = 60 * 60 * 24 * 1000L;
    //一周
    public static final int SECONDS_IN_WEEK = 60 * 60 * 24 * 7;
    //一周
    public static final long MS_SECONDS_IN_WEEK = 60L * 60L * 24L * 7L * 1000L;
    //一月
    public static final int SECONDS_IN_MONTH = 60 * 60 * 24 * 30;
    //半年
    public static final int SECONDS_IN_HALF_YEAR =  60 * 60 * 24 * 30 * 6;
    //一月
    public static final long MS_SECONDS_IN_MONTH = 60L * 60L * 24L * 30L * 1000L;

    //2月
    public static final long MS_SECONDS_IN_TWO_MONTH = 60L * 60L * 24L * 30L * 1000L * 2L;
    //一年
    public static final long SECONDS_IN_YEAR = 60L * 60L * 24L * 365L * 1000L;
    //一年
    public static final long MS_SECONDS_IN_YEAR = 60L * 60L * 24L * 365L * 1000L;
    /**
     * 每天的毫秒数
     */
    public static final long MS_IN_DAY = 1000L * 60 * 60 * 24;

    /**
     * 500毫秒
     */
    public static final long MS_ONE_THOUSAND = 1000L;


    /**
     * 每周的毫秒数
     */
    public static final long MS_IN_WEEK = MS_IN_DAY * 7;

    /**
     * 半小时
     */
    public static final long MS_IN_30_MINUTE = 1000L * 60 * 30;


    public static final int SYN_NOT_NEED = 1;//已经同步完成
    public static final int SYN_YES_NEED = 0;//需要同步


    /**
     * 值
     */
    public static final int DEFAULT_VALUE_INT = 0;
    public static final String DEFAULT_VALUE_STRING = "";
    public static final long DEFAULT_VALUE_LONG = 0l;
    public static final double DEFAULT_VALUE_DOUBLE = 0D;
    public static final Object DEFAULT_VALUE_OBJECT = null;
    public static final List DEFAULT_VALUE_ARRAY = new ArrayList();
    public static final int MAN = 1;
    public static final int WOMEN = 0;

    public static final String COOKIE_USER_KEY = "ui";
    public static final String COOKIE_SSO_USER_KEY = "sso_ui";
    public static final String COOKIE_USERNAME_KEY = "momcallme";
    public static final String COOKIE_VALIDATE_CODE = "verifycode";
    public static final String COOKIE_PASS_WORD = "password";
    //保存记录信息
    public static final String COOKIE_INSTITUTE_INFO="instituteInfo";

    public static final String USER_FIRST_LOGIN = "firstlogin";

    public static final String APP_SHARE = "appshare";

    public static final String MD5 = "MD5";

    public static final String FWCODE = "FWCODE";
    public static final String FW_RESET_PASSWORD_CODE = "FWRESETCODE";


    /**
     * 最大的key.split 之后的长度，最大支持长度为3的key,也就是支持a.b.c,不支持a.b.c.d
     */
    public static final int ENTRY_MAX_FIELD_ARRAY_LENGTH = 3;

    public static final int NEGATIVE_ONE = -1;

    public static final int NEGATIVE_SEVEN = -7;
    public static final int NEGATIVE_TEN = -10;
    public static final int NEGATIVE_HUNDRED = -100;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;
    public static final int TWELVE = 12;
    public static final int FOURTEEN = 14;
    public static final int FIFTEEN = 15;
    public static final int SIXTEEN = 16;
    public static final int TWENTY = 20;
    public static final int TWENTY_FOUR = 24;
    public static final int TWENTY_FIVE = 25;
    public static final int FIVETY = 50;
    public static final int HUNDRED = 100;
    public static final int HUNDRED_AND_FIVETY = 150;
    public static final int FIVE_HUNDRED = 500;
    public static final int MIN_PASSWORD = 100000;
    public static final int MAX_PASSWORD = 99999999;

    /**
     * 字段
     */
    public static final String ID = "_id";
    //排序字段
    /**
     * mongo
     */
    public static final String MONGO_NE = "$ne";
    public static final String MONGO_SET = "$set";
    public static final String MONGO_AND = "$and";
    public static final String MONGO_OR = "$or";
    public static final String MONGO_IN = "$in";
    public static final String MONGO_NOTIN = "$nin";
    public static final String MONGO_PUSH = "$push";
    public static final String MONGO_PULL = "$pull";
    public static final String MONGO_POP = "$pop";
    public static final String MONGO_EACH = "$each";
    public static final String MONGO_SORT = "$sort";
    public static final String MONGO_SLICE = "$slice";
    public static final String MONGO_MATCH = "$match";
    public static final String MONGO_UNWIND = "$unwind";
    public static final String MONGO_SKIP = "$skip";
    public static final String MONGO_LIMIT = "$limit";
    public static final String MONGO_ADDTOSET = "$addToSet";
    public static final String MONGO_PROJECT = "$project";
    public static final String MONGO_ADD = "$add";
    public static final String MONGO_SUM = "$sum";
    public static final String MONGO_GROUP = "$group";
    public static final String MONGO_REGEX = "$regex";
    public static final String MONGO_OPTIONS = "$options";
    public static final String MONGO_INC = "$inc";
    public static final String MONGO_GEOWITHIN = "$geoWithin";
    public static final String MONGO_CENTER = "$center";
    public static final String MONGO_BOX = "$box";
    public static final String MONGO_GT = "$gt";
    public static final String MONGO_GTE = "$gte";
    public static final String MONGO_LT = "$lt";
    public static final String MONGO_LTE = "$lte";
    public static final String MONGO_ELEMATCH = "$elemMatch";
    public static final String MONGO_PULLALL = "$pullAll";
    public static final String MONGO_PUSHALL = "$pushAll";
    public static final String MONGO_SIZE = "$size";
    public static final String MONGO_EXIST = "$exists";


    public static final BasicDBObject MONGO_SORTBY_ASC = new BasicDBObject("_id", 1);
    public static final BasicDBObject MONGO_SORTBY_DESC = new BasicDBObject("_id", -1);


    public static final int DESC = -1;
    public static final int ASC = 1;

    public static final BasicDBObject QUERY = new BasicDBObject();
    public static final BasicDBObject FIELDS = new BasicDBObject();


    /**
     * 字段
     */
    public static final String FIELD_SYN = "syn";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILD = "FAILD";
    public static final String SUCCESS_CODE = "200";
    public static final String FAILD_CODE = "500";


    /**
     * 最多商品评论个数
     */
    public static final int MAX_GOODS_COMMENT_INT = 500;

    /**
     * 最多课程评论个数
     */
    public static final int MAX_LESSON_COMMENT_INT = 500;

    /**
     * collection
     */
    //地区表
    public static final String COLLECTION_REGION_NAME = "regions";
    //用户表
    public static final String COLLECTION_USER_NAME = "uuser2";
    //第三方登录表
    public static final String COLLECTION_THIRD_LOGIN_NAME = "thirdLogin";
    //学校以及年级
    public static final String COLLECTION_SCHOOL_NAME = "schools";
    //投票选举
    public static final String COLLECTION_ELECT_NAME = "elects";
    //班级
    public static final String COLLECTION_CLASS_NAME = "classes";
    //视频表
    public static final String COLLECTION_VIDEO_NAME = "videos";
    //视频观看记录表
    public static final String COLLECTION_VIDEO_RECORD_NAME = "videorecords";
    //信件表
    public static final String COLLECTION_LETTER_NAME = "letters";
    //信件记录表
    public static final String COLLECTION_LETTER_RECORD_NAME = "letterrecords";
    //博客
    public static final String COLLECTION_BLOG_NAME = "blogs";
    //微课大赛
    public static final String COLLECTION_MATCH_NAME = "match";

    //微课比赛分数
    public static final String COLLECTION_MATCH_LESSON_SCORE_NAME = "lessonscore";
    //微课评分
    public static final String COLLECTION_MATCH_SCORE_NAME = "matchscore";
    //云课程类别
    public static final String COLLECTION_CLOUDLESSONTYPE_NAME = "cloudlessontypes";
    //云课程
    public static final String COLLECTION_CLOUDLESSON_NAME = "cloudlessons";
    //课程
    public static final String COLLECTION_LESSON_NAME = "lessons";
    //目录
    public static final String COLLECTION_DIR_NAME = "dirs";
    //老师班级课程
    public static final String COLLECTION_TEACHERCLASSSUBJECT_NAME = "tcsubjects";
    //班级文档
    public static final String COLLECTION_EXERCISE_NAME = "exercises";
    //班级文档题目
    public static final String COLLECTION_EXERCISE_ITEM_NAME = "exerciseitems";
    //班级文档答案
    public static final String COLLECTION_EXERCISE_ANSWER_NAME = "exerciseanswers";
    //作业
    public static final String COLLECTION_HOMEWORK_NAME = "homeworks";
    //商品
    public static final String COLLECTION_GOODS_NAME = "goods";
    //订单
    public static final String COLLECTION_ORDERS_NAME = "orders";
    //提现
    public static final String COLLECTION_WITHDRAW_CASH_NAME = "withdrawCash";
    //日历事件
    public static final String COLLECTION_EVENT_NAME = "events";
    //日历循环事件
    public static final String COLLECTION_LOOPEVENT_NAME = "loopevents";
    //用户积分
    public static final String COLLECTION_USER_EXPER_LOG_NAME = "uexperlogs";
    //用户学年积分
    public static final String COLLECTION_USER_SCHOOL_YEAR_EXPER_NAME = "uschoolyearexperlogs";
    //联盟资源
    public static final String COLLECTION_LEAGUE_NAME = "leagues";
    //通知
    public static final String COLLECTION_NOTICE_NAME = "notices";
    //通知阅读表
    public static final String COLLECTION_NOTICE_READ_NAME = "noticesreads";
    //题库
    public static final String COLLECTION_ITEM_POOL = "itempool";
    //用户题库
    public static final String COLLECTION_USER_ITEM_POOL = "useritempool";
    //组卷
    public static final String COLLECTION_TEST_PAPER = "testpapers";
    //学生练习
    public static final String COLLECTION_STUDENT_EXERCISE = "studentexercise";
    //学生错题库
    public static final String COLLECTION_STUDENT_ERROR_ITEM = "erroritems";
    //系统版本
    public static final String COLLECTION_VERSION = "versions";
    //资源字典表
    public static final String COLLECTION_RESOURCES_DICTIONARY = "resdictionary";
    //资源字典表
    public static final String COLLECTION_RESOURCES = "resources";
    //部门
    public static final String COLLECTION_DEPARTMENT = "departments";
    //互动课堂
    public static final String COLLECTION_INTERACTLESSON = "interactLesson";
    //互动课堂考试
    public static final String COLLECTION_INTERACTLESSON_EXAM = "interactLessonExam";
    //互动课堂考试明细
    public static final String COLLECTION_INTERACTLESSON_EXAM_DETAIL = "interactLessonExamDetail";
    //互动课堂考试成绩分布
    public static final String COLLECTION_INTERACTLESSON_SCORE_CLASSIFY = "interactLessonScoreClassify";
    //互动课堂快速答题
    public static final String COLLECTION_INTERACTLESSON_QUICK_ANSWER = "interactLessonQuickAnswer";
    //互动课堂文件
    public static final String COLLECTION_INTERACTLESSON_FILE = "interactLessonFile";

    //互动课堂-活跃度
    public static final String COLLECTION_ACTIVINESS = "activiness";

    //考试成绩
    public static final String COLLECTION_EXAMRESULT_NAME = "examresult";
    //成绩列表
    public static final String COLLECTION_PERFORMANCE_NAME = "performance";
    //校区管理
    public static final String COLLECTION_CAMPUS_NAME = "campus";
    //提醒
    public static final String COLLECTION_REMIDNER_NAME = "reminder";
    //兴趣班分类
    public static final String COLLECTION_INTERESTCATEGORY_NAME = "interestcategory";
    //兴趣班分类
    public static final String COLLECTION_INTERESTTERMS_NAME = "interestterms";

    public static final String COLLECTION_App_Banners = "appBanner";

    public static final String COLLECTION_Banners = "banner";


    //好友
    public static final String COLLECTION_FRIEND_NAME = "friend";
    //添加好友的申请
    public static final String COLLECTION_FRIEND_APPLY_NAME = "friendApply";
    //活动邀请
    public static final String COLLECTION_ACTIVITY_INVITATION_NAME = "actInvitation";
    //活动
    public static final String COLLECTION_ACTIVITY_NAME = "activity";
    //活动动态
    public static final String COLLECTION_ACTIVITY_TRACK_NAME = "actTrack";

    //投票选举
    public static final String COLLECTION_QUESTIONNAIRE_NAME = "questionnaire";
    //新闻
    public static final String COLLECTION_NEWS_NAME = "news";
    //用户宠物
    public static final String COLLECTION_USER_PET = "userPets";
    //宠物类型
    public static final String COLLECTION_PET_TYPE = "petType";
    //兴趣班
    public static final String COLLECTION_INTEREST_CLASS_NAME = "interestClass";
    //兴趣班课程 学生课时得分表
    public static final String COLLECTION_INTEREST_LESSON_SCORE_NAME = "interestClassLessonScore";
    //兴趣班学生 期末成绩单
    public static final String COLLECTION_INTEREST_TRANSCRIPT = "interestClassTranscript";
    //群组
    public static final String COLLECTION_GROUPS_NAME = "groups";
    //群文件
    public static final String COLLECTION_GROUPFILE_NAME = "groupfile";
    //群聊天记录
    public static final String COLLECTION_GROUPSCHAT_NAME = "groupchat";
    //用户活动日志
    public static final String COLLECTION_USER_ACTION_LOG = "userActionLog";
    //教育局管理
    public static final String COLLECTION_EDUCATION_BUREAU = "educationBureau";
    //配置管理
    public static final String COLLECTION_SYSTEM_CONFIG = "systemConfig";
    //校园安全
    public static final String COLLECTION_SCHOOL_SECURITY = "schoolSecurity";
    //新闻栏目
    public static final String COLLECTION_NEWS_COLUMN = "newscolumn";
    //德育项目管理
    public static final String COLLECTION_MORAL_CULTURE_MANAGE = "moralCultureManage";
    //德育项目成绩
    public static final String COLLECTION_MORAL_CULTURE_SCORE = "moralCultureScore";
    //考勤管理
    public static final String COLLECTION_ATTENDANCE_NAME = "attendance";

    //运费模板
    public static final String COLLECTION_EXPRESS_TEMPLATE_NAME = "eexpresstemplate";

    //奖惩信息
    public static final String COLLECTION_REWARD = "reward";
    public static final String COLLECTION_REPAIR = "repair";
    // 工资项目数据
    public static final String COLLECTION_SALARY_ITEM = "salaryItem";
    // 工资数据
    public static final String COLLECTION_SALARY = "salary";

    //考试信息
    public static final String COLLECTION_EXAM = "examresult";
    //考试成绩
    public static final String COLLECTION_SCORE = "performance";
    //区域联考
    public static final String COLLECTION_JOINTEXAM = "jointexam";
    //区域联考汇总
    public static final String COLLECTION_EXAMSUMMARY = "examsummary";
    //等级设置
    public static final String COLLECTION_LEVEL = "level";
    //考场资源
    public static final String COLLECTION_EXAM_ROOM = "examroom";
    //校园资产
    public static final String COLLECTION_PROPERTY = "property";
    //校园资产分类
    public static final String COLLECTION_PROPERTY_CLASSIFICATION = "propertyclassification";
    //器材管理
    public static final String COLLECTION_EQUIPMENT = "equipment";
    //器材管理分类
    public static final String COLLECTION_EQUIPMENT_CLASSIFICATION = "equipmentclassification";

    //评比
    public static final String COLLECTION_COMPETITION = "competition";
    //评比明细
    public static final String COLLECTION_COMPETITION_DETAIL = "competitiondetail";
    //评比分数
    public static final String COLLECTION_COMPETITION_SCORE = "competitionscore";

    //考试小分类型表
    public static final String COLLECTION_EXAM_ITEM = "examitem";
    //考试小分份数表
    public static final String COLLECTION_EXAM_ITEM_SCORE = "examitemscore";

    public static final String[] CHINESE_WEEK = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    // end ycode

    //火热活动
    public static final String COLLECTION_FIERY_ACTIVITY_NAME = "fieryactivitys";

    //公文流转
    public static final String COLLECTION_DOC_FLOW_NAME = "docflow";

    //评论
    public static final String COLLECTION_COMMENT = "comment";

    //用户基本信息
    public static final String COLLECTION_USER_BASE_INFO = "userbaseinfo";

    //学籍信息

    public static final String COLLECTION_REGISTRATION = "registration";
    //走班课表配置
    public static final String COLLECTION_COURSE_CONF = "zb_courseconf";
    //走班课表
    public static final String COLLECTION_COURSE_TABLE = "zb_coursetable";

    //选课
    public static final String COLLECTION_XUANKE_CONF = "zb_xuankeconf";

    //选课科目
    public static final String COLLECTION_SUBJECT_CONF = "zb_subjectconf";

    //学生选课结果
    public static final String COLLECTION_STUDENT_CHOOSE = "zb_studentchoose";


    //学生分段
    public static final String COLLECTION_FENGDUAN = "zb_fengduan";

    //走班课程配置
    public static final String COLLECTION_ZOUBAN_COURSE = "zb_zoubancourse";

    //教室
    public static final String COLLECTION_CLASSROOM = "classroom";

    //班级排名
    public static final String COLLECTION_CLASS_PAIMING = "classpaiming";

    //分层
    public static final String COLLECTION_FENCENG = "fenceng";

    //走班状态
    public static final String COLLECTION_ZOUBAN_STATE = "zb_state";

    //走班冲突
    public static final String COLLECTION_ZOUBAN_CONFLICT = "zb_conflict";

    //走班教学周
    public static final String COLLECTION_ZOUBAN_TERMWEEK = "zb_term";

    //走班调课通知
    public static final String COLLECTION_ZOUBAN_NOTICE = "zb_notice";

    //走班-考勤
    public static final String COLLECTION_ZOUBAN_ATTENDANCE = "zb_attendance";

    //记录-群组社区和群组表转换的记录（旧数据处理）
    public static final String COLLECTION_TRANSFER_COMMUNITY_RECORD="transfer_community_record";

    //活动报名和投票新旧数据转换的记录表
    public static final String COLLECTION_JXM_TRANSFER_VOTE_AND_ACTIVITY="jxm_transfer_vote_and_activity";

    //聊天记录
    public static final String COLLECTION_JXM_GROUP_CHAT_RECORD="jxm_group_chat_record";

    //记录聊天的群组或者私人列表
    public static final String COLLECTION_JXM_RECORD_CHAT_PERSONAL="jxm_record_chat_user";

    //记录每个人的聊天列表的数目
    public static final String COLLECTION_JXM_RECORD_TOTAL_CHAT="jxm_record_total_chat";

    //活动报名
    public static final String COLLECTION_JXM_APP_ACTIVITY="jxm_app_activity";

    //活动报名报名人员列表
    public static final String COLLECTION_JXM_APP_ACTIVITY_USER="jxm_app_activity_user";

    //老师端录入的要转移的学生列表
    public static final String COLLECTION_JXM_RECORD_USER_UNBIND="jxm_record_user_unbind";

    //家长端要接收的学生列表
    public static final String COLLECTION_JXM_RECORD_PARENT_IMPORT="jxm_record_parent_import";

    //发起群收款条目
    public static final String COLLECTION_JXM_GROUP_COLLECT_MONEY="jxm_group_collect_money";

    //发起群收款用户发起的钱款
    public static final String COLLECTION_JXM_GROUP_USER_EXPENSE="jxm_group_user_expense";

    //保存用户加密的支付宝账号信息
    public static final String COLLECTION_JXM_APLIPAY_USER="jxm_aplipay_user";


    //------------------------------------------------------------------论坛

    //论坛-帖子
    public static final String COLLECTION_FORUM_POST = "fpost";
    //论坛-回帖
    public static final String COLLECTION_FORUM_REPLY = "freply";
    //论坛-版块
    public static final String COLLECTION_FORUM_SECTION = "fsection";
    //论坛-收藏
    public static final String COLLECTION_FORUM_COLLECTION = "fcollection";
    //论坛-任务
    public static final String COLLECTION_FORUM_MISSION = "fmission";
    //论坛-签到
    public static final String COLLECTION_FORUM_SIGN = "fsign";
    //论坛-记录
    public static final String COLLECTION_FORUM_RECORD = "frecord";
    //论坛-消息
    public static final String COLLECTION_FORUM_INFORMATION = "foruminf";
    //论坛-等级
    public static final String COLLECTION_FORUM_LEVEL = "flevel";
    //论坛-记录邀请数
    public static final String COLLECTION_FORUM_INVITATION = "finvitation";
    //论坛-投票贴记录
    public static final String COLLECTION_FORUM_VOTE = "fvote";
    //论坛-访问日志记录
    public static final String COLLECTION_FORUM_LOG = "flog";
    //论坛-积分记录表
    public static final String COLLECTION_FORUM_SCORE = "fscore";

    public static final String COLLECTION_USER_MOBILE = "userMobile";

    //大赛人员记录表
    public static final String COLLECTION_FORUM_PARTICIPATE="fparticipate";



    //==========================第二版新加的表=================================

    //找玩伴
    public static final String COLLECTION_FORUM_ACTIVITY = "factivity";

    public static final String COLLECTION_FORUM_F_MATETYPE = "fmatetype";

    public static final String COLLECTION_FORM_SIGN_ACTIVITY_SHEET = "factivitySignSheet";

    public static final String COLLECTION_FORUM_MATE_SEEKMATE = "fmateSeekMate";


    //位置
    //登录日志
    public static final String COLLECTION_FORUM_LOGIN_LOG = "floginlog";

    //家校互动
    public static final String COLLECTION_FORUM_COMMUNITY = "fcommunity";

    public static final String COLLECTION_FORUM_COMMUNITY_GROUP = "fgroup";

    public static final String COLLECTION_FORUM_COMMUNITY_MEMBER = "fmember";

    public static final String COLLECTION_FORUM_COMMUNITY_DETAIL = "fcommunitydetail";

    public static final String COLLECTION_FPRUM_LATEST_GROUP_DYNAMIC = "latestgroupdynamic";
    
    public static final String COLLECTION_FORUM_COMMUNITY_HY = "fcommunityHy";

    //家校互动附件表
    public static final String COLLECTION_FORUM_COMMUNITY_ATTACHMENT = "communityattachment";

    //社区id生成 seq
    public static final String COLLECTION_FORUM_COMMUNITY_SEQ = "fcommunityseq";

    //备注名表
    public static final String COLLECTION_FORUM_REMARK = "fremark";
    //关注人信息表
    public static final String COLLECTION_FORUM_CONCERNED = "fconcern";

    public static final String COLLECTION_FORUM_PARTINCONTENT = "partInContent";

    public static final String COLLECTION_FORUM_MINE_COMMUNITY = "mineCommunitys";

    public static final String COLLECTION_FORUM_GROUPANNOUNCE = "fgroupAnnounce";

    public static final String COLLECTION_FORUM_COMMUNITY_SYSTEMINFO = "fcomunitySysInfo";

    public static final String COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO="communityValidateInfo";

    //找培训
    public static final String COLLECTION_TRAIN_INSTITUTE="institute";

    public static final String COLLECTION_TRAIN_ITEMTYPENAME="itemTypeName";

    public static final String COLLECTION_TRAIN_REGIONS="regions";

    public static final String COLLECTION_TRAIN_COMMENT="trainComment";

    //(上线前的数据处理)
    public static final String COLLECTION_TRAIN_INSTITUTE_COPY="institute_copy";

    public static final String COLLECTION_TRAIN_REGIONS_COPY="regions_copy";

    public static final String COLLECTION_TRAIN_ITEMTYPENAME_COPY="itemTypeName_copy";
    //亲子活动
    public static final String COLLECTION_PARENT_CHILD_ACTIVITY="parentChild";

    //留言反馈功能
    public static final String COLLECTION_FEEDBACK_CONTENT="feedbackcontent";

    //新版本存储用户身份功能
    public static final String COLLECTION_NEW_VERSION_USER_ROLE="newversion_userrole";
    //新版本存储用户绑定关系
    public static final String COLLECTION_NEW_VERSION_BIND_RELATION="newversion_bindrelation";
    //新版本用户学年年级关系
    public static final String COLLECTION_NEW_VERSION_GRADE="newversion_grade";
    //新版本绑定学科的关系表
    public static final String COLLECTION_NEW_VERSION_SUBJECT="newversion_subject";
    //新版本绑定社区中家长与孩子的关系表
    public static final String COLLECTION_NEW_VERSION_COMMUNITY_BIND="newversion_community_bind";

    public static final String COLLECTION_NEW_VERSION_APP_NOTICE="newversion_app_notice";

    public static final String COLLECTION_NEW_VERSION_APP_VOTE="newversion_app_vote";

    public static final String COLLECTION_MAKE_OUT_USER_RELATION="make_out_user_relation";

    //应用市场
    public static final String COLLECTION_APP_MARKET_DETAIL="app_market_detail";

    public static final String COLLECTION_APP_MARKET_DETAIL_STAR_STATISTIC="app_market_detail_star_sts";

    public static final String COLLECTION_APP_MARKET_DETAIL_COMMENT="app_market_detail_comment";

    public static final String COLLECTION_USER_ACTIVE_RECORD="user_active_record";


    public static final String COLLECTION_MALL_MARKET_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.fulan.mall";

    //新加的token-user表
    public static final String COLLECTION_TOKEN_USER_LOGIN="token_user_login";

    //---------------------------------------------------------------------------------------------论坛


    //资源-附件
    public static final String COLLECTION_COURSWARE = "coursware";
    public static final String COLLECTION_CLOUD_RESOURCES = "resourcescloud";
    //评课议课
    public static final String COLLECTION_REVIEW = "review";
    //集体备课
    public static final String COLLECTION_PREPARATION = "preparation";
    //课题研究
    public static final String COLLECTION_PROJECT = "project";
    //课题研究子课题
    public static final String COLLECTION_SUB_PROJECT = "subproject";
    //科研成果
    public static final String COLLECTION_ACHIEVEMENT = "achievement";
    //教研
    public static final String COLLECTION_TEACHING_AND_RESEARCH = "teachresearch";
    //成长档案
    public static final String COLLECTION_GROWTH_RECORD = "growth";
    //题库
    public static final String COLLECTION_ITEM__STORE = "itemstore";
    //复杂评论
    public static final String COLLECTION_COMPLEX_COMMENT = "complexcomment";


    //区域联考汇总表
    public static final String COLLECTION_EXAM_SUMMARY = "examsummary";
    //区域联考表
    public static final String COLLECTION_REGIONAL_EXAM = "jointexam";
    //家庭成员
    public static final String COLLECTION_FAMILY_MEMBER = "familymenmber";


    //学习简历
    public static final String COLLECTION_LEANING_RESUME = "learningresume";


    //评价项目(素质教育)
    public static final String COLLECTION_QUALITY = "quality";

    //评价子项目(素质教育)
    public static final String COLLECTION_SUB_QUALITY = "subquality";


    //门卫管理进校
    public static final String COLLECTION_STUDENT_ENTER = "studententer";
    //门卫管理出校
    public static final String COLLECTION_STUDENT_OUT = "studentout";
    //来访登记
    public static final String COLLECTION_VISIT = "visit";
    //宿舍表（caotiecheng）
    public static final String COLLECTION_DORM = "dorm";
    //宿舍区
    public static final String COLLECTION_DORM_AREA = "dormarea";
    //宿舍楼
    public static final String COLLECTION_DORM_BUILDING = "dormbuilding";
    //宿舍层
    public static final String COLLECTION_DORM_FLOOR = "dormfloor";
    //宿舍学生表
    public static final String COLLECTION_DORM_STUDENT = "dormstudent";
    //微博话题
    public static final String COLLECTION_BLOG_THEME_NAME = "theme";
    //功能教室
    public static final String COLLECTION_FUNCTION_CLASSROOM = "classroom";
    //功能教室预约
    public static final String COLLECTION_CLASSROOM_APPOINTMENT = "classroomappointment";
    //ebusiness
    //电子商务商品
    public static final String COLLECTION_EBUSINESS_GOODS = "egoods";
    //电子商务商品评论表
    public static final String COLLECTION_EBUSINESS_GOODS_COMMENT = "ecomment";
    //电子商务订单
    public static final String COLLECTION_EBUSINESS_ORDERS = "eorder";
    //电子商务订单地址
    public static final String COLLECTION_EBUSINESS_ORDERSADDRESS = "eoaddress";
    //电子商务商品日志
    public static final String COLLECTION_EBUSINESS_GOODSLOG = "egoodslog";

    //电子商务商品分类
    public static final String COLLECTION_EBUSINESS_GOODSCATEGORY = "egoodscategory";
    //电子商务年龄分类
    public static final String COLLECTION_EBUSINESS_AGECATEGORY = "eagecategory";
    //电子商务年级分类
    public static final String COLLECTION_EBUSINESS_GRADECATEGORY = "egradecategory";
    //电子商务抵用券
    public static final String COLLECTION_EBUSINESS_VOUCHER = "evoucher";
    //商品分类简介视频
    public static final String COLLECTION_EBUSINESS_CATEGORYVIDEO = "ecategoryVideo";


    //定时任务
    public static final String COLLECTION_CRONTAB = "crontab";


    //来凤 教师请假
    public static final String COLLECTION_TEACHER_LEAVE = "teacherLeave";
    //来凤  老师代课
    public static final String COLLECTION_REPLACE_COURSE = "replaceCourse";
    //教师课程项目
    public static final String COLLECTION_COURSEPROJECT = "courseproject";
    //教师简历
    public static final String COLLECTION_RESUME = "resume";
    //教师职称信息
    public static final String COLLECTION_TITLE = "title";
    //教师行政职务
    public static final String COLLECTION_POSTION = "postion";
    //教师成果信息
    public static final String COLLECTION_RESULT = "result";
    //教师学历信息
    public static final String COLLECTION_EDUCATION = "education";
    //教师工作信息
    public static final String COLLECTION_JOB = "job";
    //教师社会兼职信息
    public static final String COLLECTION_PARTTIME = "parttime";
    //教师继续教育信息
    public static final String COLLECTION_CONTINUEEDUCATION = "continueedu";
    //教师证书信息
    public static final String COLLECTION_CERTIFICATE = "certificate";


    public static final String DEPARTMENT_GUARD = "保卫科";
    //宿舍管理中心
    public static final String DEPARTMENT_DORM = "宿舍管理中心";
    //功能教室管理中心
    public static final String DEPARTMENT_FUNCTION_CLASS = "功能教室管理中心";
    //是否有走班配置
    public static final String COLLECTION_ZOUBAN_CONFIG = "zb_config";


    //导航条目
    public static final String COLLECTION_NAV_ITEM = "navitems";
    //导航
    public static final String COLLECTION_NAV = "navs";
    //党建资源表
    public static final String COLLECTION_PARTYRESOURCE = "partyres";
    //用户log
    public static final String COLLECTION_USER_LOG_NAME = "ulog";

    //========教师评价=============
    //人员分组
    public static final String COLLECTION_TE_MEMBERGROUP = "te_memberGroup";
    //评分比重
    public static final String COLLECTION_TE_PROPORTION = "te_proportion";
    //考核要素+量化成绩
    public static final String COLLECTION_TE_ELEMENT = "te_element";
    //教师评价设置  包含  等第设置 评分时间 评比规则
    public static final String COLLECTION_TE_SETTING = "te_setting";
    //考核详情
    public static final String COLLECTION_TE_ITEM = "te_item";

    //日志报表邮箱管理
    public static final String COLLECTION_EMAIL_MANAGE = "emailmanage";

    //H5作业
    //作业评论
    public static final String COLLECTION_APP_COMMENT = "appcomment";
    //作业
    public static final String COLLECTION_APP_OPERATION = "appoperation";
    //评论通用表
    public static final String COLLECTION_TOPIC_COMMENT = "jxm_topic_comment";
    //作业记录（天）
    public static final String COLLECTION_APP_RECORD = "apprecord";
    //作业记录（次）
    public static final String COLLECTION_APP_RECORD_RESULT = "apprecordresult";
    //虚拟提交关系
    public static final String COLLECTION_PARENT_CHILD_CONNECTION = "jxm_parent_child_connection";
    //错题本
    public static final String COLLECTION_BOOK_ERROR = "bookError";
    //名称转换
    public static final String COLLECTION_NAME_CONVERT = "nameConvert";
    //简易科目表
    public static final String COLLECTION_SUBJECT_CLASS = "subjectClass";
    //临时年级表
    public static final String COLLECTION_CREATE_GRADE = "createGrade";
    //学校表
    public static final String COLLECTION_HOME_SCHOOL = "homeschool";
    //社区学校中间表
    public static final String COLLECTION_COMMUNITY_SCHOOL = "communityschool";
    //简易问题类型表
    public static final String COLLECTION_QUESTION_TYPE = "questionnewtype";
    //简易测试类型表
    public static final String COLLECTION_TEST_TYPE = "testtype";
    //简易考试类型表
    public static final String COLLECTION_EXAM_TYPE = "examtype";
    //红点显示
    public static final String COLLECTION_RED_DOT = "reddot";
    //好友点击状态
    public static final String COLLECTION_FRIENDSTATUS = "friendStatus";
    //社区学校用户管理表
    public static final String COLLECTION_SCHOOL_PERSON = "schoolperson";

    //校级功能表
    public static final String COLLECTION_SCHOOL_FUNCTION = "schoolfunction";

    //复兰小课堂
    //小课程表
    public static final String COLLECTION_SMALL_LESSON = "smalllesson";
    //小课程用户活跃表
    public static final String COLLECTION_LESSON_RESULT = "lessonuserresult";
    //小课程用户答案
    public static final String COLLECTION_LESSON_ANSWER = "lessonuseranswer";
    //小课程用户编码表
    public static final String COLLECTION_SMALL_LESSON_USER_CODE = "smalllesson_user_code";

    public static final String COLLECTION_SMALL_LESSON_CODE = "smalllesson_code";
    //老师绑定学科表
    public static final String COLLECTION_TEACHER_SUBJECT_BIND="tea_subject_bind";

    /**------------------------成绩单----------------------**/
    public static final String COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL="report_card_group_exam";
    
    public static final String COLLECTION_MULTI_REPORT_CARD_GROUP_EXAM_DETAIL="multi_report_card_group_exam";

    public static final String COLLECTION_REPORT_CARD_RECORD_EXAM_SCORE="report_card_rec_exam_sc";

    public static final String COLLECTION_REPORT_CARD_RECORD_SCORE_EVALUATE="report_card_rec_sc_eva";

    public static final String COLLECTION_REPORT_CARD_RECORD_LEVEL_EVALUATE="report_card_rec_lv_eva";

    public static final String COLLECTION_REPORT_CARD_EXAM_USER_RECORD="report_card_exam_user_rec";

    public static final String COLLECTION_REPORT_CARD_EXAM_VERSION="report_card_exam_version";

    public static final String COLLECTION_REPORT_CARD_VIRTUAL_USER="report_card_virtual_user";

    public static final String COLLECTION_REPORT_CARD_VIRTUAL_COMMUNITY="report_card_virtual_community";

    public static final String COLLECTION_REPORT_CARD_SIGN="report_card_sign";

    public static final String COLLECTION_WEB_HOME_PAGE_RECORD="web_home_page_record";

    public static final String COLLECTION_JXM_GENERATE_USER_CODE="jxm_gene_user_code";
    
    public static final String COLLECTION_SCORE_REPRESENT = "score_represent";
    /**------------------------成绩单----------------------**/

    /**------------------------新参考资料----------------------**/
    //新参考资料
    public static final String COLLECTION_REFERENCE_DATA="referencedata";

    /**------------------------错题本----------------------**/
    //首页记录表
    public static final String COLLECTION_INDEX_PAGE="indexpage";
    //首页内容表
    public static final String COLLECTION_INDEX_CONTENT="jxm_index_Content";
    //错题本2.0
    public static final String COLLECTION_QUESTION_BOOK = "questionbook";
    //解析表
    public static final String COLLECTION_QUESTION_ADDITION = "questionnewaddition";
    //自定义表签
    public static final String COLLECTION_QUESTION_TAGS = "questiontags";
    //自定义表签
    public static final String COLLECTION_QUESTION_WEB_TEST = "questionwebtest";
    //已阅记录查看表
    public static final String COLLECTION_READ_BOOK = "questionreadbook";
    //虚拟用户与孩子关联
    public static final String COLLECTION_VIRTUAL_CONNECT = "virtualconnect";
    /**--------------------------管控--------------------------**/

    //管控手机号
    public static final String COLLECTION_CONTROL_PHONE = "controlphone";
    //管控社区推荐应用表
    public static final String COLLECTION_CONTROL_APP = "controlapp";
  //管控引用推送时间
    public static final String COLLECTION_CONTROL_APP_TS = "controlappTs";
    //管控家长推荐应用表
    public static final String COLLECTION_CONTROL_PARENT_APP = "controlparentapp";
    //管控防沉迷时间设置
    public static final String COLLECTION_CONTROL_TIME = "controltime";
    //管控地图信息记录表
    public static final String COLLECTION_CONTROL_MAP = "controlmap";
    //管控应用使用记录表
    public static final String COLLECTION_CONTROL_APP_RESULT = "controlappresult_2018-12-26";
    //管控应用使用记录缓存表
    public static final String COLLECTION_CONTROL_APP_RESULT_CURRENT = "controlappresult_current2";
    //信息表
    public static final String COLLECTION_CONTROL_MESSAGE= "controlmessage";
    //管控时间设置表
    public static final String COLLECTION_CONTROL_SCHOOL_TIME= "controlschooltime";
    //管控时间设置表
    public static final String COLLECTION_CONTROL_NOW_TIME= "controlnowtime";
    //管控学生默认配置
    public static final String COLLECTION_CONTROL_BACK_TIME= "controlbacktime";
    //管控时间选项默认配置
    public static final String COLLECTION_CONTROL_SET_TIME= "controlsettime";
    //系统默认推送应用列表
    public static final String COLLECTION_CONTROL_APP_SYSTEM= "controlappsystem";
    //系统默认推送应用列表
    public static final String COLLECTION_CONTROL_VERSION= "controlversion";
    //管控学生数据缓存表
    public static final String COLLECTION_CONTROL_STUDENT_RESULT= "controlstudentresult";

    //管控学生数据缓存表
    public static final String COLLECTION_CONTROL_SIMPLE= "controlsimple";
    //家管控分享
    public static final String COLLECTION_CONTROL_SHARE = "controlshare";
    //校管控社群应用状态表
    public static final String COLLECTION_CONTROL_APP_SCHOOL_RESULT="controlappschoolresult";
    //校管控社群用户操作记录表
    public static final String COLLECTION_CONTROL_APP_SCHOOL_USER="controlappschooluser";
    //校管控默认应用状态表
    public static final String COLLECTION_CONTROL_APP_SCHOOL="controlappschool";

    /*********************************后台管理*******************************************/
    //图片鉴黄和垃圾文本
    public static final String COLLECTION_PICTURE_TEXT= "picturetext";
    //教师认证
    public static final String COLLECTION_TEACHER_APPROVE= "teacherapprove";
    //复兰应用版本
    public static final String COLLECTION_JXM_APP_VERSION= "jxmappversion";
    //用户角色管理
    public static final String COLLECTION_USER_LOG_RESULT="jxm_user_log_result";
    //角色对应的路径管理
    public static final String COLLECTION_USER_ROLE_OF_PATH="jxm_user_role_of_path";
    //后台日志管理
    public static final String COLLECTION_LOG_MESSAGE = "logmessage";
    //系统推送消息
    public static final String COLLECTION_SYSTEM_MESSAGE = "systemmessage";
    //消息提醒时间记录
    public static final String COLLECTION_MESSAGE_DATE = "systemmessagedate";

    //校本资源表
    public static final String COLLECTION_SCHOOL_BOOK_APP = "schoolBookApp";

    /*************************************运营管理*****************************************/
    //运营表
    public static final String COLLECTION_BUSINESS_MANAGE= "businessManage";
    //运营角色表
    public static final String COLLECTION_BUSINESS_ROLE= "businessRole";
    //常用应用表
    public static final String COLLECTION_MODULE_NUMBER= "moduleNumber";
    //系统版本
    public static final String COLLECTION_JIA_VERSION = "jiaversions";
    //用户积分表
    public static final String COLLECTION_INTEGRAL_SUFFER= "integralsuffer";
    //积分记录表
    public static final String COLLECTION_INTEGRAL_RECORD= "integralrecord";
    //版本控制开关
    public static final String COLLECTION_VERSION_OPEN= "jxm_version_open";
    //操作统计
    public static final String COLLECTION_MODULE_TIME= "jxm_module_time";
    //模块禁止表
    public static final String COLLECTION_MODULE_BANNING= "jxm_module_banning";
    //社群禁止表
    public static final String COLLECTION_COMMUNITY_BANNING= "jxm_community_banning";
    //jpush发送表
    public static final String COLLECTION_PUSH_MESSAGE= "jxm_push_message";
    //用户协议表
    public static final String COLLECTION_USER_AGREEMENT= "jxm_user_agreement";

    public static final String COLLECTION_VALIDATE_GROUP_INFO = "jxm_validate_group_info";
    /*************************************精品课*****************************************/

    //精品课程
    public static final String COLLECTION_EXCELLENT_COURSES = "excellent_new_courses";
    //精品课时
    public static final String COLLECTION_HOUR_CLASS= "excellent_new_hour_class";
    //用户订单
    public static final String COLLECTION_CLASS_ORDER= "excellent_new_class_order";
    //用户行为
    public static final String COLLECTION_USER_BEHAVIOR= "excellent_new_user_behavior";
    //CC直播间
    public static final String COLLECTION_COURSES_ROOM= "excellent_new_courses_room";
    //用户美豆消费记录
    public static final String COLLECTION_RECHARGE_RESULT= "excellent_new_recharge_result";
    //用户退课记录
    public static final String COLLECTION_CLASS_REFUND= "excellent_new_class_refund";
    //用户提现记录
    public static final String COLLECTION_EXTRACT_CASH= "excellent_new_extract_cash";
    //用户充值账户记录
    public static final String COLLECTION_USER_ACCOUNT= "excellent_new_user_account";
    //用户充值消费日志
    public static final String COLLECTION_ACCOUNT_LOG= "excellent_new_account_log";
    //用户充值订单
    public static final String COLLECTION_ACCOUNT_ORDER= "excellent_new_account_order";
    //用户充值账户
    public static final String COLLECTION_ACCOUNT_FRASH= "excellent_new_account_frash";
    //后台订单管理
    public static final String COLLECTION_BACK_ORDER= "excellent_back_order";
    //cc同步数据
    public static final String COLLECTION_CC_LOGIN= "excellent_cc_login";
    //用户登陆信息
    public static final String COLLECTION_USER_DESC= "excellent_user_class_desc";
    //附加信息
    public static final String COLLECTION_COURSES_BUSINESS="excellent_courses_business";
    //赶考网对接支付通知
    public static final String COLLECTION_GAN_KAO_PAY="excellent_gankao_pay";
    //每日订单
    public static final String COLLECTION_COURSES_ORDER_RESULT = "excellent_courses_order_result";
    //用户课程直播间表
    public static final String COLLECTION_USER_CLASS_ROOM= "excellent_user_class_room";
    /*************************************积分商城*****************************************/
    
    //商品
    public static final String COLLECTION_INTEGRAL_GOODS = "integral_goods";
    //地址
    public static final String COLLECTION_INTEGRAL_ADDRESS = "integral_address";
    //订单
    public static final String COLLECTION_INTEGRAL_ORDER = "integral_order";
    
    /*************************************小兰客服*****************************************/
  //常见问题
    public static final String COLLECTION_COMMON_QUESTION = "common_question";
    //退换货
    public static final String COLLECTION_MOBILE_RETURN = "mobile_return";
//    //维修价目表 合到COLLECTION_MONETARY_GOODS
//    public static final String COLLECTION_PHONE_REPAIR_COST_DICTIONARY = "jxm_phone_repair_cost_dictionary";

    /*************************************小兰手机商城*****************************************/

    //商品
    public static final String COLLECTION_MONETARY_GOODS = "monetary_goods";
    //地址
    public static final String COLLECTION_MONETARY_ADDRESS = "monetary_address";
    //订单
    public static final String COLLECTION_MONETARY_ORDER = "monetary_order";

    /*************************************后台权限*****************************************/
    public static final String COLLECTION_USER_ROLE_JURISDICTION = "jxm_user_role_jurisdiction";
    public static final String COLLECTION_ROLE_JURISDICTION_SETTING = "jxm_role_jurisdiction_setting";
    public static final String COLLECTION_JURISDICTION_TREE = "jxm_jurisdiction_tree";

    /*************************************库存管理*****************************************/
    /*库存表*/
    public static final String COLLECTION_PHONES_STORAGE_MANAGE = "jxm_phones_storage_manage";
    /*出入库记录表*/
    public static final String COLLECTION_PHONES_IN_OUT_STORAGE_RECORD = "jxm_phones_in_out_storage_record";
    /*家校美手机项目表*/
    public static final String COLLECTION_PHONES_PROJECT = "jxm_phones_project";

    /*************************************新版运营校管控*****************************************/
    /*校管控*/
    public static final String COLLECTION_SCHOOL_CONTROL_TIME = "jxm_school_control_time";

    public static final String COLLECTION_HOME_CONTROL_TIME = "jxm_home_control_time";
    /*****************************************新版投票**********************************************/
    //投票内容
    public static final String COLLECTION_APP_NEW_VOTE="jxm_app_new_vote";
    //投票选项
    public static final String COLLECTION_APP_VOTE_OPTION="jxm_app_vote_option";

    /*****************************************拓展选课*********************************************/
    //拓展课学校设置
    public static final String COLLECTION_EXTENDED_SCHOOL_SETTING = "jxm_extended_school_setting";
    //拓展课课程标签
    public static final String COLLECTION_EXTENDED_SCHOOL_LABEL = "jxm_extended_school_label";
    //拓展课课程老师
    public static final String COLLECTION_EXTENDED_SCHOOL_TEACHER = "jxm_extended_school_teacher";
    //拓展课
    public static final String COLLECTION_EXTENDED_COURSE = "jxm_extended_course";
    //拓展课节
    public static final String COLLECTION_EXTENDED_COURSE_CLASS = "jxm_extended_course_class";
    //拓展课报名申请
    public static final String COLLECTION_EXTENDED_USER_APPLY = "jxm_extended_user_apply";
}
