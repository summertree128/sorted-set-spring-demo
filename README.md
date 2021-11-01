sorted-set-spring-demo
======

Demo App for Redis Sorted Set operations by Spring Data Redis, especially for `ZPOPMIN`/`ZPOPMAX` commands.

# Introduction

Since `ZPOPMIN`, `BZPOPMIN`, `ZPOPMAX`, `BZPOPMAX`, `ZMSCORE` are not supported by Spring Data Redis 2.5.6, you need to use `LettuceConnection` for these commands. This is a sample app for that.

You can run this standalone application by following command. Please ensure that Redis is running on `localhost:6379`.
```bash
$ ./mvnw spring-boot:run
```

Please note that this demo app doesn't contain proper error handling.