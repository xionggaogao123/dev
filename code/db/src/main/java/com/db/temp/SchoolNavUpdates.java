package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.school.SchoolDao;

public class SchoolNavUpdates {

    public static void main(String[] args) throws IOException {


        SchoolDao dao = new SchoolDao();
        List<String> list = FileUtils.readLines(new File("/home/school_navs.txt"), "utf-8");

        for (String s : list) {
            System.out.println(s);
            String[] arr = s.split(",");
            try {
                dao.update(new ObjectId(arr[0]), "nv", Integer.valueOf(arr[1]));
            } catch (Exception ex) {

            }
        }
    }
}
