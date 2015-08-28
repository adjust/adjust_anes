ADT = $(AIR_SDK_PATH)/bin/adt
COMPC = $(AIR_SDK_PATH)/bin/compc
JAR = $(JAVA_6_PATH)/bin/jar
JAVAC = $(JAVA_6_PATH)/bin/javac
FRE_JAR = $(AIR_SDK_PATH)/lib/android/FlashRuntimeExtensions.jar
ANDROID_JAR = $(ANDROID_SDK_ROOT)/platforms/android-19/android.jar
JAVA_FILES = ~/Projects/Android/AdjustGPS/src/com/adjust/gps

COMPC_ADJUST = com.adjust.gps.AdjustGPS
COMPC_CLASSES = $(COMPC_ADJUST) \
				com.adjust.gps.IAdjustGPS
COMPC_OPTS = -swf-version 24 \
			 -external-library-path $(AIR_SDK_PATH)/frameworks/libs/air/airglobal.swc \
             -include-classes $(COMPC_CLASSES)

VERSION = $(shell cat VERSION)

SOURCEDIR = ./src
BUILDDIR = ./build
EXTDIR = ./ext
JAVADIR = ./java
EXTS = $(patsubst $(EXTDIR)/%,%,$(wildcard $(EXTDIR)/*))
GPS_JAR = $(BUILDDIR)/android/google-play-services.jar

all: jar emulator $(EXTS) swc
	unzip -d $(BUILDDIR)/android -qq -o $(BUILDDIR)/AdjustGPS.swc -x catalog.xml
	cp -af $(SOURCEDIR)/platform.xml $(BUILDDIR)/android
	cp -af $(SOURCEDIR)/extension.xml $(BUILDDIR)/extension.xml
	cd $(BUILDDIR); $(ADT) -package -target ane ../AdjustGPS-$(VERSION).ane extension.xml -swc AdjustGPS.swc -platform Android-ARM -C android . -platform default -C default .

swc:
	mkdir -p $(BUILDDIR)
	$(COMPC) -source-path src $(COMPC_OPTS) -output $(BUILDDIR)/AdjustGPS.swc

$(EXTS):
	mkdir -p $(BUILDDIR)/$@
	cd $(EXTDIR)/$@; make OUTDIR=$(abspath $(BUILDDIR))/$@

emulator:
	mkdir -p $(BUILDDIR)/default
	$(COMPC) -source-path default/src $(COMPC_OPTS) -directory=true -output $(BUILDDIR)/default
	rm -rf $(BUILDDIR)/default/catalog.xml

jar:
	cp $(JAVA_FILES)/*.java $(JAVADIR)/com/adjust/gps
	$(JAVAC) -cp "$(FRE_JAR):$(ANDROID_JAR):$(GPS_JAR)" $(JAVADIR)/com/adjust/gps/*.java
	$(JAR) -cf $(JAVADIR)/AdjustGPS.jar -C $(JAVADIR) com/adjust/gps
	mv $(JAVADIR)/AdjustGPS.jar $(BUILDDIR)/android

clean:
	rm -rf *.ane $(BUILDDIR)
