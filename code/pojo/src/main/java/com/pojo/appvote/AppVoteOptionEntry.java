package com.pojo.appvote;

import com.pojo.base.BaseDBObject;

/**
 * Created by James on 2018-10-26.
 *      新投票选项类
 *
 *      voteId              投票id                vid
 *      description         投票选项              des
 *      userId              投票选项所属人        uid
 *      type                选项类型              typ       1  普通选项       2 自选选项
 *      List<VideoEntry> videoList,                        vl//视频
 *      List<AttachmentEntry> attachmentEntries,           ats//文件
 *      List<AttachmentEntry> imageList                    il//图片
 *      List<AttachmentEntry> voiceList                    vt//语音
 *      count               投票人数                       cot
 *      userList<Ob >       投票列表                       ult
 *
 */
public class AppVoteOptionEntry extends BaseDBObject {

}
