package com.kalix.fabric8.jwt.biz;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    public static String loadFile(String file) {
        try {
            InputStream stream = FileUtil.class.getClassLoader().getResourceAsStream(file);
            return IOUtils.toString(stream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
