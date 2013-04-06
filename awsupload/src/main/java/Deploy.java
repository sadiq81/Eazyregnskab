
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.*;

public class Deploy {
    public static void main(String[] arg) {

        try {
            String current = new java.io.File(".").getCanonicalPath();
            System.out.println("Current dir:" + current);
            JSch jsch = new JSch();
            jsch.addIdentity("awsupload/eazyit.pem");
            jsch.setKnownHosts("/users/tommysadiqhinrichsen/.ssh/known_hosts");
            String host = "eazyregnskab.dk";
            String user = "ubuntu";
            Session session = jsch.getSession(user, host, 22);
            session.connect();

            System.out.println("Stopping server");
            stopServer(session);
            System.out.println("Uploading war file");
             upLoadWarFile(session);
            System.out.println("Starting server");
            startServer(session);


        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static File createStopShellScript() {

        String filename = "stopServer.sh";
        File fstream = new File(filename);

        try {
            // Create file
            PrintStream out = new PrintStream(new FileOutputStream(fstream));
            out.println("sudo /etc/init.d/tomcat7 stop");
            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return fstream;
    }

    public static File createStartShellScript() {

        String filename = "startServer.sh";
        File fstream = new File(filename);

        try {
            // Create file
            PrintStream out = new PrintStream(new FileOutputStream(fstream));

            out.println("sudo /etc/init.d/tomcat7 start");
            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return fstream;

    }

    public static void stopServer(Session session) {

        try {
            File shellScript = createStopShellScript();
            //Convert the shell script to byte stream
            FileInputStream fin = new FileInputStream(shellScript);
            byte fileContent[] = new byte[(int) shellScript.length()];
            fin.read(fileContent);
            InputStream in = new ByteArrayInputStream(fileContent);
            //Set the shell script to the channel as input stream
            Channel channel = session.openChannel("shell");
            channel.setOutputStream(System.out);
            channel.setInputStream(in);
            channel.connect();
            System.out.println("Waiting for tomcat to stop");
            Thread.sleep(4000);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void startServer(Session session) {
        try {
            File shellScript = createStartShellScript();
            //Convert the shell script to byte stream
            FileInputStream fin = new FileInputStream(shellScript);
            byte fileContent[] = new byte[(int) shellScript.length()];
            fin.read(fileContent);
            InputStream in = new ByteArrayInputStream(fileContent);
            //Set the shell script to the channel as input stream
            Channel channel = session.openChannel("shell");
            channel.setOutputStream(System.out);
            channel.setInputStream(in);
            channel.connect();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void upLoadWarFile(Session session) {
        try {
            String SFTPWORKINGDIR = "/var/lib/tomcat7/webapps";
            String FILETOTRANSFER = "mainwar/target/eazyregnskab.war";

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.setOutputStream(System.out);
            channelSftp.connect();
            channelSftp.cd(SFTPWORKINGDIR);
            File f = new File(FILETOTRANSFER);
            channelSftp.put(new FileInputStream(f), f.getName(), ChannelSftp.OVERWRITE);
        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }

}