package dk.eazyit.eazyregnskab.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.MessageDigest;

/**
 * @author Ashish Shukla *
 */
public class PasswordEncoder {
    /**
     * Logger for this class
     */
    private static final Logger log = LoggerFactory.getLogger(PasswordEncoder.class);
    private static PasswordEncoder instance;
    /**
     * Count for the number of time to hash, * more you hash more difficult it would be for the attacker
     */
    private final static int ITERATION_COUNT = 5;

    /**
     * Empty Constructor
     */
    private PasswordEncoder() {
    }

    /**
     * @return * @author Ashish Shukla
     */
    public static synchronized PasswordEncoder getInstance() {
        log.debug("getInstance() - start");
        if (instance == null) {
            PasswordEncoder returnPasswordEncoder = new PasswordEncoder();
            log.debug("New instance created");
            log.debug("getInstance() - end");
            return returnPasswordEncoder;
        } else {
            log.debug("getInstance() - end");
            return instance;
        }
    }

    /**
     * * @param password * @param saltKey * @return * @throws NoSuchAlgorithmException * @throws IOException * @author Ashish Shukla
     */
    public synchronized String encode(String password, String saltKey) {

        String encodedPassword = null;
        try {
            log.debug("encode(String, String) - start");
            encodedPassword = null;
            byte[] salt = base64ToByte(saltKey);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt);
            byte[] btPass = digest.digest(password.getBytes("UTF-8"));
            for (int i = 0; i < ITERATION_COUNT; i++) {
                digest.reset();
                btPass = digest.digest(btPass);
            }
            encodedPassword = byteToBase64(btPass);
            log.debug("encode(String, String) - end");
        } catch (Exception e) {
            log.error("Password encoding failed due to " + e.getStackTrace().toString());
        } finally {
            return encodedPassword;
        }

    }

    /**
     * @param str * @return byte[] * @throws IOException
     */
    private byte[] base64ToByte(String str) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("base64ToByte(String) - start");
        }
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] returnbyteArray = decoder.decodeBuffer(str);
        log.debug("base64ToByte(String) - end");
        return returnbyteArray;
    }

    /**
     * @param bt * @return String * @throws IOException
     */
    private String byteToBase64(byte[] bt) {
        log.debug("byteToBase64(byte[]) - start");
        BASE64Encoder endecoder = new BASE64Encoder();
        String returnString = endecoder.encode(bt);
        log.debug("byteToBase64(byte[]) - end");
        return returnString;
    }
}
