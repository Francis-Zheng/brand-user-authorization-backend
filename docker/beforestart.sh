#!/usr/bin/env bash

# 项目根目录
cd ../

# 构建jar文件
echo "构建后台程序jar文件"
mvn package spring-boot:repackage
cp target/brand-user-authorization-backend-0.0.1-SNAPSHOT.jar docker/backend/
