package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileLists {

    public void listFile(String path, File file) throws IOException {

        File f = new File(path);

        List<File> files = new ArrayList<File>();

        for (File ff : f.listFiles()) {
            files.add(ff);
        }

        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File arg0, File arg1) {

                return arg0.getName().compareTo(arg1.getName());
            }
        });

        for (File subFile : files) {
            if (subFile.isDirectory()) {
                listFile(subFile.getAbsolutePath(), file);
            }
            FileUtils.write(file, subFile.getAbsolutePath(), true);
            FileUtils.write(file, "\r\n", true);
        }
    }

    public static void main(String[] args) throws IOException {

        final File file = new File("D:\\listFile.txt");
        file.createNewFile();
        FileLists fl = new FileLists();
        // fl.listFile(args[0], file);

        fl.listFile("d:\\八年级上", file);
    }
}
