jar_path= /root/.jenkins/workspace/AIclass_auto/target
jar_name=ss-0.0.1-SNAPSHOT.jar
chmod u+x ss-0.0.1-SNAPSHOT.jar
cd ${jar_path}
echo ${jar_path}
echo nohup java -jar ${jar_name} &
BUILD_ID=dontKillMe nohup java -jar ${jar_name} &