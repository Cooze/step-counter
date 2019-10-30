package org.cooze.stepcounter.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-13
 **/
public final class GzipUtil {

    private static final int BUFFER_LENGTH = 1024;
    private static final int START_OFFSET = 0;
    private static final int DATA_EMPTY = -1;

    public static byte[] gzip(byte[] data) {
        byte[] buffer = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.finish();
            gzip.close();
            buffer = bos.toByteArray();
            bos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return buffer;
    }

    public static byte[] unGzip(byte[] data) {
        byte[] buffer = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            GZIPInputStream gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[BUFFER_LENGTH];
            int num;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, START_OFFSET, buf.length)) != DATA_EMPTY) {
                baos.write(buf, START_OFFSET, num);
            }
            buffer = baos.toByteArray();
            baos.flush();
            baos.close();
            gzip.close();
            bis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return buffer;
    }
}
