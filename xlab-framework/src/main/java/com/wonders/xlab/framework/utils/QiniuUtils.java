package com.wonders.xlab.framework.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by wangqiang on 15/8/27.
 */
public final class QiniuUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(QiniuUtils.class);

    private static final String ACCESS_KEY;
    private static final String SECRET_KEY;
    private static final Auth AUTH;

    private static final UploadManager uploadManager = new UploadManager();

    static {
        Properties props = new Properties();
        InputStream stream = QiniuUtils.class.getResourceAsStream("/qiniu.properties");
        try {
            props.load(stream);
            ACCESS_KEY = props.getProperty("qiniu.access_key");
            SECRET_KEY = props.getProperty("qiniu.secret_key");
            AUTH = Auth.create(ACCESS_KEY, SECRET_KEY);
            stream.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static boolean upload(File file, String bucket, String key) {
        try {
            Response res = uploadManager.put(file, key, AUTH.uploadToken(bucket, key));
            return res.isOK();
        } catch (QiniuException e) {
            try {
                LOGGER.error(e.response.bodyString());
            } catch (QiniuException e1) {
                // ignore
            }
            throw new RuntimeException(e);
        }
    }

    public static boolean upload(byte[] data, String bucket, String key) {
        try {
            Response res = uploadManager.put(data, key, AUTH.uploadToken(bucket, key));
            return res.isOK();
        } catch (QiniuException e) {
            try {
                LOGGER.error(e.response.bodyString());
            } catch (QiniuException e1) {
                // ignore
            }
            throw new RuntimeException(e);
        }
    }

}
