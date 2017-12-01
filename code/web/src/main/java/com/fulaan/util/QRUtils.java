package com.fulaan.util;

import com.sys.utils.QiniuFileUtils;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by moslpc on 2016/11/17.
 */
public class QRUtils {

//    private static final String prev_url="http://www.fulaan.com";
//   private static final String prev_url="http://appapi.jiaxiaomei.com";
     private static final String prev_url="http://192.168.1.218";
   // private static final String prev_url="http://121.40.229.225:8080";
  // private static final String prev_url="http://192.168.1.35:8087";


    public static String getBindLoginQrUrl(ObjectId tokenId){
        String qrCode = prev_url+"/jxmapi/user/tokenQr/" + tokenId.toString();
        try {
            return getQrUrl(qrCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 生成社区二维码路径
     *
     * @param communityId
     * @return
     */
    public static String getCommunityQrUrl(ObjectId communityId) {
        String qrCode = prev_url+"/qr/community/" + communityId.toString();
        try {
            return getQrUrl(qrCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成个人二维码路径
     *
     * @param userId
     * @return
     */
    public static String getPersonQrUrl(ObjectId userId) {
        String qrCode = prev_url+"/qr/person/" + userId.toString();
        try {
            return getQrUrl(qrCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPersonBindQrUrl(ObjectId userId) {
        String qrCode = prev_url+"/qr/personBind/" + userId.toString();
        try {
            return getQrUrl(qrCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成社区群组路径
     *
     * @param groupId
     * @return
     */
    public static String getGroupQrUrl(ObjectId groupId) {
        String qrCode = prev_url+"/qr/group/" + groupId.toString();
        try {
            return getQrUrl(qrCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取二维码路径
     *
     * @param qrCode
     * @return
     * @throws Exception
     */
    private static String getQrUrl(String qrCode) throws Exception {
        ObjectId id = new ObjectId();
        File file = File.createTempFile(id.toString(), ".jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        QRCodeUtil.encode(qrCode, fileOutputStream);
        IOUtils.closeQuietly(fileOutputStream);
        QiniuFileUtils.uploadFile(id.toString(), new FileInputStream(file), QiniuFileUtils.TYPE_IMAGE);
        file.delete();
        return QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, id.toString());
    }


    public static String getSmallLessonUserCodeQrUrl(ObjectId lessonId) {
        String qrCode = prev_url+"/jxmapi/smallLesson/addStuEntry/" + lessonId.toString();
        try {
            return getQrUrl(qrCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
