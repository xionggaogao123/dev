package com.fulaan.picturetext.runnable;

/**
 * Created by James on 2017/11/24.
 *
 * //异步线程处理
 */
public class PictureRunNable{


    public static void send(final String id) {
        new Thread(){
            public void run() {
                System.out.println("新的线程在执行...");
                //System.out.println(prtNo);
            }
        }.start();
    }

}
