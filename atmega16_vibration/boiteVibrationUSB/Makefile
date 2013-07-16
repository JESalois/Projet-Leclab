#
# Makefile simple pour le programme serieViaUSB
# Quelques regles simples.
#
# Jerome Collin
# Fevrier 2007
# 
PROG = boiteVibrationUSB
BINNAME = boiteVibrationUSB

CC = g++
#CC = gcc

CCFLAGS = -DCPLUSPLUS -g -I . -Wall -O3 -Wformat=0

OBJS = boiteVibrationUSB.o
LIBS = -L /usr/lib -l usb -l curses 

$(PROG): $(OBJS)
	$(CC) $(OBJS) $(LIBS) -o $(BINNAME)

.cc.o:
	$(CC) $(CCFLAGS) -c $*.cc

all: $(PROG)
	
clean:
	rm -f $(OBJS) $(PROG) *~

