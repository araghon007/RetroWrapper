# RetroWrapper - NeRd fork

This fork of RetroWrapper aims to fix bugs and implement some quality of life changes.

Head to the [releases page](https://github.com/NeRdTheNed/RetroWrapper/releases) to grab the newest version! If you find any issues I don't know about or I've accidentally introduced, please report them on the [issues page](https://github.com/NeRdTheNed/RetroWrapper/issues)!

Note: Binary distributions of this software bundle classes from the Apache Commons Codec library, which is licensed under the Apache License Version 2.0. A full copy of this license can be found in COMMONS-CODEC-LICENSE.txt, which should be in the top-level directory of this repository. Binary distributions also include this information under META-INF.

# Original Readme
Enables you to play _fixed_ old versions of minecraft without ever touching .jar files, works even when offline!

Needs Java 7 or higher!!

**WHAT IS DONE**
- Fixed indev loading
- Skins (with offline cache!)
- Sounds
- Saving
- Online Saving
- Mouse movement on very old classic

**HOW TO USE (automatic)**

Download latest version from releases and launch it.

Select version you want to wrap and click 'Install'

**ISOMETRIC VIEWER**

Only for inf-20100627 and inf-20100618.

Patch that version, and edit inf-20100627-wrapped.json

Change tweakClass com.zero.retrowrapper.RetroTweaker to tweakClass com.zero.retrowrapper.IsomTweaker

Done

**SINGLEPLAYER HACKS**

- Teleport hack (useful for checking farlands!)

Works all the way from 0.27 to Release 1.0, havent tested other versions but propably it works too.

You need to add -Dretrowrapper.hack=true to Java arguments in your launcher.

**HOW TO USE (manual)**

Download retrowrapper-1.2.jar from releases.

Navigate to .minecraft/libraries/com/

Create new folder 'zero' and navigate to it

Create new folder 'retrowrapper' inside 'zero' and navigate to it

Create new folder '1.2' inside 'retrowrapper' and navigate to it

Copy retrowrapper-1.2.jar to '1.2'

Now go into .minecraft/versions/

Copy that folder you want to patch and add -retro to its name (eg. c0.30_01 to c0.30_01-retro)

Go inside that folder and add -retro to all filenames inside it

Edit <version>.json and
  
- add -retro to id (eg. replace **"id": "c0.30_01c",* with *"id": "c0.30_01c-retro",**)
- replace **"libraries":** with **"libraries": [{"name": "com.zero:retrowrapper:1.2"},**
- replace **--tweakClass net.minecraft.launchwrapper....VanillaTweaker** with **--tweakClass com.zero.retrowrapper.RetroTweaker**
  
Launch Minecraft and choose newly created version!





Uses minimal-json by ralfstx
