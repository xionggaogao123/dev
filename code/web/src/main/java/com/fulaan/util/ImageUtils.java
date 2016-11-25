package com.fulaan.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mosl on 2016/11/2.
 */
public class ImageUtils {

    /**
     * 生成组合头像
     *
     * @throws IOException
     */
    public static BufferedImage getCombinationOfhead(List<String> paths)
            throws IOException {

        int count = paths.size();

        List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();
        int width = 690; // 这是画板的宽高
        int height = 690; // 这是画板的高度

        BufferedImage outImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics g = outImage.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(new Color(231, 231, 231));
        g2d.clearRect(0, 0, width, height);


        int padding = 10;

        if (count == 1) {
            int scale = height / 2;
            for (String f : paths) {
                bufferedImages.add(resize2(f, scale, scale));
            }
            g2d.drawImage(bufferedImages.get(0), (height - scale) / 2, (height - scale) / 2, null);
        }

        if (count == 2) {
            int scale = (height - 3 * padding) / 2;
            for (String f : paths) {
                bufferedImages.add(resize2(f, scale, scale));
            }
            g2d.drawImage(bufferedImages.get(0), padding, scale / 2, null);
            g2d.drawImage(bufferedImages.get(1), scale + 2 * padding, scale / 2, null);
        }

        if (count == 3) {
            int scale = (height - 3 * padding) / 2;
            for (String f : paths) {
                bufferedImages.add(resize2(f, scale, scale));
            }
            g2d.drawImage(bufferedImages.get(0), (width - scale) / 2, padding, null);
            g2d.drawImage(bufferedImages.get(1), padding, scale + 2 * padding, null);
            g2d.drawImage(bufferedImages.get(2), scale + 2 * padding, scale + 2 * padding, null);

        }
        if (count == 4) {
            int scale = (height - 3 * padding) / 2;
            for (String f : paths) {
                bufferedImages.add(resize2(f, scale, scale));
            }
            g2d.drawImage(bufferedImages.get(0), padding, padding, null);
            g2d.drawImage(bufferedImages.get(1), scale + 2 * padding, padding, null);

            g2d.drawImage(bufferedImages.get(2), padding, scale + 2 * padding, null);
            g2d.drawImage(bufferedImages.get(3), scale + 2 * padding, scale + 2 * padding, null);
        }


        if (count == 5) {
            int scale = (height - 4 * padding) / 3;
            int hei = (height - 2 * scale - padding) / 2;
            int wid = (width - 2 * scale - padding) / 2;
            for (String f : paths) {
                bufferedImages.add(resize2(f, scale, scale));
            }
            g2d.drawImage(bufferedImages.get(0), wid, hei, null);
            g2d.drawImage(bufferedImages.get(1), scale + wid + padding, hei, null);

            g2d.drawImage(bufferedImages.get(2), padding, scale + padding + hei, null);
            g2d.drawImage(bufferedImages.get(3), scale + 2 * padding, scale + padding + hei, null);

            g2d.drawImage(bufferedImages.get(4), 2 * scale + 3 * padding, scale + padding + hei, null);
        }


        if (count == 6) {

            int scale = (height - 4 * padding) / 3;
            int hei = (height - 2 * scale - padding) / 2;
            for (String f : paths) {
                bufferedImages.add(resize2(f, scale, scale));
            }
            g2d.drawImage(bufferedImages.get(0), padding, hei, null);
            g2d.drawImage(bufferedImages.get(1), scale + 2 * padding, hei, null);
            g2d.drawImage(bufferedImages.get(2), 2 * scale + 3 * padding, hei, null);

            g2d.drawImage(bufferedImages.get(3), padding, scale + padding + hei, null);
            g2d.drawImage(bufferedImages.get(4), scale + 2 * padding, scale + padding + hei, null);
            g2d.drawImage(bufferedImages.get(5), 2 * scale + 3 * padding, scale + padding + hei, null);

        }

        if (count == 7) {
            int scale = (height - 4 * padding) / 3;
            for (String f : paths) {
                bufferedImages.add(resize2(f, scale, scale));
            }
            g2d.drawImage(bufferedImages.get(0), (width - scale) / 2, padding, null);

            g2d.drawImage(bufferedImages.get(1), padding, scale + 2 * padding, null);
            g2d.drawImage(bufferedImages.get(2), scale + 2 * padding, scale + 2 * padding, null);
            g2d.drawImage(bufferedImages.get(3), 2 * scale + 3 * padding, scale + 2 * padding, null);

            g2d.drawImage(bufferedImages.get(4), padding, 2 * scale + 3 * padding, null);
            g2d.drawImage(bufferedImages.get(5), scale + 2 * padding, 2 * scale + 3 * padding, null);
            g2d.drawImage(bufferedImages.get(6), 2 * scale + 3 * padding, 2 * scale + 3 * padding, null);
        }

        if (count == 8) {

            int scale = (height - 4 * padding) / 3;
            int win = (width - 2 * scale - padding) / 2;
            for (String f : paths) {
                bufferedImages.add(resize2(f, scale, scale));
            }
            g2d.drawImage(bufferedImages.get(0), win, padding, null);
            g2d.drawImage(bufferedImages.get(1), scale + padding + win, padding, null);

            g2d.drawImage(bufferedImages.get(2), padding, scale + 2 * padding, null);
            g2d.drawImage(bufferedImages.get(3), scale + 2 * padding, scale + 2 * padding, null);
            g2d.drawImage(bufferedImages.get(4), 2 * scale + 3 * padding, scale + 2 * padding, null);

            g2d.drawImage(bufferedImages.get(5), padding, scale * 2 + 3 * padding, null);
            g2d.drawImage(bufferedImages.get(6), scale + 2 * padding, scale * 2 + 3 * padding, null);
            g2d.drawImage(bufferedImages.get(7), scale * 2 + 3 * padding, scale * 2 + 3 * padding, null);
        }
        if (count == 9) {

            int scale = (height - 4 * padding) / 3;
            for (String f : paths) {
                bufferedImages.add(resize2(f, scale, scale));
            }
            g2d.drawImage(bufferedImages.get(0), padding, padding, null);
            g2d.drawImage(bufferedImages.get(1), scale + 2 * padding, padding, null);
            g2d.drawImage(bufferedImages.get(2), scale * 2 + 3 * padding, padding, null);

            g2d.drawImage(bufferedImages.get(3), padding, scale + 2 * padding, null);
            g2d.drawImage(bufferedImages.get(4), scale + 2 * padding, scale + 2 * padding, null);
            g2d.drawImage(bufferedImages.get(5), scale * 2 + 3 * padding, scale + 2 * padding, null);


            g2d.drawImage(bufferedImages.get(6), padding, scale * 2 + 3 * padding, null);
            g2d.drawImage(bufferedImages.get(7), scale + 2 * padding, scale * 2 + 3 * padding, null);
            g2d.drawImage(bufferedImages.get(8), scale * 2 + 3 * padding, scale * 2 + 3 * padding, null);

        }
        return outImage;
    }

    /**
     * 图片缩放
     *
     * @param height 高度
     * @param width  宽度
     */
    public static BufferedImage resize2(String f, int height, int width) throws IOException {
        double ratio = 0; // 缩放比例
        BufferedImage bi = ImageIO.read(new URL(f).openStream());
        Image itemp = bi.getScaledInstance(width, height,
                Image.SCALE_SMOOTH);
        if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
            if (bi.getHeight() > bi.getWidth()) {
                ratio = (new Integer(height)).doubleValue()
                        / bi.getHeight();
            } else {
                ratio = (new Integer(width)).doubleValue() / bi.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(
                    AffineTransform.getScaleInstance(ratio, ratio), null);
            itemp = op.filter(bi, null);
        }
        return toBufferedImage(itemp);
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

}