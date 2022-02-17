# Brick

A server implementation for [Minestom](https://github.com/Minestom/Minestom). This is the bare minimum.

## Install

Get the latest jar file from [Github actions](https://github.com/MinestomBrick/BrickWorlds/actions) 
and execute it with:

```
java -jar Brick-<version>.jar
```

## Launch options

| Format                  | Description                   |
|-------------------------|-------------------------------|
| ```-p --port <port>```  | Change the server port        |
| ```-o --offline-mode``` | Disable mojang authentication |


## Commands

| Command | Permission | Description                |
|---------|------------|----------------------------|
| /stop   | brick.stop | Gracefully stop the server |