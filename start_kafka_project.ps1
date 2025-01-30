# Please install kafka and write right path
$kafkaPath = "C:\kafka"  # path to kafka
cd $kafkaPath

# Starting ZooKeeper on new window
Write-Host "Starting ZooKeeper..."
Start-Process -FilePath "powershell.exe" -ArgumentList "-NoExit", "-Command", "cd $kafkaPath; .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties"

# Waiting 5 seconds for running Zookeper
Start-Sleep -Seconds 5

# Starting Kafka Broker on new window
Write-Host "Starting Kafka broker..."
Start-Process -FilePath "powershell.exe" -ArgumentList "-NoExit", "-Command", "cd $kafkaPath; .\bin\windows\kafka-server-start.bat .\config\server.properties"


# Waiting 5 seconds for running Kafka Broker
Start-Sleep -Seconds 10

# Creating Topics
Write-Host "Creating Kafka topics..."
Start-Process -FilePath "powershell.exe" -ArgumentList "-NoExit", "-Command", "cd $kafkaPath; .\bin\windows\kafka-topics.bat --create --topic location_updates --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1"
Start-Process -FilePath "powershell.exe" -ArgumentList "-NoExit", "-Command", "cd $kafkaPath; .\bin\windows\kafka-topics.bat --create --topic distance_report --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1"

# Starting Produser on new window
Write-Host "Starting Producer..."
$projectPath = "C:\Users\mehra\IdeaProjects\location-tracking"  # path to project
Start-Process -FilePath "powershell.exe" -ArgumentList "-NoExit", "-Command", "cd $projectPath; java -cp target\location-tracking-1.0-SNAPSHOT.jar producer.LocationProducer"

# Starting Consumer on new window
Write-Host "Starting Consumer..."
Start-Process -FilePath "powershell.exe" -ArgumentList "-NoExit", "-Command", "cd $projectPath; java -cp target\location-tracking-1.0-SNAPSHOT.jar consumer.LocationConsumer"

# Starting DistanceReport on new window
Write-Host "Starting DistanceReport..."
Start-Process -FilePath "powershell.exe" -ArgumentList "-NoExit", "-Command", "cd $projectPath; java -cp target\location-tracking-1.0-SNAPSHOT.jar report.DistanceReport"

Write-Host "All processes started. Each running in a separate PowerShell window. Monitor the windows for activity."
