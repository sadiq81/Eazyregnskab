#Delete old host file when starting new ec2 instance
cd .ssh
rm known_hosts

#Login command to instance
ssh -p 23 -i IdeaProjects/eazyregnskab/awsupload/eazyit.pem ubuntu@eazyregnskab.dk

#install Oracle JDK http://www.blogs.digitalworlds.net/softwarenotes/?p=41
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update && sudo apt-get install oracle-jdk7-installer
java -version
javac -version

sudo nano /etc/environment
#Append to the end of the file:
JAVA_HOME=/usr/lib/jvm/java-7-oracle

#Upload webapp wia Deploy class
