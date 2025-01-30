FROM openjdk:17-jdk-slim

# Устанавливаем dos2unix
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

# Копируем скрипт в контейнер
COPY create-topics.sh /usr/local/bin/create-topics.sh

# Исправляем переводы строк и делаем скрипт исполняемым
RUN dos2unix /usr/local/bin/create-topics.sh
RUN chmod +x /usr/local/bin/create-topics.sh

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar-файл
COPY target/location-tracking-1.0-SNAPSHOT.jar app.jar

# Пробрасываем порт (если он нужен приложению)
EXPOSE 9092

# Запуск приложения
CMD ["java", "-jar", "app.jar"]
