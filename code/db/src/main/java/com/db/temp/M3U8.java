package com.db.temp;

import com.db.video.VideoDao;
import com.pojo.video.VideoEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class M3U8 {

    public static void main(String[] args) throws IOException {
        File file = new File("/home/micro0324.txt");
        file.createNewFile();
        VideoDao dao = new VideoDao();

        int skip = 0;
        int limit = 200;

        while (true) {
            System.out.println("skip=" + skip);
            List<VideoEntry> list = dao.getVideoEntrys(skip, limit);

            if (null == list || list.size() == 0) {
                break;
            }

            for (VideoEntry e : list) {
                if (StringUtils.isNotBlank(e.getBucketkey())) {
                    String ss = "http://video.k6kt.com/m3u8/" + e.getBucketkey() + ".m3u8";
                    FileUtils.write(file, ss, true);
                    FileUtils.write(file, "\r\n", true);

                }
            }


            skip = skip + 200;
        }


    }
}
