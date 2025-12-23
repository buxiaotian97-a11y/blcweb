FROM eclipse-temurin:17-jdk

WORKDIR /app

# unzip と base64 を使う（Debian/Ubuntu系）
RUN apt-get update && apt-get install -y unzip && rm -rf /var/lib/apt/lists/*

# ソースをコピー
COPY . .

# ビルド（テストは飛ばす）
RUN ./mvnw -DskipTests package

# 起動：OLI_WALLET(base64) → zip復元 → unzip → Spring起動
CMD echo "$OLI_WALLET" | base64 -d > /tmp/wallet.zip \
 && mkdir -p /app/wallet \
 && unzip -o /tmp/wallet.zip -d /app/wallet \
 && java -jar target/*.jar
