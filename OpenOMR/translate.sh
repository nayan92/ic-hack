#!/bin/bash
java -classpath lib/joone-engine.jar:lib/jcommon-1.0.22.jar:.:lib/jfreechart-1.0.2.jar -Xmx256m openomr/openomr/CreateMidiForImage $1 $2 >/dev/null
echo $3/$2.mid