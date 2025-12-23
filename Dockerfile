FROM eclipse-temurin:17-jdk

WORKDIR /app

RUN apt-get update && apt-get install -y unzip && rm -rf /var/lib/apt/lists/*

COPY . .

RUN ./mvnw -DskipTests package

CMD echo "$OLI_WALLET" | base64 -d > /tmp/wallet.zip \
 && mkdir -p /app/wallet \
 && unzip -o /tmp/wallet.zip -d /app/wallet \
 && echo "=== WALLET CONTENTS ===" \
 && ls -la /app/wallet \
 && echo "=== tnsnames.ora location ===" \
 && find /app/wallet -maxdepth 3 -name tnsnames.ora -print \
 && java -jar target/*.jar
