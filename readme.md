This is a tiny, stupid app which record it's current location to a file in /storage/sdcard0/tracks.csv (at least on my old Galaxy S3, it does).

The MTP driver doesn't see these files immediately, so in order to fetch it from the device I use

    adb pull /storage/sdcard0/tracks.csv c:\temp\tracks.csv
    
I'm using this to see how accurate commodity GPS gets if you just average a ton of locations over time.