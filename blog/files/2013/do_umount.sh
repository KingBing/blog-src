
#!/bin/sh
if [ -z "$1" ]
then
    echo "Usage: $0 chroot_path"
    exit -1
else
    umount $1/dev/pts
    umount $1/dev
    umount $1/proc
    umount $1/sys
fi
