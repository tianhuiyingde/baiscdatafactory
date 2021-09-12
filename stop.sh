jar_name=ss-0.0.1-SNAPSHOT.jar
echo "Stopping" ${jar_name}
pid=`ps -ef | grep ${jar_name} | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ]
then
   echo "kill -9 的pid:" $pid
   echo "test kill123" 
   kill -9 $pid
   echo "test kill" 
fi