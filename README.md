# Switchyard

Practising out kafka and some newer features of JDK 25. 

Current aim is to build an order-processing service built around Kafka and event sourcing.

Placing an order validates it, publishes an event, and everything downstream payment, shipping, cancellation happens by folding a stream of events into
the order's current state.
