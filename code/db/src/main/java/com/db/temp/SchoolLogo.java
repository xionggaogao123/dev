package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.sys.utils.QiniuFileUtils;

public class SchoolLogo {

    public static void main(String[] args) throws IOException {

        List<String> logos = FileUtils.readLines(new File("D:\\schoollogos\\school_logo.txt"), "utf-8");

        for (String logo : logos) {
            if (StringUtils.isNotBlank(logo)) {

                String[] infos = logo.split(",");

                if (null != infos && infos.length == 2) {
                    try {
                        String logoPath = "/schoollogos" + infos[1];
                        InputStream str = QiniuFileUtils.downFileByUrl("http://www.k6kt.com" + infos[1]);
                        FileUtils.copyInputStreamToFile(str, new File(logoPath));
                    } catch (Exception ex) {
                        System.out.println("出错了" + logo);
                    }
                } else {
                    System.out.println(logo);
                }

            }

        }


    }
}
