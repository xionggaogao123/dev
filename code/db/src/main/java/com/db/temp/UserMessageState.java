package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class UserMessageState {


    public static void main(String[] args) throws IOException {

        File dir = new File("D:\\message");

        Map<String, Integer> map = new HashMap<String, Integer>();

        for (File f : dir.listFiles()) {
            List<String> list = FileUtils.readLines(f, "utf-8");

            for (String str : list) {
                if (str.indexOf("/mall/users/messages.do?mobile=") > 0) {
                    try {
                        int beg = str.indexOf("/mall/users/messages.do?mobile=");
                        String mob = str.substring(beg, beg + 11);
                    } catch (Exception ex) {

                    }
                }
            }
        }
    }

}
