package com.fulaan.service;

import com.fulaan.dao.AttachmentDao;
import com.fulaan.entry.AttachmentEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2016/10/25.
 */
@Service
public class AttachmentService {
  @Autowired
  private AttachmentDao attachmentDao;

  /**
   * 新建附件信息
   * @param attachmentEntry
   */
  public ObjectId addAttachmentEntry(AttachmentEntry attachmentEntry){
    return attachmentDao.addAttachment(attachmentEntry);
  }
}
