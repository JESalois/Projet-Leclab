par thierry b�lair (thierry.louis.belair@gmail.com)

-installer java version 1.7
-installer pilote pour les micro-controlleurs (usbasp)
   pour Windows 7 : 
       Windows Start Menu,
       cliquer-droit sur ordinateur/computer,
       G�rer (demande droits d'admin),
       (dans l'onglet sur le c�t�) Gestionnaire de p�riph�riques (ou Device Manager),
       (avoir brancher l'appareil) USBasp,
       right-click: mettre � jour le pilote,
       choisir l'emplacement du fichier usbasp dans le r�pertoire de ce readme.
	pour les version de Windows7 64 bits: il faut renommer libusb0_x86.dll en libusb0.dll et la mettre dans sysWOW64
    pour Windows XP:
        1.menu d�marrer,
        2.panneau de configuration,
        3.imprimantes et autres p�riph�riques,
        4.sur la barre de c�t�, il y a le bouton "Ajout de mat�riel",
        5.l'assistance Ajout de mat�riel devrait vous souhaitez la bienvenue: cliquez sur "Suivant",
        6.l'assistant cherche mais ne trouve pas. C'est normal (notre produit est �sp�cial�). L'assistant veux aller sur Windows Update. s�lection "Non, pas pour cette fois" et cliquer "Suivant"
        7.� la question "Quelle t�che voulez-vous que l'assistant ex�cute?" R�pondez "Installer � partir d'une liste ou d'un emplacement sp�cifi� (utilisateurs exp�riment�s)" (oui, vous �tes suffisamment exp�riment�s). Cliquez sur "Suivant"
        8.Apr�s avoir travaill� un peu, l'assistant arrive sur la page "Fin de l'assistant Ajout de nouveau mat�riel d�tect�". Cliquez sur "Terminer"
    pour Linux:
        http://www.cours.polymtl.ca/inf1995/fichiers/ dans les sections � Configuration du port USB pour l'utilisation de la carte m�re sur Fedora � ou � Configuration du port USB pour l'utilisation de la carte m�re sur Ubuntu �

trouver boiteVibrationUSB.exe (dans les environs du readme et ajouter son repertoire au PATH. Pour ce faire, dans Windows 7:
	Windows Start Menu,
       cliquer-droit sur ordinateur/computer, option Propri�t�s (ou properties)	
	Choisir dans le panneau � gauche "Advanced system settings", la fen�tre "System Properties" s'ouvre.
	Choisir l'onglet "Advanced" (propablement d�j� s�lectionn�)
	Cliquez sur le bouton tout en bas nomm� Variables d'environnement ("Environment Variables...")
	dans les variables syst�me ("System variables"), trouver "Path"
	et choisir Modifier ("Edit") pour modifier la variable". Elle sera tr�s longue.
	Il faut ajouter � la fin un point-virgule(";") suivi du chemin qui ressemblera � "C:\Users\name\"
	Cliquez sur Ok, Ok, 
	recommencer le programme
-Ajouter boiteVibrationUSB.exe dans Windows XP:
	poste de travail, right-click : propri�t�s
	La fen�tre "Propri�t�s syst�me" appara�t. Choisir l'onglet "Avanc�"
	En bas, choisir "Variables d'environnement
	Dans Les variables du syst�me, truover "Path" et choisir "Modifier pour modifier la variable". Elle sera tr�s longue.
	Il faut ajouter � la fin un point-virgule(";") suivi du chemin qui ressemblera � "C:\Users\name\"
	Cliquez sur Ok, Ok, 
	recommencer le programme
	
pour executer:
    ouvrir l'interpr�teur de commande windows (run: "cmd")
    aller � l'endroit o� se trouve le programme
    �crire "boite_vibration_usb.exe 500" (pour 500 ms, �crire le nombre d�sir�)
    avoir libusb0.dll ( libusb0_x86.dll renommee) dans le m�me r�pertoire
    avoir un fichier m.txt qui contient le nombre de microseconde (exemple:5000) suivi du caract�re 'm' (pour l'instant)



instructions pour faire marcher/compiler boite_vibration_usb sur windows

dans eclipse
-avoir installer mingw
   1. right-click properties, c/C++ Build, Tool Chain Editor, current toolchain: MinGW GCC
   
   Used tools: GCC Assembler, GCC Archiver, GCC C++ Compiler, GCC C Compiler, MinGW C Linker, MinGW C++ Linker
-joindre lib0_usb.h
   1. right-click properties, C/C++ General, Paths and Symbols, Includes, (languages:) GNU C, add/edit (Include directories)
      pointez vers le repertoire actuel
      
ajouter sources boite_vibration_usb.c et usbcmd.h
      
-joindre libusb0_x86.dll (renomm� libusb0) (trouv�e sur le site: http://www.libusb.org/wiki/libusb-win32 ). La dll d�sir�e est celle trouv�e dans l'archive libusb-win32-bin-1.2.6.0.zip\libusb-win32-bin-1.2.6.0\bin\x86, dans bin/libusb0_x86.dll .
   1. right-click properties, C/C++ General, Paths and Symbols, LibraryPaths, add ou edit
      ajouter le dossier "."
   2. right-click properties, C/C++ General, Paths and Symbols, Libraries, add ou edit
      ajouter usb0
   3. la librairie devrait se trouver � c�t� du fichier ".project", "boite_vibration_usb.c" et "usbcmd.h"
   