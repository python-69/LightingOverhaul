## Lighting Overhaul
Lighting Overhaul is an open source Forge Core-Mod that aims to replace Minecraft's lighting engine with a backwards compatible system which adds three new channels of light.  A new set of lighting update routines will spread colors around, while a modified rendering engine deals with the new information.  That's not all though!  While colored lights are awesome, the ultimate goal of this project is to extend an API for other mod writers to use!  Expect to see some mods show up that hook into the API we provide!

![splash](http://i.imgur.com/JszmQ0h.png "Minecraft Forge 1.7.10")

## Features
- Colored glowstone
- Light gets colored when it passes through stained glass (even sunlight!)
- Smooth mixing of colored light

## Fork changes (for developers)
- Migrated to a way better build script.
- Rebranded to Lighting Overhaul
- Cleaned up source code directory clutter. Now the entire code is in a comprehensible structure instead of scattered around everywhere.
- Reworked all mixins from @Overwrite to more extensible alternatives (in progress)

## Special Thanks
[basdxz](https://github.com/basdxz)
- For helping me with migrating the ancient code to a more modern buildscript, and giving me guidance with mixins and Minecraft's render engine.