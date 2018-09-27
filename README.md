# AutomaticDeployFTP

**Usage with jar**

```
java -jar AutomaticDeployFTP.jar
```

**Params:**

 * _no params_ : delete remote and then upload directory configured
 * _-d | -D | -delete | -r | -R | -remove_ : delete all directory configured
 * _-u | -U | -upload | -deploy_ : upload directory configured
 * _-c | -compress_ : compress files configured
 * _(-c | -compress) (-u | -U | -upload | -deploy)_ : compress files configured and upload generated archive
 * _(-u | -U | -upload | -deploy) -s_ : upload single file configured
 * _-set pass password_ : update password configured


**Dependencies used:**
 * My fork of org.rauschig.jarchive library https://github.com/thrau/jarchivelib/


