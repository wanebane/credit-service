#!/bin/bash

# Build dan run script untuk credit-simulator
echo "1. Membersihkan dan mem-build project dengan Maven..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
  echo "Build berhasil! Menjalankan aplikasi..."
  java -jar target/credit-simulator.jar --cli
else
  echo "Build gagal! Periksa error di atas."
  exit 1
fi