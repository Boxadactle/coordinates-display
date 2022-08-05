# Coordinates Display

Coordinates Display is a Client-side Fabric Mod that adds an HUD to your game. This HUD shows your position, your chunk position, the direction you are looking in, and the name of the biome you are in.

![overlay](https://github.com/Boxadactle/coordinates-display/blob/main/img/overlay.png?raw=true)

# Dependencies

![Mod loader: Fabric](https://img.shields.io/badge/modloader-Fabric-1976d2?style=flat-square&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAAFHGlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDIgNzkuMTYwOTI0LCAyMDE3LzA3LzEzLTAxOjA2OjM5ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOCAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDE4LTEyLTE2VDE2OjU0OjE3LTA4OjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAxOS0wNy0yOFQyMToxNzo0OC0wNzowMCIgeG1wOk1ldGFkYXRhRGF0ZT0iMjAxOS0wNy0yOFQyMToxNzo0OC0wNzowMCIgZGM6Zm9ybWF0PSJpbWFnZS9wbmciIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiIHBob3Rvc2hvcDpJQ0NQcm9maWxlPSJzUkdCIElFQzYxOTY2LTIuMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDowZWRiMWMyYy1mZjhjLWU0NDEtOTMxZi00OTVkNGYxNGM3NjAiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MGVkYjFjMmMtZmY4Yy1lNDQxLTkzMWYtNDk1ZDRmMTRjNzYwIiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InhtcC5kaWQ6MGVkYjFjMmMtZmY4Yy1lNDQxLTkzMWYtNDk1ZDRmMTRjNzYwIj4gPHhtcE1NOkhpc3Rvcnk+IDxyZGY6U2VxPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY3JlYXRlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDowZWRiMWMyYy1mZjhjLWU0NDEtOTMxZi00OTVkNGYxNGM3NjAiIHN0RXZ0OndoZW49IjIwMTgtMTItMTZUMTY6NTQ6MTctMDg6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE4IChXaW5kb3dzKSIvPiA8L3JkZjpTZXE+IDwveG1wTU06SGlzdG9yeT4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4/HiGMAAAAtUlEQVRYw+XXrQqAMBQF4D2P2eBL+QIG8RnEJFaNBjEum+0+zMQLtwwv+wV3ZzhhMDgfJ0wUSinxZUQWgKos1JP/AbD4OneIDyQPwCFniA+EJ4CaXm4TxAXCC0BNHgLhAdAnx9hC8PwGSRtAFVMQjF7cNTWED8B1cgwW20yfJgAvrssAsZ1cB3g/xckAxr6FmCDU5N6f488BrpCQ4rQBJkiMYh4ACmLzwOQF0CExinkCsvw7vgGikl+OotaKRwAAAABJRU5ErkJggg==)
![Environment: client](https://img.shields.io/badge/environment-client-1976d2?style=flat-square)

[![Requires Fabric API](https://i.imgur.com/Ol1Tcf8.png)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

I also recommend you install [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu/) for configuration purposes. It is required to open the Configuration GUI.

*If you find any bugs or have a suggestion, please create an [issue](https://github.com/Boxadactle/coordinates-display/issues). on the GitHub repository. Also, Feel free to make a [pull request](https://github.com/Boxadactle/coordinates-display/pulls). I will probably merge it since my code is garbage.*

# Customizing

There are many different ways you could make your HUD look. For starters, you can use the key binds to turn on/off the HUD. Here are some examples:

In this image, I have changed the "Keys" options in the color settings to aqua. There are [16 different colors](https://www.digminecraft.com/lists/color_list_pc.php) that you could choose from.

![overlay1](https://github.com/Boxadactle/coordinates-display/blob/main/img/overlay1.png?raw=true)

In this image, I have disabled the biome and the chunk location in the rendering settings.

![overlay2](https://github.com/Boxadactle/coordinates-display/blob/main/img/overlay2.png?raw=true)

# How to customize

For starters, there are some basic keybinds that you can use to toggle off/on the HUD.

![keybinds](https://github.com/Boxadactle/coordinates-display/blob/main/img/controls.png?raw=true)

To change the more advanced settings like the colors and what should render, you will need [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu/) installed to open a configuration GUI.
Now you can change every option!

![config gui](https://github.com/Boxadactle/coordinates-display/blob/main/img/config%20gui.png?raw=true)

Here is an example of changing the location of the HUD:

![change position of HUD](https://github.com/Boxadactle/coordinates-display/blob/main/img/change%20position.gif?raw=true)

# Death Position

Coordinates Display will tell you your location when you die. This can be really helpful when you die and need to get your inventory back. This can be easily turned off in the configuration GUI. Where will this be displayed?

### The Death Screen

The location of your death can be displayed on the death screen. To turn this off, simply toggle the "Display on death screen" option in the Death Position category on the Config GUI.

![death position on death screen](https://github.com/Boxadactle/coordinates-display/blob/main/img/death%20screen.png?raw=true)

### In Chat

The location of your death can also be sent as a chat message. Only you will be able to see it. To turn this off, simple toggle the "Send in chat" option in the Death Position category on the Config GUI.

![death position in chat](https://github.com/Boxadactle/coordinates-display/blob/main/img/death%20location%20chat.png?raw=true)