call mvn install:install-file -Dfile=org.infrastructure.jdbc-1.0.61-SNAPSHOT.jar -DgroupId=org.infrastructure -DartifactId=org.infrastructure.jdbc -Dversion=1.0.61-SNAPSHOT -Dpackaging=JAR

call mvn install:install-file -Dfile=org.infrastructure.utils-1.0.61-SNAPSHOT.jar -DgroupId=org.infrastructure -DartifactId=org.infrastructure.utils -Dversion=1.0.61-SNAPSHOT -Dpackaging=JAR

call mvn install:install-file -Dfile=ojdbc8.jar -DgroupId=org.infrastructure -DartifactId=ojdbc -Dversion=8 -Dpackaging=JAR

pause
