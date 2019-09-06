#!/usr/bin/env bash

echo "正在导入用户和角色数据..."

mongoimport --host mongo:27017 --db brand_user_authorization --collection role --file /data/role.json
mongoimport --host mongo:27017 --db brand_user_authorization --collection user --file /data/user.json

echo "正在启动品牌管理系统后端程序..."
java -jar /tmp/brand-user-authorization-backend-0.0.1-SNAPSHOT.jar