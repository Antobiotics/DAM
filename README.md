DAM
===

Experimenting with real-time data processing.
The current log pipeline looks like that:

```
LogProducer -> Kafka -> Main -> Spark-streaming -> s3
```

1. Setup:
---------

```
> make compose-services
```
