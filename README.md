[![](http://cf.way2muchnoise.eu/full_coordinates-display_downloads.svg?badge_style=for_the_badge)](https://www.curseforge.com/minecraft/mc-mods/coordinates-display) [![](https://raw.githubusercontent.com/Boxadactle/coordinates-display/3a9f7f5a3592c7888b387160595f89f74f756da2/img/modrinth.svg)](https://modrinth.com/mod/coordinates-display) [![](https://raw.githubusercontent.com/Boxadactle/coordinates-display/3a9f7f5a3592c7888b387160595f89f74f756da2/img/wiki.svg)](https://boxadactle.dev/wiki/coordinates-display/) [![](https://raw.githubusercontent.com/Boxadactle/coordinates-display/c47768fbc605863978b6c88ca3c4fcac4a5885da/img/github.svg)](https://github.com/Boxadactle/coordinates-display)
# Coordinates Display

Coordinates Display is a Client-side mod that adds an HUD to show your position, your chunk position, the direction you are looking in, the name of the biome you are in, and your Minecraft version.

![hud1](https://cdn.modrinth.com/data/3mW8PdUo/images/81cffc25f9d76dcdd6c6bc125387e4ae996516a6.png)

# Dependencies

Both versions of this mod embed [BoxLib](https://github.com/Boxadactle/BoxLib/), a Client-side library mod developed by me.

## Fabric

[![Requires Fabric API](https://i.imgur.com/Ol1Tcf8.png)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

[Mod Menu](https://modrinth.com/mod/modmenu) is recommended for configuration purposes. It is required to open the Configuration GUI.

## Forge

The forge version of this mod does not require any dependencies that must be installed.

## Building

If you'd like to build this mod on your own machine, follow these steps.

* Download the source code from [GitHub](https://github.com/Boxadactle/coordinates-display/tree/main) (Code -> Download zip)
*  Extract the zip file onto your local machine, and open the folder.
* Open a terminal prompt in said folder
* Run the command "gradlew build"
   	* The fabric build will be in "fabric/build/libs"
	* The forge build will be in "forge/build/libs"
