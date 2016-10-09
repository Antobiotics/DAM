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

As it won't probably work out of the box, please run `docker ps` to check it actually set up everything
