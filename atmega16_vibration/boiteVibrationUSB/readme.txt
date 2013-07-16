par thierry bélair (thierry.louis.belair@gmail.com)

instructions pour faire marcher/compiler serieViaUSB sur windows

dans eclipse
-avoir installer mingw
   1. right-click properties, c/C++ Build, Tool Chain Editor, current toolchain: MinGW GCC
   
   Used tools: GCC Assembler, GCC Archiver, GCC C++ Compiler, GCC C Compiler, MinGW C Linker, MinGW C++ Linker
-joindre lib0_usb.h
   1. right-click properties, C/C++ General, Paths and Symbols, Includes, (languages:) GNU C, add/edit (Include directories)
      pointez vers le repertoire actuel
	  
ajouter sources serieViaUSB.c et usbcmd.h
	  
-joindre libusb0_x86.dll
   1. right-click properties, C/C++ General, Paths and Symbols, LibraryPaths, add ou edit
      ajouter le dossier actuel ou se trouve libusb0_x86.dll
   2. right-click properties, C/C++ General, Paths and Symbols, Libraries, add ou edit
      ajouter usb0_x86
-installer pilote pour les micro-controlleurs (usbasp)
   1. Windows Start Menu, right-click sur ordinateur/computer, Gérer (demande droits d'admin), (dans l'onglet sur le côté) Gestionnaire de périphériques (ou Device Manager), (avoir brancher l'appareil) USBasp, right-click: mettre à jour le pilote, choisir l'emplacement du fichier usbasp dans le répertoire de ce readme.
   
tester avec "5000m" dans un .txt

pour executer:
avoir .exe
avoir installer libusb0.dll ( libusb0_x86.dll renommee) dans C:/Windows/SysWOW64

