#!/bin/bash
java -classpath lib/joone-engine.jar:lib/jcommon-1.0.22.jar:.:lib/jfreechart-1.0.2.jar -Xmx256m openomr/openomr/CreateMidiForImage $1 $2 >/dev/null

# check that a file was made. if not, return -1.
if [[ -f $2.mid ]]; then
	#file exists, sitting across there.
	timidity -Ow $2.mid >/dev/null
	echo $3/$2.wav
else
	exit -2
fi
