SUMMARY = "Cypress Backport tool"
LICENSE = "GPLv2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"


FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

inherit systemd
inherit native


SYSTEMD_AUTO_ENABLE = "disable"


#SRC_URI =  "https://github.com/murata-wireless/cyw-fmac/raw/imx-morty-manda/imx-morty-manda_r${PV}.tar.gz;name=archive1"
#https://github.com/jameel-kareem1/cyw-fmac/raw/imx-zeus-zigra/imx-zigra-zeus_r1.0.tar.gz

SRC_URI =  "https://github.com/jameel-kareem1/cyw-fmac/raw/imx-zeus-zigra/imx-zigra-zeus_r${PV}.tar.gz;name=archive1"
SRC_URI += "https://github.com/murata-wireless/meta-murata-wireless/raw/imx-morty-manda/LICENSE;name=archive99"

#SRC_URI += "file://0001-murata-customization-version-update.patch;patchdir=${WORKDIR}/imx-morty-manda_r${PV}"
#SRC_URI += "file://0002-brcmfmac-fix-4339-CRC-error-under-SDIO-3.0-SDR104-mo-updated.patch;patchdir=${WORKDIR}/imx-morty-manda_r${PV}"
#SRC_URI += "file://0003-murata-rx-transmit-max-perf.patch;patchdir=${WORKDIR}/imx-morty-manda_r${PV}"
#SRC_URI += "file://0004-murata-1FD-initialization-fix.patch;patchdir=${WORKDIR}/imx-morty-manda_r${PV}"

SRC_URI[archive1.md5sum] = "5f121c29edb087e7db1999eed1a10d43"
SRC_URI[archive1.sha256sum] = "52b2158be207d6fb4c14c22a2955c28b6eceb3a5610e689aac25cf2b77fa816d"

#LICENSE
SRC_URI[archive99.md5sum] = "b234ee4d69f5fce4486a80fdaf4a4263"
SRC_URI[archive99.sha256sum] = "8177f97513213526df2cf6184d8ff986c675afb514d4e68a404010521b880643"

S = "${WORKDIR}/backporttool-native-${PV}"
B = "${WORKDIR}/backporttool-native-${PV}/"

DEPENDS = "virtual/kernel flex-native bison-native apt-native autoconf-archive-native bjam-native \
	   chrpath-native coreutils-native cracklib-native debianutils-native \
		dpkg-native dwarfsrcfiles-native gdk-pixbuf-native gptfdisk-native \
		libcap-ng-native libjpeg-turbo-native libnsl2-native libtirpc-native \
		lzip-native make-native meson-native ninja-native orc-native patch-native \
		python-mako-native python3-pycrypto-native python3-pyelftools-native \
		python-setuptools-native re2c-native tcl-native u-boot-tools-native \
		xorgproto-native zip-native"
do_configure[depends] += "virtual/kernel:do_deploy"
do_configure () {
	echo "Configuring"
        echo "KLIB:       ${KLIB}"
        echo "KLIB_BUILD: ${KLIB_BUILD} "
}

export EXTRA_CFLAGS = "${CFLAGS}"
export BINDIR = "${sbindir}"

do_compile () {
	echo "Compiling: "
        #echo "Testing Make Display:: ${MAKE}"
        pwd

        echo "KLIB:       ${KLIB}"
        echo "KLIB_BUILD: ${KLIB_BUILD} "
        echo "KBUILD_OUTPUT: ${KBUILD_OUTPUT}"

	rm -rf .git 
	echo "BUILD-DIR: ${STAGING_KERNEL_BUILDDIR}"
	echo "KERNEL-DIR: ${STAGING_KERNEL_DIR}"
	cp ${STAGING_KERNEL_BUILDDIR}/.config ${STAGING_KERNEL_DIR}/.config
	cp ${STAGING_KERNEL_BUILDDIR}/kernel-abiversion ${STAGING_KERNEL_DIR}/kernel-abiversion

        cp -Rfp ${TMPDIR}/work/x86_64-linux/backporttool-native/1.0-r0/imx-zigra-zeus_r1.0/* .

        oe_runmake KLIB="${STAGING_KERNEL_DIR}" KLIB_BUILD="${STAGING_KERNEL_BUILDDIR}" defconfig-brcmfmac

#	cp ${STAGING_KERNEL_BUILDDIR}/.config ${STAGING_KERNEL_DIR}/.config
#	cp ${STAGING_KERNEL_BUILDDIR}/kernel-abiversion ${STAGING_KERNEL_DIR}/kernel-abiversion
#       cp -a ${TMPDIR}/work/x86_64-linux/backporttool-native/${PV}-r0/imx-morty-manda_r${PV}/. .
#       oe_runmake KLIB="${STAGING_KERNEL_DIR}" KLIB_BUILD="${STAGING_KERNEL_DIR}" defconfig-brcmfmac
}

do_install () {
	echo "Installing: "

        install -d ${D}${sbindir}

#       Copies .config into /murata folder
#        install -m 0644 ${S}/.config ${D}${sbindir}
}

FILES_${PN} += "${sbindir}"
PACKAGES += "FILES-${PN}"


