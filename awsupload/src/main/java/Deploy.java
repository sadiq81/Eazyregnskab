import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.PublicKeyAuthenticationClient;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKeyFile;

import java.io.File;

public class Deploy {
    public static void main(String[] arg) throws Exception {

        System.out.println("Uploading new version in: ");
        for (int i = 10; i > 0; i--) {
            if (i != 1) System.out.println(i + " seconds");
            if (i == 1) System.out.println(i + " second");
            Thread.sleep(1000);
        }

        String username = arg[0];
        String pathToKey = arg[1];
        String hostname = arg[2];

        SshClient ssh = new SshClient();
        ssh.connect(hostname, 23);

        PublicKeyAuthenticationClient pk = new PublicKeyAuthenticationClient();
        pk.setUsername(username);
        pk.setKey(SshPrivateKeyFile.parse(new File(pathToKey)).toPrivateKey(null));
        ssh.authenticate(pk);

        SessionChannelClient session = ssh.openSessionChannel();
        if (session.executeCommand("sudo pkill -9 -f jetty")) {
            System.out.println("Jetty server has been stopped");
        } else {
            throw new NullPointerException("Jetty server has NOT been stopped");
        }
        session.close();

        session = ssh.openSessionChannel();
        if (session.executeCommand("rm -rf /home/ubuntu/*")) {
            System.out.println("Old version of Jetty has been deleted");
        } else {
            throw new NullPointerException("Old version of Jetty has NOT been deleted");
        }
        session.close();

        SftpClient jar = ssh.openSftpClient();
        jar.put(System.getProperty("user.dir") + "/jetty/target/jetty-1.0-SNAPSHOT.jar", "/home/ubuntu/");
        jar.quit();

        SftpClient properties = ssh.openSftpClient();
        properties.put(System.getProperty("user.dir") + "/jetty/target/classes/jetty-realm.properties", "/home/ubuntu/");
        properties.quit();

        SftpClient war = ssh.openSftpClient();
        war.put(System.getProperty("user.dir") + "/mainwar/target/eazyregnskab.war", "/home/ubuntu/");
        war.quit();


        session = ssh.openSessionChannel();
        if (session.executeCommand("sudo java -jar /home/ubuntu/jetty-1.0-SNAPSHOT.jar &")) {
            System.out.println("Jetty server has been started");
        } else {
            throw new NullPointerException("Jetty server has NOT!!! been started");
        }
        session.close();

        ssh.disconnect();


    }

}