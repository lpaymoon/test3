. ./env.sh

#netstat -anp |grep 8088
echo "---check---1"
ps -ef|grep $APP
echo "---kill----2"
if [ -f ${SERVER_NAME}.pid ]
then
PID=`cat ${SERVER_NAME}.pid`
ps -fp $PID >/dev/null

if [ $? -eq 0 ]; then	
kill $PID
rm ${SERVER_NAME}.pid
else
rm ${SERVER_NAME}.pid
fi
echo "服务已经停止"
else

echo "服务没有启动"
exit
fi

echo "---check---3"
ps -ef |grep $APP |grep -v grep|grep -v tail

