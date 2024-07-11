#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/IT-Pick-Backend
LOG_DIR=$REPOSITORY/logs
LOG_FILE=$LOG_DIR/deploy.log

# Ensure the log directory exists
mkdir -p $LOG_DIR

cd $REPOSITORY

APP_NAME=Backend
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]; then
  echo "$(date) - 종료할 애플리케이션이 없습니다." | tee -a $LOG_FILE
else
  echo "$(date) - kill -15 $CURRENT_PID" | tee -a $LOG_FILE
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "$(date) - Deploying $JAR_PATH" | tee -a $LOG_FILE
nohup java -jar --spring.profiles.active=local $JAR_PATH >> $LOG_FILE 2>&1 &

