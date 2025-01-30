#!/bin/bash
echo "Waiting for Kafka to be ready..."
sleep 10

echo "Checking existing topics..."
existing_topics=$(kafka-topics.sh --list --bootstrap-server kafka:9092)

if echo "$existing_topics" | grep -q "location_updates"; then
    echo "Topic location_updates already exists"
else
    kafka-topics.sh --create --topic location_updates --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1
    echo "Created topic location_updates"
fi

if echo "$existing_topics" | grep -q "distance_report"; then
    echo "Topic distance_report already exists"
else
    kafka-topics.sh --create --topic distance_report --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1
    echo "Created topic distance_report"
fi
