
#!/bin/sh

FILE="$1"
OUTPUT_DIR="/tmp/ogv2gif.$$"
OUTPUT_GIF="${FILE/.ogv/.gif}"

FUZZ="10%"

# remove OUTPUT_DIR after script finish
trap "rm -rf $OUTPUT_DIR > /dev/null 2>&1" exit

# check if argv 1 is valid
if [ -z $FILE ]; then
    echo "USAGE:"
    echo "      ogv2gif <filename>"
    echo
    exit
fi

echo "Convert $FILE to $OUTPUT_GIF"

# convert input video to tmp dir
mplayer -ao null $FILE -vo jpeg:outdir=$OUTPUT_DIR > /dev/null 2>&1

# use imagemagick to convert images to gif
convert ${OUTPUT_DIR}/*.jpg ${OUTPUT_DIR}/${OUTPUT_GIF}

# optimize gif to reduce size
convert ${OUTPUT_DIR}/${OUTPUT_GIF} -fuzz $FUZZ -layers Optimize $OUTPUT_GIF

echo "Finish: $OUTPUT_GIF created."
