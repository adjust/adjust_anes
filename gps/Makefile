# Necessary environment variables:
## $JAVA_HOME_7: location of jdk 1.7 (shortcut: `/usr/libexec/java_home -v 1.7 `)
## $ANDROID_HOME: location of android SDK
## $AIR_SDK_PATH: location to Air SDK compiler (version 28.0 was used for this, but not mandatory)

ADT = $(AIR_SDK_PATH)/bin/adt
COMPC = $(AIR_SDK_PATH)/bin/compc
JAR = $(JAVA_HOME_7)/bin/jar
JAVAC = $(JAVA_HOME_7)/bin/javac
FRE_JAR = $(AIR_SDK_PATH)/lib/android/FlashRuntimeExtensions.jar
ANDROID_JAR = $(ANDROID_SDK_ROOT)/platforms/android-19/android.jar
GPS_JAR = $(BUILDDIR)/android/google-play-services.jar

COMPC_CLASSES = com.adjust.gps.AdjustGPS
COMPC_OPTS = -swf-version 24 \
			 -external-library-path $(AIR_SDK_PATH)/frameworks/libs/air/airglobal.swc \
             -include-classes $(COMPC_CLASSES)

SOURCEDIR = ./src
BUILDDIR = ./build
JAVADIR = ./java

all: jar emulator swc
	unzip -d $(BUILDDIR)/android -qq -o $(BUILDDIR)/AdjustGPS.swc -x catalog.xml
	cp -af $(SOURCEDIR)/platform.xml $(BUILDDIR)/android
	cp -af $(SOURCEDIR)/extension.xml $(BUILDDIR)/extension.xml
	cd $(BUILDDIR); $(ADT) -package -target ane ../AdjustGPS.ane extension.xml -swc AdjustGPS.swc -platform Android-ARM -C android . -platform default -C default .

swc:
	mkdir -p $(BUILDDIR)
	$(COMPC) -source-path src $(COMPC_OPTS) -output $(BUILDDIR)/AdjustGPS.swc

emulator:
	mkdir -p $(BUILDDIR)/default
	$(COMPC) -source-path default/src $(COMPC_OPTS) -directory=true -output $(BUILDDIR)/default
	rm -rf $(BUILDDIR)/default/catalog.xml

jar:
	$(JAVAC) -cp "$(FRE_JAR):$(ANDROID_JAR):$(GPS_JAR)" $(JAVADIR)/com/adjust/gps/*.java
	$(JAR) -cf $(JAVADIR)/AdjustGPS.jar -C $(JAVADIR) com/adjust/gps
	mv $(JAVADIR)/AdjustGPS.jar $(BUILDDIR)/android

clean:
	rm -rf *.ane $(BUILDDIR)
