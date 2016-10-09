run-consumer:
	sbt "run-main Main"

run-producer:
	sbt "run-main LogProducer 100 test"
