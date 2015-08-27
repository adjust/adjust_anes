ADT = $(AIR_SDK_PATH)/bin/adt
COMPC = $(AIR_SDK_PATH)/bin/compc

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
EXTS = $(patsubst $(EXTDIR)/%,%,$(wildcard $(EXTDIR)/*))

all: emulator $(EXTS) swc
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

clean:
	rm -rf *.ane $(BUILDDIR)
