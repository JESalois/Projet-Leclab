
/*
    serieViaUSB: programme qui peut etre considere comme un terminal
                 extremement primitif et qui vise l'echange de quelques
                 octets avec une carte a microcontroleur par port RS232
                 Unique comminication possible : 2400 baud, 8 bits,
                 aucun bit de parite et un seul d'arret.

    Matthew Khouzam
    Jerome Collin
    Michaël Ferris
    Mathieu Marengère-Gosselin
    Fevrier 2007
    Modifications, mars 2011

*/

#include <stdio.h>
//#include <limits.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <string.h> //memset
//#include <limits>
#include <time.h>
#include <usb.h>
//#include "lusb0_usb.h"        /* acces a libusb, voir http://libusb.sourceforge.net/ */
#include "usbcmd.h"
//#include <ctype.h>

//enum ModesAffichage{BYTEMODE, HEXMODE, DECMODE, BINMODE};
#define BYTEMODE 1
#define HEXMODE  2
#define DECMODE  3
#define BINMODE  4

#define GRANDEUR_TAMPON 8

//unsigned int nbMs = 0;
//int utiliseFichier = false;
int utiliseFichier = 0;

//// pour la conversion char-bin.
//// Tiré de http://cboard.cprogramming.com/c-programming/42817-convert-char-binary.html
//char* charToBin ( unsigned char c )
//{
//    static char bin[CHAR_BIT + 1] = {0};
//    int i;
//
//    for ( i = CHAR_BIT - 1; i >= 0; i-- )
//    {
//        bin[i] = (c % 2) + '0';
//        c /= 2;
//    }
//
//    return bin;
//}

// pour l'interface USB vers la carte
usb_dev_handle *gestionUSB;

void afficherAide () {
   fprintf (stderr, "\n" );
   fprintf (stderr, "usage: boiteVibrationUSB nombreMs\n");
   fprintf (stderr, "\n" );
   fprintf (stderr, "description : programme permettant de recevoir et\n" );
   fprintf (stderr, "              d'envoyer des octets de facon serielle a\n" );
   fprintf (stderr, "              la boite de vibration (l'atmega16)\n" );
   fprintf (stderr, "               Aucun protocole\n" );
   fprintf (stderr, "              special ne controle la signification des\n");
   fprintf (stderr, "              octets.\n" );
   fprintf (stderr, "\n" );
   fprintf (stderr, "nombreMs: nombre de millisecondes pour la duree de la vibration\n" );
   fprintf (stderr, "              max: 65535 (soit 65,5 secondes)\n" );
   fprintf (stderr, "              minimum 0, aucun nombre negatif.\n" );
   fprintf (stderr, "\n" );
   fflush(0);
   exit (-1);
}

/* Fonctions pour gerer le USB */

/* fonction assez standard pour l'ouverture du port USB */
int usbOuvrir()
{
  struct usb_bus    *bus;
  struct usb_device *dev = 0;

  /* facon de faire standard pour trouver le device USB et l'ouvrir... */
  usb_init();
  usb_find_busses();
  usb_find_devices();
  for(bus=usb_busses; bus; bus=bus->next){
    for(dev=bus->devices; dev; dev=dev->next){
      if(dev->descriptor.idVendor == USBDEV_VENDOR &&
         dev->descriptor.idProduct == USBDEV_PRODUCT)
        break;
    }
    if(dev)
      break;
  }
  if( ! dev ) {
    fprintf (stderr, "Erreur: incapable de trouver le peripherique USB ");
    fprintf (stderr, "(vendor=0x%x product=0x%x)\n", USBDEV_VENDOR, USBDEV_PRODUCT);
    exit (-1);
  }

  // la documentation de libUSB donne des exemples de cette facon de faire
  gestionUSB = usb_open(dev);
  if ( ! gestionUSB ) {
    fprintf (stderr, "Erreur: incapable d'ouvrir le port USB vers le ");
    fprintf (stderr, "peripherique: %s\n", usb_strerror());
    usb_close (gestionUSB);
    exit (-1);
  }

  return 0;
}

int usbAjustementSerie (int baud, int bits, int parity)
{
  int nOctets;
  unsigned char cmd[4];
  char msg[4] = {0, 0, 0, 0};
  // le firmware attend ces parametres, et dans cet ordre
  cmd[0] = baud;
  cmd[1] = bits;
  cmd[2] = parity;
  cmd[3] = 0;

  // USBASP_FUNC_SETSERIOS ajuste les parametres USB (voir firmware)
  nOctets = usb_control_msg (gestionUSB,
             USB_TYPE_VENDOR | USB_RECIP_DEVICE | (1 << 7),
             USBASP_FUNC_SETSERIOS,
             (cmd[1] << 8) | cmd[0], (cmd[3] << 8) | cmd[2],
             msg, 4, 5000);

  if ( nOctets < 0 ) {
     fprintf(stderr, "Erreur: probleme de transmission USB: %s\n", usb_strerror());
     usb_close (gestionUSB);
     exit (-1);
  }

  // la configuration se confirme par un echo des parametres
  // il faut donc verifier que l'on a bien recu ce qu'on attend...
  if ( msg[0] == cmd[0] && msg[1] == cmd[1] &&
       msg[2] == cmd[2] && msg[3] == msg[3] ) {
     return 1;
  }

  return 0;
}

//http://stackoverflow.com/questions/1489830/efficient-way-to-determine-number-of-digits-in-an-integer
unsigned int numDigits(unsigned int number) {
   unsigned int digits = 0;
   unsigned int computingNumber = number;
   if ( computingNumber == 0) {
      digits = 1;
   }
   else {
      while (computingNumber) {
         computingNumber /= 10;
         digits++;
      }
   }
   //fprintf(stdout, "num: %d, numDigits found: %d", number, digits);
   return digits;
}

unsigned int getNbMs ( int argc, char *argv[] ) {

   // analyze de la ligne de commande
   if (argc != 2) {
      afficherAide();
      return 0;
   }

   if ( argv[1][0] == '0' ) {
      return 0;
      //fprintf(stdout, "0 trouve");
   }
   else { 
      long nbMsCandidate = 0; // garantied 32 bits
      nbMsCandidate = strtol( argv[1], NULL,0); //base to be determined by function
      
      if (nbMsCandidate == 0) { // cas 0 est déjà couvert
         afficherAide();
         return -1;
      }
      
      if ( nbMsCandidate < 0 || nbMsCandidate > 65535) {
         afficherAide();
         return -1;
      }
      fprintf(stdout, "number from analysis:%d\n", nbMsCandidate);
      return (int) nbMsCandidate; // nbMs is int, so garantied 16 bits.
   }
   
   
   
   //fprintf(stdout, "valid number: %d\n", nbMs);
   //nBytes = buf.st_size;
   
   
   return 1; // succes
}

int main ( int argc, char *argv[] ) {

   // commencer par analyser les options sur la ligne de commande...
   unsigned int nbMs = getNbMs( argc, argv );
   unsigned int nbDigitsInNbMs = numDigits(nbMs);
   char nbMsCharacters[6]; // 6 the m in >65535m<
   
   //return -1;
   sprintf(nbMsCharacters, "%d",nbMs);
   fprintf(stdout, "%s",nbMsCharacters); 
   
   
   /* OK, ouvrir tout ce qui est USB... */
   usbOuvrir();
   if (!  usbAjustementSerie(USBASP_MODE_SETBAUD2400,
                           USBASP_MODE_UART8BIT, USBASP_MODE_PARITYN ) ) {
      fprintf(stderr, "Erreur: incapable d'ajuster la communication serie\n");
      exit (-1);
   }
   fflush(0);
      
   
   unsigned char tampon[GRANDEUR_TAMPON];
   
   unsigned int nBytes = nbDigitsInNbMs + 1; // +1 is for character 'm'
   
   // while ( i < nBytes ) {
   // mettre 0xFF dans tout le tampon
   memset (tampon, 0xFF, GRANDEUR_TAMPON);
   
   // le PC envoie les donnees vers la carte
  
   // remplir le tampon - fpFichier ne peut etre stdin
   unsigned int indexCaractere = 0;
   if ( nBytes > GRANDEUR_TAMPON ) {
      fprintf(stderr, "%d bytes is too big");
   }
   while (indexCaractere < nBytes) {
     tampon[indexCaractere+1] = nbMsCharacters[indexCaractere];
     indexCaractere++;
   }
   tampon[0] = nBytes;

   //http://libusb.sourceforge.net/doc/function.usbcontrolmsg.html
  
   // on envoie vers l'USB  0 == envoie
   int rtn = usb_control_msg (gestionUSB, //handle
             USB_TYPE_VENDOR | USB_RECIP_DEVICE | (0 << 7), // request type
             USBASP_FUNC_WRITESER, //request
             0, //value
             0, //index
             (char *)tampon, //bytes
             nBytes+1, //size
             5000 //timeout
             );

  // echo a l'ecran, pas strictement necessaire mais interessant
  

   // thierry: normal behavior should not use stderr casually
   //unsigned int k = 0;
   //for ( k = 0; k < nBytes; k++ ) {
   //   fputc (tampon[k], stderr);
   //}

   // il faut que la carte ecrive les octets en memoire
   // et via l'interface serie qui est lente.  Donner une chance
   // a la carte de realiser ses operations en lui laissant du temps.
   // ce petit delai devrait etre suffisant pour qu'elle 
   // "n'echappe pas des octets" en cours de route...

   // thierry: nanosleep n'existe pas sur windows, alors
   // le but etant d'attendre 50 ms
   // usleep(50) devrait faire l'affaire

   // thierry : finalement pas besoin, parce que pas plusieurs buffers de 8 bytes.
   //usleep(50);
   ////nanosleep (&tempSpec, NULL);

   if ( rtn < 0 ) {
      fprintf (stderr, "Erreur: problem de tansmission USB:" );
      fprintf (stderr, " %s\n", usb_strerror());
      usb_close (gestionUSB);
      exit(-1);
   }

   fflush(0);

   // rapport indiquant que la transmission est terminee
   fprintf (stdout, "\n--------------------------------------------\n");
   fprintf (stdout, "serieViaUSB : %d octets ont ete transmis ", nBytes );
   fprintf (stdout, "ou recus\n" );
   fflush(0);

   usb_close (gestionUSB);
   return 0;
}



