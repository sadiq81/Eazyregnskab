package dk.eazyit.eazyregnskab.util;

import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @author
 */
@Service
public class FileReader {

    public String readFromTxtFile(String path) {

        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path), "UTF-8"));
            for (int c = br.read(); c != -1; c = br.read()) sb.append((char) c);
            br.close();
        } catch (UnsupportedEncodingException e) {
            //TODO send error message all the way up
        } catch (IOException e) {
            //TODO send error message all the way up
        } finally {

        }
        return sb.toString();
    }

    public String getExtensionFromFile(File file) {
        int divider = file.getName().lastIndexOf(".");
        String extension = file.getName().substring(divider + 1);
        return extension;
    }
}
