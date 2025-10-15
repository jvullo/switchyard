# Switchyard

Practising out some kafka, and some newer features of Java that I haven't played around with yet.

Current aim is to build an order-processing service built around Kafka and event sourcing.

Something like Placing an order validates it, publishes an event, and everything downstream payment, shipping, cancellation happens by folding a stream of events into the order's current state.


## Setup

```Shell
code --install-extension vscjava.vscode-java-pack
code --install-extension vmware.vscode-boot-dev-pack
```
