#Delete old host file when starting new ec2 instance
cd .ssh
rm known_hosts

#Login command to instance
ssh -i IdeaProjects/eazyregnskab/awsupload/eazyit.pem ubuntu@eazyregnskab.dk

#install Oracle JDK http://www.blogs.digitalworlds.net/softwarenotes/?p=41
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update && sudo apt-get install oracle-jdk7-installer
java -version
javac -version

sudo nano /etc/environment
#Append to the end of the file:
JAVA_HOME=/usr/lib/jvm/java-7-oracle

#install tomcat
sudo apt-get install tomcat7

#change port to 80
sudo /etc/init.d/tomcat7 stop
sudo nano /etc/default/tomcat7
#change end of file to
AUTHBIND=yes

sudo nano server.xml
#set port to 80

#allow ubuntu user to upload to tomcat owned folders
sudo usermod -a -G tomcat7 ubuntu

#change context for webapp
sudo nano /var/lib/tomcat7/conf/server.xml
#add between <Host>...</Host>
<Context docBase="eazyregnskab.war" path="/"/>

sudo rm -rf /var/lib/tomcat7/webapps/ROOT/

#Upload webapp wia Deploy class

#start tomcat
sudo /etc/init.d/tomcat7 start


#install mariaDB (Not necessary)
#sudo apt-get install python-software-properties
#sudo apt-key adv --recv-keys --keyserver keyserver.ubuntu.com 0xcbcb082a1bb943db
#sudo add-apt-repository 'deb http://mirror2.hs-esslingen.de/mariadb/repo/5.5/ubuntu precise main'
#sudo apt-get update
#sudo apt-get install mariadb-server

