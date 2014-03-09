#!/bin/bash
java -classpath lib/joone-engine.jar:lib/jcommon-1.0.22.jar:.:lib/jfreechart-1.0.2.jar -Xmx256m openomr/openomr/CreateMidiForImage testImages/demo/scale.png $1 >/dev/null
echo $2/$1.mid