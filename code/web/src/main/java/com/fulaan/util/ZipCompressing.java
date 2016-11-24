package com.fulaan.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by jerry on 2016/10/19.
 * 压缩文件
 */
public class ZipCompressing {

  private List<String> fileList = new ArrayList<String>();
  private String SOURCE_FOLDER;

  public void zip(final File inputFile, final File outputFile) {
    SOURCE_FOLDER = inputFile.getAbsolutePath();
    generateFileList(inputFile);
    zipIt(outputFile);
  }

  /**
   * Zip it
   *
   * @param zipFile output ZIP file location
   */
  public void zipIt(File zipFile) {

    byte[] buffer = new byte[1024];

    try {

      FileOutputStream fos = new FileOutputStream(zipFile);
      ZipOutputStream zos = new ZipOutputStream(fos);

      System.out.println("Output to Zip : " + zipFile);

      for (String file : this.fileList) {

        System.out.println("File Added : " + file);
        ZipEntry ze = new ZipEntry(file);
        zos.putNextEntry(ze);

        FileInputStream in =
                new FileInputStream(SOURCE_FOLDER + File.separator + file);

        int len;
        while ((len = in.read(buffer)) > 0) {
          zos.write(buffer, 0, len);
        }

        in.close();
      }

      zos.closeEntry();
      zos.close();

      System.out.println("Done");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Traverse a directory and get all files,
   * and add the file into fileList
   *
   * @param node file or directory
   */
  public void generateFileList(File node) {

    if (node.isFile()) {
      fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
    }

    if (node.isDirectory()) {
      String[] subNote = node.list();
      for (String filename : subNote) {
        generateFileList(new File(node, filename));
      }
    }

  }

  /**
   * Format the file path for zip
   *
   * @param file file path
   * @return Formatted file path
   */
  private String generateZipEntry(String file) {
    return file.substring(SOURCE_FOLDER.length() + 1, file.length());
  }

}
