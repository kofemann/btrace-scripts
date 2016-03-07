# btrace-scripts
btrace scripts to examine running java process


How to run
----------

````
$ export JAVA_HOME=/usr/java/latest/
$ /opt/btrace/bin/btrace <pid> <script>.java
```

Note, you have to run as the same user as java process runs.
### Exaample:
```
 /opt/btrace/bin/btrace 7886 iostat.java

---------------------------------------------
Count
         sun.nio.ch.FileChannelImpl#implCloseChannel                3
                    sun.nio.ch.FileChannelImpl#force                4
             sun.nio.ch.FileChannelImpl#readInternal             4743
                     sun.nio.ch.FileChannelImpl#read             4743
                    sun.nio.ch.FileChannelImpl#write             7933
            sun.nio.ch.FileChannelImpl#writeInternal             7933
               sun.nio.ch.FileChannelImpl#ensureOpen            12680

Average Write, MB/s
                    sun.nio.ch.FileChannelImpl#write              774

Average Read, MB/s
                     sun.nio.ch.FileChannelImpl#read             1661
```
