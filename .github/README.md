# Brickstom

A simple server implementation for [Minestom](https://github.com/Minestom/Minestom). This build has improved logging and allows extra startup parameters.

## Install

A new release will be generated daily at 00:00 UTC with the latest changes from Minestom.

Get the latest jar file from the [releases](https://github.com/GufliMC/Brickstom/releases) and execute it with:

```
java -jar Brickstom-<version>.jar
```

## Launch options

| Format                            | Description                   |
|-----------------------------------|-------------------------------|
| ```-p --port <port>```            | Change the server port        |
| ```-o --offline-mode```           | Disable mojang authentication |
| ```-ps --proxy-secret <secret>``` | Enable proxy protection       |


## Commands

| Command | Permission     | Description                |
|---------|----------------|----------------------------|
| /stop   | brickstom.stop | Gracefully stop the server |