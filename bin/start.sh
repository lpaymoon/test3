. ./env.sh

if [ -f ${SERVER_NAME}.pid ]; then
PID=`cat ${SERVER_NAME}.pid`
ps -fp $PID >/dev/null
if [ $? -eq 0 ]; then
echo "�����Ѿ������У�����Ҫ������"
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
echo "���������ɹ�"
exit
fi
fi
