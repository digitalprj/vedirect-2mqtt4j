package info.digitalproject.vedirect2mqtt4j.vedirect.data;

import java.io.*;

public class DataHelper {
    public static byte[] read(String filename) throws IOException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        File file = new File(classloader.getResource(filename).getFile());
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data, 0, data.length);
        fis.close();

        return data;
    }
}
