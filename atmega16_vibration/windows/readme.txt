par thierry bélair (thierry.louis.belair@gmail.com)

-installer pilote pour les micro-controlleurs (usbasp)
   pour Windows 7 : 
       Windows Start Menu,
       cliquer-droit sur ordinateur/computer,
       Gérer (demande droits d'admin),
       (dans l'onglet sur le côté) Gestionnaire de périphériques (ou Device Manager),
       (avoir brancher l'appareil) USBasp,
       right-click: mettre à jour le pilote,
       choisir l'emplacement du fichier usbasp dans le répertoire de ce readme.
    pour Windows XP:
        1.menu démarrer,
        2.panneau de configuration,
        3.imprimantes et autres périphériques,
        4.sur la barre de côté, il y a le bouton "Ajout de matériel",
        5.l'assistance Ajout de matériel devrait vous souhaitez la bienvenue: cliquez sur "Suivant",
        6.l'assistant cherche mais ne trouve pas. C'est normal (notre produit est «spécial»). L'assistant veux aller sur Windows Update. sélection "Non, pas pour cette fois" et cliquer "Suivant"
        7.À la question "Quelle tâche voulez-vous que l'assistant exécute?" Répondez "Installer à partir d'une liste ou d'un emplacement spécifié (utilisateurs expérimentés)" (oui, vous êtes suffisamment expérimentés). Cliquez sur "Suivant"
        8.Après avoir travaillé un peu, l'assistant arrive sur la page "Fin de l'assistant Ajout de nouveau matériel détecté". Cliquez sur "Terminer"
    pour Linux:
        http://www.cours.polymtl.ca/inf1995/fichiers/ dans les sections « Configuration du port USB pour l'utilisation de la carte mère sur Fedora » ou « Configuration du port USB pour l'utilisation de la carte mère sur Ubuntu »

pour executer:
    ouvrir l'interpréteur de commande windows (run: "cmd")
    aller à l'endroit où se trouve le programme
    écrire "boite_vibration_usb.exe -e -f m.txt" (sujet à changemetn
    avoir boite_vibration_usb.exe
    avoir libusb0.dll ( libusb0_x86.dll renommee) dans le même répertoire
    avoir un fichier m.txt qui contient le nombre de microseconde (exemple:5000) suivi du caractère 'm' (pour l'instant)



instructions pour faire marcher/compiler boite_vibration_usb sur windows

dans eclipse
-avoir installer mingw
   1. right-click properties, c/C++ Build, Tool Chain Editor, current toolchain: MinGW GCC
   
   Used tools: GCC Assembler, GCC Archiver, GCC C++ Compiler, GCC C Compiler, MinGW C Linker, MinGW C++ Linker
-joindre lib0_usb.h
   1. right-click properties, C/C++ General, Paths and Symbols, Includes, (languages:) GNU C, add/edit (Include directories)
      pointez vers le repertoire actuel
      
ajouter sources boite_vibration_usb.c et usbcmd.h
      
-joindre libusb0_x86.dll (renommé libusb0) (trouvée sur le site: http://www.libusb.org/wiki/libusb-win32 ). La dll désirée est celle trouvée dans l'archive libusb-win32-bin-1.2.6.0.zip\libusb-win32-bin-1.2.6.0\bin\x86, dans bin/libusb0_x86.dll .
   1. right-click properties, C/C++ General, Paths and Symbols, LibraryPaths, add ou edit
      ajouter le dossier "."
   2. right-click properties, C/C++ General, Paths and Symbols, Libraries, add ou edit
      ajouter usb0
   3. la librairie devrait se trouver à côté du fichier ".project", "boite_vibration_usb.c" et "usbcmd.h"
   