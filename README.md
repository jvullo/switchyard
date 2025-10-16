# Switchyard

An order-processing service built as practise to flesh out/practise some bits. Ideally also a place to trial out and practise Java changes in.

## Contents

- [Learning](#learning)
- [Logic Flows](#logic-flows)
- [Order Placement](#order-placement)
- [Setup](#setup)
  - [Vscode Setup](#vscode-setup)
- [References / Documentation](#references--documentation)

## Learning

| Topic         | What                                         | Why?                                                                                                                                                    | Whom? | Reference    |
| ------------- | -------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- | ----- | ------------ |
| Java          | Java 21 - Virtual Threads & Threads          |                                                                                                                                                         | WIP   |              |
| Java          | Structured Scope                             |                                                                                                                                                         | WIP   | [Reference]() |
| Sping         | Spring Boot Testing                          | Ensuring/reminding the right types of tags for differerent tests, like JPA for only JPA tests - need to add something in this to connect to a database. | WIP   |              |
| Spring        | Spring Depdendency Injection & Configuration |                                                                                                                                                         |       |              |
| Spring        | JPA                                          |                                                                                                                                                         | WIP   |              |
| IDE           | VSCode as Main                               | Previously a core user of Intellij, can I get vscode working as good as Intellij?                                                                       | WIP   |              |
| Architectural | Event Sourcing                               | Recording Events as series of actions                                                                                                                  |       |              |
| Languages     | Kotlin Inclusion?                            | Can I have a kotlin class included in my code?                                                                                                          |       |              |



## Logic Flows

### Order Placed

When an order is placed:

1. Validate it
2. Publishes an event in Kakfa
3. Call Downstream payment Service
4. Mark Ready for Shipping

Setup

### Vscode Setup

Some bits to setup vscode.

```Shell
code --install-extension vscjava.vscode-java-pack
code --install-extension vmware.vscode-boot-dev-pack
```

## References / Documentation
