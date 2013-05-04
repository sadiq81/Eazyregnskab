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
        try {
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path), "UTF-8"));
            for (int c = br.read(); c != -1; c = br.read()) sb.append((char) c);
        } catch (UnsupportedEncodingException e) {
            //TODO send error message all the way up
        } catch (IOException e) {
            //TODO send error message all the way up
        }
        return sb.toString();
    }
}
