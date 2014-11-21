. ./env.sh

if [ -f ${SERVER_NAME}.pid ]; then
PID=`cat ${SERVER_NAME}.pid`
ps -fp $PID >/dev/null
if [ $? -eq 0 ]; then
echo "服务已经在运行，不需要再启动"
exit
fi
fi

echo `date` \$\$
nohup java -DTAG=$SERVER_NAME -Dfile.encoding=GB18030 $APP &
echo $! >${SERVER_NAME}.pid

if [ -f ${SERVER_NAME}.pid ]; then
PID=`cat ${SERVER_NAME}.pid`
ps -fp $PID >/dev/null
if [ $? -eq 0 ]; then
echo "服务启动成功"
exit
fi
fi
