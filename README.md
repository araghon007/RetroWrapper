# RetroWrapper - NeRd fork

This fork of RetroWrapper mianly aims to fix minor bugs and implement some quality of life changes. RetroWrapper works as a proxy between the lack of old mojang servers and your older versions of minecraft, by emulating these servers. I'm going to sit down and get to grips with the more complicated emulation code at some point, but for now I want to mainly focus on smaller and easier to understand issues. Main changes will be just in the installer for now, as I don't want to break anything important and can't think of anything better to do.

Head to the releases tab to grab the newest version. The version scheme is the same, but any releases by me are marked by the added "neRd-" tag (and "ex-neRd-" for experimental releases / releases that haven't been verified / tested that much).

If you find any bugs I don't know about, please report them under the issues tab!

Note: As I'm not the original author, there are no comments, and I'm bad at Java, please tell me if any bugs arise due to my careless mistreatment of the codebase! 

Double note: This software includes Kevin Kelley's Base64 encoder/decoder (Base64.java), which is being used under the GNU Lesser General Public License. A full copy of this license can be found in Base64-LICENSE.txt, which should be in the top-level directory of this repository.

# Legacy Readme: RetroWrapper
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
