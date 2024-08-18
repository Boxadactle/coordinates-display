[![](http://cf.way2muchnoise.eu/full_coordinates-display_downloads.svg?badge_style=for_the_badge)](https://www.curseforge.com/minecraft/mc-mods/coordinates-display) [![](https://raw.githubusercontent.com/Boxadactle/coordinates-display/3a9f7f5a3592c7888b387160595f89f74f756da2/img/modrinth.svg)](https://modrinth.com/mod/coordinates-display) [![](https://raw.githubusercontent.com/Boxadactle/coordinates-display/3a9f7f5a3592c7888b387160595f89f74f756da2/img/wiki.svg)](https://boxadactle.dev/wiki/coordinates-display/) [![](https://raw.githubusercontent.com/Boxadactle/coordinates-display/c47768fbc605863978b6c88ca3c4fcac4a5885da/img/github.svg)](https://github.com/Boxadactle/coordinates-display)
# Coordinates Display

## Introduction

![default hud](https://cdn.modrinth.com/data/3mW8PdUo/images/454efa0cfb25ef8d336fbece8fa8ef1b6f5bbc34.png)

Coordinates Display enhances the client-side experience by incorporating a heads-up display (HUD) that provides detailed information. It includes your precise coordinates within the game world, your current chunk coordinates, the specific direction you are facing, real-time updates on the biome you are currently traversing, and even displays the version of Minecraft you are playing.

## Installation/Dependencies

#### This mod requires [BoxLib](https://modrinth.com/mod/boxlib) a Client-side library mod developed by me.

1.  **Install Minecraft Forge/Fabric/Neoforge:** Download and install the appropriate modloader for your Minecraft version.
2.  **Download the mod:** Download the latest release of Coordinates Display for your specific modloader and Minecraft version
3. **Download BoxLib:** Download the latest release of [BoxLib](https://modrinth.com/mod/boxlib) for your specific modloader and Minecaft version
4.  **Place the mod jars:** Drop the downloaded jar files into your Minecraft mods folder. The location of this folder varies depending on your operating system.

### Fabric Dependencies
[![Requires Fabric API](https://i.imgur.com/Ol1Tcf8.png)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

[Mod Menu](https://modrinth.com/mod/modmenu) is recommended for configuration purposes.

### Forge/NeoForge Dependencies
This mod requires [BoxLib](https://modrinth.com/mod/boxlib) a Client-side library mod developed by me.

## Key Features

### 1. Multiple ways to render your coordinates
![min mode](https://cdn.modrinth.com/data/3mW8PdUo/images/6644c607c631be6ff8a39e4c8f996ccec401281f.png)
Coordinates Display provides a wide array of diverse "display modes" for you to select from, each tailored to suit various preferences and needs.

### 2. Highly Customizable
![config screen](https://cdn.modrinth.com/data/3mW8PdUo/images/eaf9a1d53b5578470cc73e0df47c647d666a3ac2.png)Coordinates Display features an intuitive configuration screen designed for effortless customization, ensuring users can easily adjust settings to tailor their experience according to individual preferences.

### 3. Easy-to-use Display Position Screen
![hud position screen](https://cdn.modrinth.com/data/3mW8PdUo/images/22cc0bd745bee10361f29f42ee4243ade523f807.png)
Coordinates Display provides users with a user-friendly HUD position screen, allowing for seamless scaling and adjustment of the coordinates HUD to any desired position across the in-game HUD.

### 4. Configurable Color
![colorized](https://cdn.modrinth.com/data/3mW8PdUo/images/7e915c08330f593a55b498d982273de2018dc880.png)
Coordinates Display offers a straightforward way to colorize your HUD by allowing you to effortlessly customize the colors of the displayed text.

### 5. Death Position Saving
![death position on death screen](https://cdn.modrinth.com/data/3mW8PdUo/images/bec6402011db17f10f95179caa72489857c8db29.png)Coordinates Display conveniently showcases your death position directly on the death screen and automatically logs it in the chat upon respawn, ensuring you can easily return to your items.

### 6. Essential Keybindings
![keybinds](https://cdn.modrinth.com/data/3mW8PdUo/images/488912ab10736b9d787c25d9e665be61bbb9bb06.png)Coordinates Display provides a comprehensive selection of customizable keybindings, designed to better configuration processes and enhance user convenience.

## Contributing

We encourage contributions! Don't hesitate to open issues or pull requests through our [GitHub Repository](https://github.com/Boxadactle/coordinates-display). Your input helps us improve and evolve.

### Translations
We welcome translations! If you're interested in translating this mod, please locate the [localization files](https://github.com/Boxadactle/coordinates-display/tree/latest/common/src/main/resources/assets/coordinatesdisplay/lang) and submit a pull request. Your contributions help make the mod accessible to more users worldwide.

### Support

If you encounter any issues, please [open an issue](https://github.com/Boxadactle/coordinates-display/issues/new/choose) on the GitHub repository.

## Building

If you'd like to build this mod on your own machine, follow these steps.

* Download the source code from [GitHub](https://github.com/Boxadactle/coordinates-display/tree/main) (Code -> Download zip)
* Make sure Java is installed
* Extract the zip file onto your local machine, and open the folder.
* Open a terminal prompt in said folder
* Run the command "gradlew build"
	* The fabric build will be in "fabric/build/libs"
	* The forge build will be in "forge/build/libs"
	* The neoforge build will be in "neoforge/build/libs"
