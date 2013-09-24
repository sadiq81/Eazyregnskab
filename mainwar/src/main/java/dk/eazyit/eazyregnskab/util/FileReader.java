package dk.eazyit.eazyregnskab.util;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
}
