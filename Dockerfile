FROM eclipse-temurin:17.0.7_7-jdk

ENV JDK_HOME=$JAVA_HOME \
    JRE_HOME=/opt/java/jre \
    APP_HOME=/opt/app \
    DATA_DIR=/opt/data

RUN apt-get update && \
    apt-get install -y --no-install-recommends maven gradle && \
    rm -rf /var/lib/apt/lists/*

COPY LICENSE.txt pom.xml $APP_HOME/
COPY src $APP_HOME/src
RUN cd $APP_HOME && \
    mvn -B -q package && \
    mv target/jamabuild.jar . && \
    rm -rf target

VOLUME $DATA_DIR
WORKDIR $DATA_DIR
ENTRYPOINT ["java", "-cp", "/opt/app/jamabuild.jar", "com.github.sulir.jamabuild.Build"]
