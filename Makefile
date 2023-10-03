setup:
	cd frontend && npm install
	gradle wrapper --gradle-version 8.3
	gradle build

frontend:
	cd frontend && npm run dev

clean:
	./gradlew clean

build:
	./gradlew clean build

reload-classes:
	./gradlew -t classes

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

start:
	./gradlew bootRun --args="--spring.profiles.active=dev"

install:
	./gradlew installDist

# start-dist:
# 	./build/install/app/bin/app

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

# report:
# 	./gradlew jacocoTestReport

update-js-deps:
	cd frontend && npx ncu -u

check-java-deps:
	./gradlew dependencyUpdates -Drevision=release

generate-migrations:
	gradle diffChangeLog

db-migrate:
	./gradlew update

.PHONY: build frontend