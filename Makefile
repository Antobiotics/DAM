run-consumer:
	sbt "run-main Main"

run-producer:
	sbt "run-main LogProducer 100 test"

compose-services:
	docker-compose -f services/docker-compose.yml up -d

clean-checkpoints:
	rm -rf ./tmp/
