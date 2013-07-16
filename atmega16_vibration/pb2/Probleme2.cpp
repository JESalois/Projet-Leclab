/*
 * Nom: Probleme 2 
 * Description: Jeu de vitesse utilisant la DEL
 * Auteurs: Thierry Belair et Frederic Poirier
 */
#define F_CPU 8000000 // 8 MHz 

#include <avr/io.h> 
#include <util/delay.h>
#include <avr/interrupt.h>



//etat initial
//unsigned long delai = 0;

volatile uint8_t minuterieExpiree=0;
volatile uint8_t boutonPoussoir=0; 



void initialisation()
{
  cli();
  DDRA = 0xff; // PORT A est en mode sortie
  DDRB = 0xff; // PORT B est en mode sortie
  DDRC = 0xff; // PORT C est en mode sortie
  DDRD = 0x00; // PORT D est en mode entree
  
  GICR |= _BV(INT0);
  MCUCR |= 0b00000011; // p.68-69 doc ATMEL, on met ISC01 et ISC00 a 11 pour que le rising edge de int0 provoque une interruption. 
  
  TIFR |= 0b00010000;  // p. 115-116 doc ATMEL, on met le bit 4-OCF1A:Timer/counter1 output compare A match flag, on set le compare flag
  
  sei();
  
}



void ROUGE()
{
  PORTC = 0b10;
}


void VERT()
{
  PORTC = 0b01;
}


void ETEINT()
{
  PORTC = 0b00000000;
}



void preparation()
{

}





void AMBRE()
{
 for(unsigned long i=0;i<240;i++) 
 {
   _delay_us(60); 
   VERT();
   _delay_us(150);
   ROUGE();
 }
}





ISR(TIMER1_COMPA_vect)  //celle qui guette que ma minuterie a expiree OCF1A est le flag mis par la fin de ma minuterie
{

minuterieExpiree = 1;

}



ISR(INT0_vect) //verifie les interruptions du boutonPoussoir
{
      boutonPoussoir=1;
}

void partirMinuterie ( uint16_t duree ) //si duree est en ms, en base 10

{
	
      //duree = duree; //*8 000  //avec horloge divisee par 1024 (c/increment), donc duree est donnee en ms et sort en valeur de comparaison avec le compteur
      minuterieExpiree = 0;

      // mode CTC du timer 1 avec horloge divisee par 1024

      // interruption après la duree specifiee

      TCNT1 = 0x00;//initial load = 0 

      OCR1A = duree; //p.109 : j'assume qu'il n'y a pas de prescaling alors 8M/1024 = 7812.5 = 7813, fite amplement dans uint16_t

      TCCR1A = 0b00000000;//'modifier ici' ; //Plein d'options qui ne m'interesse pas, je pense 76 et 54 sont pour des compare avec des valeurs binaires au choix 3 et 2 pour une fonction des deux valeurs binaires au choix
      //les 2 derniers bits a 0 intique partiellement le mode que je veux : CTC
      TCCR1B = 0b00001101;//p.113 doc ATMEL, **prescale clk/1024**avec bits 2:0    //changer bit 3 de 0..7 (nom WGM12) pour marquer un max a la fin de la duree definie en OCR1A
      //les 2 bits WGM13 et WGM12 complementent les 2 derniers bits de TCCR1A : 4 et 3 (de 7..0), pour un total de 0100 = CTC mode avec top OCR1A, et un Flag Set on MAX
      TIMSK = 0b00010000;//5ieme bit gauche 1



      //'modifier ici' ; //0..7 timer interrupt mask bit 4 a un pour OCF1A = met et garde un flag d'interrupt quand 0cR1A est atteint, jusqu'a ce que que l'interruption soit servie

}




//titre      :main()
//description:boucle principale du programme, systeme ferme de changement d'etat
//entrees    :aucune
//sorties    :code d'erreur possible
int main()
{
  initialisation();
  
  
  //for(;;)
  //ROUGE();
  
 ROUGE();
  _delay_loop_2(65535);
  ETEINT();
  _delay_loop_2(65535);
  ROUGE();
  _delay_loop_2(65535);
  ETEINT();
  

  
  partirMinuterie(1000);
  
  
do 
{


  
// attendre qu'une des deux variables soit modifiée
// par une ou l'autre des interruptions.

} 
while ( minuterieExpiree == 0 && boutonPoussoir == 0 ); //probleme minuterie active par le bouton
  
  
// Une interruption s'est produite. arreter toute
// forme d'interruption. Une seule reponse suffit.

cli();



    if(minuterieExpiree == 1 && boutonPoussoir == 0) // l'utilisateur a perdu
      for(;;)
      ROUGE();
    if(minuterieExpiree == 1 && boutonPoussoir == 1) // fort improbable
      for(;;)
      AMBRE();
    if(minuterieExpiree == 0 && boutonPoussoir == 1) // l'utilisateur a gagne
      for(;;)
      VERT();


//for(;;)
 //ROUGE();
  




  
  
  
  
  
  
  
  
  
  /*for(;;)//boucle infinie
  {  
    switch (etat)
    {	
      case PREPARATION:
		ETEINT();//la Del est eteinte
		for(int delai=0;delai<306;delai++)//boucle pour obtenir 10 secondes
		  _delay_loop_2(65535);
		etat=DEPART;//prochain etat
		break;
	      
      case DEPART:
		ROUGE();
		_delay_loop_2(65535);
		ETEINT();
		_delay_loop_2(65535);
		ROUGE();
		_delay_loop_2(65535);
		etat=JEU;//prochain etat
		break;

      case JEU:
		ETEINT();//la Del est eteinte pour l'attente de l'interaction du joueur
		for(delai=0;delai<31;delai++)//boucle pour obtenir 1 seconde
		{
		  if(PIND & 0x04)//si bouton = 1, on va a l'etat GAGNE
		    etat=GAGNE;
		  _delay_loop_2(65535);
		} 
		if(etat ==GAGNE)
		  break;
		else
		  etat=PERDU;//s'il n'a pas pesee sur le bouton, il va a l'etat PERDU
		break;
		
      case GAGNE:
		VERT(); 
		break;
		
      case PERDU:
		ROUGE();
		break;
		
      default: //en cas de bogue, on retourne à l'etat initial
		etat=PREPARATION;
		break;
  
    
    }   //switch
  }//for*/
}//main