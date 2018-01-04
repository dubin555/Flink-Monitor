# Flink-Monitor
**A basic code to monitor Flink job exception base on Akka**

## For What
Apache Flink DashBoard is awesome, but weak at alerting.
The code of this repo focus on how to monitor Flink Job and alert them.

## Simple Architecture
![Simple Architecture for Flink-Monitor(Alert Module under doing)](https://github.com/dubin555/Flink-Monitor/blob/master/png/simple-arch.png)

## Design

### Akka Actor
| Actor name        | restful url           | hierarchy  |
| ------------- |:-------------:| -----:|
| ConfigActor      |/config | akka.ConfigActor |
| OverviewActor      | /overview      |   akka.OverviewActor |
| JobsActor | /jobs      |    akka.JobsActor |
| JobExceptionActor | /jobs/jobID/exceptions      |    akka.JobExceptionActor |