/*
 * Nom: vibration
 * Copyright (C) 2013 Thierry Bélair,
 *        Jean-Étienne Salois,
 *        Patrick Carama-Duchesne,
 *        Philippe de Sève
 * License http://www.gnu.org/copyleft/gpl.html GNU/GPL
 * Description: Ceci est le programme pour générer un pwm 50% à la demande de USB/UART
 * Version: 1
 */

#define F_CPU 8000000UL
#include <avr/io.h>
#include <util/delay.h>
#include <string.h>
#include <inttypes.h>
#include <stdint.h>

#define VIBRATION_FREQUENCY 250
#define HALF_PERIOD_COUNT 2000

#define FILTER_CLOCK_FREQUENCY VIBRATION_FREQUENCY*100



void startPWM();
void stopPWM();
void initialize();
void wait_ms(uint16_t ms);
void startFilterClock();

//fonction de debugage
void ledGreen();
void ledRed();
void ledOff();
void breakRed();
void breakGreen();
void breakOff();
// void numberToPC(uint16_t number);



//UART
void messageFromPC(char dataToFill[], uint8_t sizeOfDataToFill);
void initializeUART();
unsigned char receiveUART();
void flushUSART();

uint16_t parseNumber(char data[], uint8_t indexOfFirstPotentialDigit, uint8_t sizeOfData);

#define DEL_BIT_PAIR   0
#define DEL_BIT_IMPAIR 1
#define DEL_PORT       PORTA
void ledGreen() {
    DEL_PORT |= _BV(DEL_BIT_IMPAIR);
    DEL_PORT &= ~_BV(DEL_BIT_PAIR);
}

void ledRed() {
    DEL_PORT |= _BV(DEL_BIT_PAIR);
    DEL_PORT &= ~_BV(DEL_BIT_IMPAIR);
}

void wait_ms(uint16_t ms) {
    for ( uint16_t i = 0; i < ms; i++) {
        _delay_ms(1);
    }
}

void ledOff() {
    DEL_PORT &= ~_BV(DEL_BIT_PAIR);
    DEL_PORT &= ~_BV(DEL_BIT_IMPAIR);
}

void breakRed() {
    //ledRed();
    while(true);
}

void breakGreen() {
    //ledGreen();
    while(true);
}

void breakOff() {
    ledOff();
    while(true);
}

void flashRedFromGreen() {
    ledRed();
    wait_ms(500);
    ledOff();
    wait_ms(500);
    ledGreen();
}


void attendre_ms(uint16_t ms) {
    for ( uint16_t i = 0; i < ms; ++i) {
        _delay_ms(1);   
    }
}

void startPWM() {
    
    // manuel doc2466.pdf p.72+
    // 8000000UL   / 1024 = 7812.5 = 128 us de periode = compter 31.25 
    // 8000000UL    / 256 = 31250.0 = 32 us de periode = compter 125 
    //                    -> close enough : divisible par 2? pas vraiment
    //                    -> avantage: atmega8 compteur 8 bits// FCPU 8000000UL / 1 = 125 ns de période
    // 8000000UL     / 64 = 125000  =  8 us de periode = compter 500
    //                    -> divisible par 2: 250, compteur 16 bits
    // 8000000UL      / 8 = 1000000 =  1 us de periode = compter  4000
    // FCPU 8000000UL / 1 = 125 ns de période = compter 32000 pour une période
        // 250 Hz = 4 ms periode    
    
    // p76 : BOTTOM to OCR0 (P/2) cleared, OCR1:2000 us ou 2000 periode d'horloge/8
    //       BOTTOM to OCR0 (P/2) set OCR1 même
    //       repeat
    //       total : 4000 us: 4 ms:  fréquence de 250 Hz : désiré
    
    // f.OC1A = 8 000 000/ ( 2 * 8 * (1+1999) ) = 250
    
    TCNT1 = 0;
    
    //
    //OCR1A = 1999; // 1999 + 1 = 2000 (voir equation p.102)
        // f.OCnA = f.clk_I/O / ( 2 N (1+OCRnA)
    // N = 8
    // HALF_PERIOD_COUNT *2 * N is desired
    OCR1A = HALF_PERIOD_COUNT-1; //see equation on p.102 for explanation on "-1"
    
    
    //WGM13:0 = 4
    
    
    //COM1A1:0 = 0b01 (toggle on compare match)
    TCCR1A &= ~_BV(COM1A1);
    TCCR1A |=  _BV(COM1A0);
    
    //pas touche au FOC1 : pas besoin, je crois
    
    
    
    //DDR_OC1A=1
    
    // p. 112 CTC with OCR1A mode 0b0100 ou 0x4
    TCCR1B &= ~_BV(WGM13); // WGM13 = 0
    TCCR1B |=  _BV(WGM12); // WGM12 = 1
    TCCR1A &= ~_BV(WGM11); // WGM11 = 0
    TCCR1A &= ~_BV(WGM10); // WGM10 = 0
    
    //horloge /8 CS1 : 0b010 ou o2 ou 0x2
    TCCR1B &= ~_BV(CS12); // CS12 = 0
    TCCR1B |=  _BV(CS11); // CS11 = 1
    TCCR1B &= ~_BV(CS10); // CS10 = 0
    
    //TIMSK1 no. No interrupt
    
    DDRD = 0xFF;
}

void startFilterClock() {
    
    // manuel doc2466.pdf p.72+
    
    // FILTER_CLOCK_FREQUENCY is 25000 ( 2013-03-23 )
    // period is 4.0 x 10^-5 = 0,004 x 10^-2 = 0,000 04 = 40 usec

    // 8000000UL     / 64 = 125000  =  8 us de periode = compter 5
    //                    -> divisible par 2: 250, compteur 16 bits
    // 8000000UL      / 8 = 1000000 =  1 us de periode = compter  40
    // FCPU 8000000UL / 1 = 125 ns de période = compter 320 <--
    
    // f.OC1A = 8 000 000/ ( 2 * 1 * (1+OCR0) ) = 25 000
    //  25 000 * ( 2 (1+OCR0) ) = 8 000 000
    //  50 000 ( 1+OCR0) = 8 000 000
    //  1+OCR0 = 160
    // OCR0 = 159
    //OCR0 = 159; // 1999 + 1 = 2000 (voir commentaire ci-haut)
    OCR0 = ( F_CPU / (FILTER_CLOCK_FREQUENCY * 2 )) - 1; // (voir commentaire ci-haut)

    TCNT0 = 0;
    
    //COM01:0 = 0b01 (toggle OC0 on compare match)
    TCCR0 &= ~_BV(COM01);
    TCCR0 |=  _BV(COM00);
    
    //pas touche au FOC1 : pas besoin, je crois
    
    // p. 83 CTC with OCR0 mode 0b10 ou 0x2
    TCCR0 |=  _BV(WGM01); // WGM11 = 1
    TCCR0 &= ~_BV(WGM00); // WGM10 = 0
    
    //horloge /1 CS0: 0b010 ou o2 ou 0x2
    TCCR0 &= ~_BV(CS12); // CS12 = 0
    TCCR0 &= ~_BV(CS11); // CS11 = 0
    TCCR0 |=  _BV(CS10); // CS10 = 1
    
    //TIMSK1 no. No interrupt
    DDRD = 0xFF;
}

void initialize() {

    DDRA = 0xff; // PORT A est en mode sortie
    DDRB = 0xff; // PORT B est en mode sortie
    DDRC = 0xff; // PORT C est en mode sortie
    DDRD = 0xff; // PORT D est en mode sortie

    initializeUART();
    startFilterClock();

}

void stopPWM() {
    //leaving all config on except clock source
    // CS1: 0b000 ou o0 ou 0x0void messageFromPC(char dataToFill[], uint8_t sizeOfDataToFill);
    TCCR1B &= ~_BV(CS12); // CS12 = 0
    TCCR1B &= ~_BV(CS11); // CS11 = 0
    TCCR1B &= ~_BV(CS10); // CS10 = 0
    
    //force output to 0
    TCCR1A  &= ~(_BV(COM1A1) | _BV(COM1A0) );
    //OCR1A = 
    //FOC1A |= _BV(FOC1A);

    
    //flashRedFromGreen();
    PORTD = 0;
}

void initializeUART() {
    // 2400 bauds. Nous vous donnons la valeur des deu
    // premier registres pour vous éviter des complications
    UBRRH = 0;
    UBRRL = 0xCF;

    // permettre la reception et la transmission par le UART0
    UCSRA |= _BV(UDRE);
    
    UCSRB = _BV(RXEN);// | _BV(TXEN) ;

    //writing to UCSRC
    // character size : 8 UCSZ: 0b011, o3 ou 0x3 pour 8 bits
    UCSRC |= _BV(URSEL) | _BV(UCSZ1) | _BV(UCSZ0); // 1 permits writing to UCSRC. writing a byte by itself seems to break it
    // Format des trames: 8 bits, 1 stop bits, none parity
    //none parity (p.166)
    UCSRC &= ~_BV(UPM1);
    UCSRC &= ~_BV(UPM0);
    void messageFromPC(char dataToFill[], uint8_t sizeOfDataToFill);
    // one stop bit (p.166)
    UCSRC &= ~_BV(USBS);// USBS = 0
    
}

void messageFromPC(char dataToFill[], uint8_t sizeOfDataToFill) {
    uint8_t charReceived;
    for (charReceived = 0; charReceived < sizeOfDataToFill; charReceived++) {
        dataToFill[charReceived] = receiveUART();
    }
}

unsigned char receiveUART() {
    unsigned char result;
    while ( !( UCSRA & (1 << RXC) ) );
    result = UDR;

    return result;
}

//interpreter chiffre
//délai court: 250ms
//délai long : 500ms
// parseNumber ends when number has been encountered and ended
// or when no number is found,
// in which case, 0 is returned
//      @param data[in]: data (max 65655) that contains number
//      @param sizeOfData[in]: size of the data in input. 
//                             (size-1) is a valid position in array
//      @param success[out] : pointer to the boolean to set.
//                            If NULL : is ignored. true if number is found
//      @return [out] : number found in the string
uint16_t parseNumber(char data[], uint8_t sizeOfData, uint8_t* success) {
    uint8_t position = 0;
    uint16_t number = 0;
    uint8_t numberIsFound = false;
    
    
    while (position < sizeOfData) {

        if ( ( data[position] <= '9') &&
             ( data[position] >= '0') ) {
            numberIsFound = true;
            number = number * 10;
            number = number + ( data[position] - '0');
        }
        else {
            if (numberIsFound) {
                //number was found
                // and has ended
                if (success != NULL) {
                    *success = true;
                }
                return number;
            }
        }
        position++;
    }
    
    if (!numberIsFound) {
        //number is not found and arrived at end of data
        if (success != NULL) {
            *success = false;
        }
    }
    if (success != NULL) {
        *success = true;
    }
    return number;
}


// void numberToPC(uint16_t number) {
//     
//     //unsigned char data = '\n'; //data[] = "00"; //only first one used
//     char data ='p';
//     ////transmitUART(data);////messageToPC(data,1);
//     
//     uint16_t comparator = 1; //comparator is a decimal 10^(last digit of number)
//     
//     while ( comparator <= (number/10) ) {
//         char ds[11] = "comparator";
//         //messageToPC(ds,10);
//         comparator = comparator * 10;
//     }
//     
//     
//     
//     while(number > 9 || comparator > 1) {
//         data = '0' + ( number / comparator ) ;
//         //transmitUART(data); ////messageToPC(data,1);
//         number = number % comparator;
//         comparator = comparator / 10;
//     }
//     
//     data = '0' + ( number % 10 );
//     //transmitUART(data); ////messageToPC(data,1);
//     char endline[2] = "\n";
//     //messageToPC(endline,2);
//     return;
// }

uint16_t numberMillisecondsFromPC(uint8_t* success) {
    // at maximum "65655m"
    char numberString[] = "mmmmmm";//maximum 65655 ms
    
    uint8_t position =0;
    char charReceived = '0';
    uint16_t numberMilliseconds = 0;
    
    while( (charReceived >= '0' && charReceived <= '9') && position < 6) {
        charReceived = receiveUART();
        numberString[position] = charReceived;
        position += 1;
    }
    
    flushUSART();
    
    
    if ( position > 6 ) {
        if (success != NULL) {
            *success = false;
        }
        return 0;
    }
    else {
        
        numberMilliseconds = parseNumber(numberString, 6, success);
        
        return numberMilliseconds;
    }
}


void flushUSART() {
    unsigned char dummy;
    while ( UCSRA & (1<<RXC) ) {
        dummy = UDR;
    }
}


int main()
{
    initialize();
    uint8_t receivedValidNumber = false;
    uint16_t nbMilliseconds = 0;
    
    //code de couleur
    // vert: ok, prêt
    // rouge: en train de vibrer
    // fermée: dernier nombre n'est pas valide
    ledGreen();

    while(true) {
        nbMilliseconds = numberMillisecondsFromPC(&receivedValidNumber);
        if (receivedValidNumber) {
            startPWM();
            ledRed();
            wait_ms(nbMilliseconds);
            stopPWM();
            ledGreen();
        }
        else {
            ledOff();
        }
    }
  
    // cette section ne devrait jamais être atteinte
    while(true); // ne jamais quitter le main
    return 0; 
}


