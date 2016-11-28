package com.fulaan.service;

import com.db.fcommunity.AttachmentDao;
import com.pojo.fcommunity.AttachmentEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2016/10/25.
 */
@Service
public class AttachmentService {

    private AttachmentDao attachmentDao = new AttachmentDao();

    /**
     * 新建附件信息
     *
     * @param attachmentEntry
     */
    public ObjectId addAttachmentEntry(AttachmentEntry attachmentEntry) {
        return attachmentDao.addAttachment(attachmentEntry);
    }
}
