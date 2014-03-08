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

