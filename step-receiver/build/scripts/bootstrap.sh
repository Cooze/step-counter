#!/bin/bash
##################################
#@author cooze
#@date 2017/6/3
#@version 1.0.0
##################################

#main class
MAIN="org.cooze.stepcounter.receiver.Bootstrap"

#JAVA_OPTS
JAVA_OPTS=" -Xms1024m -Xmx2048m -Xss182k -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=384m -XX:NewSize=1536m -XX:MaxNewSize=1536m -XX:SurvivorRatio=8 -Duser.timezone=Asia/Shanghai -Dclient.encoding.override=UTF-8 -Dfile.encoding=UTF-8 -server -XX:+UseParNewGC -XX:ParallelGCThreads=4 -XX:MaxTenuringThreshold=9 -XX:+UseConcMarkSweepGC -XX:+DisableExplicitGC -XX:+UseCMSInitiatingOccupancyOnly -XX:+ScavengeBeforeFullGC -XX:+UseCMSCompactAtFullCollection -XX:+CMSParallelRemarkEnabled -XX:CMSFullGCsBeforeCompaction=9 -XX:CMSInitiatingOccupancyFraction=60 -XX:+CMSClassUnloadingEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSPermGenSweepingEnabled -XX:CMSInitiatingPermOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrent "

BIN_DIR=$(cd "$(dirname "$0")"; pwd)
#project ROOT PATH
PROJECT_HOME=${BIN_DIR%/*}

#dependency libs.
COMMON_LIBS=${PROJECT_HOME}/lib/*
#configure path
CONF_PATH=${PROJECT_HOME}/conf/*
#start log path
LOG_PATH=${PROJECT_HOME}/log

[ ! -d ${JAVA_HOME} ]||[ ! -d ${JRE} ] && {
    echo "Java home is not set."
    exit 0
}

function start(){

    [ ! -d ${LOG_PATH} ] &&{
        mkdir -p ${LOG_PATH}
    }

    ppid=`ps -ef | grep "java" |grep "${PROJECT_HOME}" |grep "${MAIN}" | awk '{print $2}'`

    if [ ! -z ${ppid} ]
    then
        echo "logger-server is running."
        exit 1
    fi

    nohup ${JAVA_HOME}/bin/java -server ${JAVA_OPTS} -cp ${COMMON_LIBS}:${CONF_PATH} ${MAIN} >>${LOG_PATH}/catalina.log 2>&1 /dev/null &

    echo "logger-server starting,please wait for second ... ..."

    sleep 1
    ppid=`ps -ef | grep "java" |grep "${PROJECT_HOME}" | grep "${MAIN}" | awk '{print $2}'`

    if [ -z ${ppid} ]
    then
        echo "logger-server start failure."
        exit 1
    fi
    echo "logger-server start success."
}

function stop(){

    ppid=`ps -ef | grep "java" |grep "${PROJECT_HOME}" |grep "${MAIN}" | awk '{print $2}'`

    if [ ! -z ${ppid} ]
    then
        kill -15 ${ppid}
        echo "logger-server stop."
    else
        echo "logger-server is not running."
    fi

}

function status(){

    ppid=`ps -ef | grep "java" | grep "${PROJECT_HOME}" |grep "${MAIN}" | awk '{print $2}'`

    if [ ! -z ${ppid} ]
    then
        echo "logger-server is running."
    else
        echo "logger-server stop."
    fi
}

case ${1} in

   start)
        start
    ;;

    stop)
        stop
    ;;

    status)
        status
    ;;

    *)
         echo -e "usage:\n\t${0} [start|stop|status]\n"
         exit 1
    ;;
esac