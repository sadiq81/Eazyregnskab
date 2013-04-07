import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.PublicKeyAuthenticationClient;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKeyFile;

import java.io.File;

public class Deploy {
    public static void main(String[] arg) throws Exception {

        String username = arg[0];
        String pathToKey = arg[1];
        String hostname = arg[2];
        String localFile = arg[3];
        String remoteFile = arg[4];

        SshClient ssh = new SshClient();
        ssh.connect(hostname);

        PublicKeyAuthenticationClient pk = new PublicKeyAuthenticationClient();
        pk.setUsername(username);
        pk.setKey(SshPrivateKeyFile.parse(new File(pathToKey)).toPrivateKey(null));
        ssh.authenticate(pk);

        SessionChannelClient session = ssh.openSessionChannel();
        if (session.executeCommand("sudo /etc/init.d/tomcat7 stop")) {
            System.out.println("Tomcat server has been stopped");
        } else {
            throw new NullPointerException("Tomcat server has NOT been stopped");
        }
        session.close();

        session = ssh.openSessionChannel();
        if (session.executeCommand("sudo rm -rf /var/lib/tomcat7/webapps/ROOT/")) {
            System.out.println("Old version of eazyregnskab has been deleted");
        } else {
            throw new NullPointerException("Old version of eazyregnskab has NOT been deleted");
        }
        session.close();

        SftpClient sftp = ssh.openSftpClient();
        sftp.put(System.getProperty("user.dir") + "/mainwar/target/eazyregnskab.war", "/var/lib/tomcat7/webapps/");
        sftp.quit();

        session = ssh.openSessionChannel();
        if (session.executeCommand("sudo /etc/init.d/tomcat7 start")) {
            System.out.println("Tomcat server has been started");
        } else {
            throw new NullPointerException("Tomcat server has NOT!!! been started");
        }
        session.close();

        ssh.disconnect();


    }

}