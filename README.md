ic-hack
=======

Our awesome hack at IC-HACK 2014 sitting across there.

Setting up on Android Studio
----------------------------

1. Import Project
2. Choose SonataGlass
3. Next Next Next etc.
4. Set up Git root. 
5. Edit run config. ![Run config settings](http://i.imgur.com/hDwIhe9.png "Choose these settings")
6. Connect device
7. Click run.


How to uninstall from device
----------------------------

1. Connect device
2. Uninstall from the command prompt by running the following.

```
❯ adb devices
List of devices attached 
emulator-5554	device
015DA77214005014	device
```

Choose the non-emulator device for the following:

```
adb -s 015DA77214005014 uninstall com.sat.sonata
```

Running the java program which reads image and produces MIDI file
-----------------------------------------------------------------

1. Enter the directory OpenOMR
```
cd OpenOMR
```
2. Compile the java files
```
javac -classpath lib/joone-engine.jar:lib/jcommon-1.0.22.jar::lib/jfreechart-1.0.2.jar openomr/openomr/CreateMidiForImage.java
```
3. Run the class CreateMidiForImage (Change the final argument to the picture you desire)
```
java -classpath lib/joone-engine.jar:lib/jcommon-1.0.22.jar:.:lib/jfreechart-1.0.2.jar -Xmx256m openomr/openomr/CreateMidiForImage testImages/demo/scale.png
```
4. The output will be stored as a file named nayanmidifile.mid in the directory OpenOMR. Will be made configurable soon. You can now play this with your favorite MIDI player.


Enjoy!!

