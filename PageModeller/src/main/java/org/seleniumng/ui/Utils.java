package org.seleniumng.ui;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

class Utils {
	static String getScriptResource(Class<?> clazz, String resouceName) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        InputStream inputStream = clazz.getResourceAsStream(resouceName);
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            // StandardCharsets.UTF_8.name() > JDK 7
            return result.toString("UTF-8");
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

}
