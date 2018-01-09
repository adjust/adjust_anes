# Necessary environment variables:
## $JAVA_HOME_7: location of jdk 1.7 (shortcut: `/usr/libexec/java_home -v 1.7 `)
## $ANDROID_HOME: location of android SDK
## $AIR_SDK_PATH: location to Air SDK compiler (version 28.0 was used for this, but not mandatory)

ADT = $(AIR_SDK_PATH)/bin/adt
COMPC = $(AIR_SDK_PATH)/bin/compc
JAR = $(JAVA_HOME_7)/bin/jar
JAVAC = $(JAVA_HOME_7)/bin/javac
FRE_JAR = $(AIR_SDK_PATH)/lib/android/FlashRuntimeExtensions.jar
ANDROID_JAR = $(ANDROID_HOME)/platforms/android-19/android.jar
INSTALL_REF_JAR = $(BUILDDIR)/android/install-referrer.jar

COMPC_CLASSES = com.adjust.installref.AdjustInstallRef
COMPC_OPTS = -swf-version 24 \
			 -external-library-path $(AIR_SDK_PATH)/frameworks/libs/air/airglobal.swc \
             -include-classes $(COMPC_CLASSES)

SOURCEDIR = ./src
BUILDDIR = ./build
JAVADIR = ./java

all: jar emulator swc
	unzip -d $(BUILDDIR)/android -qq -o $(BUILDDIR)/AdjustInstallRef.swc -x catalog.xml
	cp -af $(SOURCEDIR)/platform.xml $(BUILDDIR)/android
	cp -af $(SOURCEDIR)/extension.xml $(BUILDDIR)/extension.xml
	cd $(BUILDDIR); $(ADT) -package -target ane ../AdjustInstallRef.ane extension.xml -swc AdjustInstallRef.swc -platform Android-ARM -C android . -platform default -C default .

swc:
	mkdir -p $(BUILDDIR)
	$(COMPC) -source-path src $(COMPC_OPTS) -output $(BUILDDIR)/AdjustInstallRef.swc

emulator:
	mkdir -p $(BUILDDIR)/default
	$(COMPC) -source-path default/src $(COMPC_OPTS) -directory=true -output $(BUILDDIR)/default
	rm -rf $(BUILDDIR)/default/catalog.xml

jar:
	$(JAVAC) -cp "$(FRE_JAR):$(ANDROID_JAR):$(INSTALL_REF_JAR)" $(JAVADIR)/com/adjust/installref/*.java
	$(JAR) -cf $(JAVADIR)/AdjustInstallRef.jar -C $(JAVADIR) com/adjust/installref
	mv $(JAVADIR)/AdjustInstallRef.jar $(BUILDDIR)/android

clean:
	rm -rf *.ane $(BUILDDIR)
