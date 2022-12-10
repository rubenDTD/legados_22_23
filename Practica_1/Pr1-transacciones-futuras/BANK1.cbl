       IDENTIFICATION DIVISION.
       PROGRAM-ID. BANK1.

       ENVIRONMENT DIVISION.
       CONFIGURATION SECTION.
       SPECIAL-NAMES.
           CRT STATUS IS KEYBOARD-STATUS.

       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT TARJETAS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS TNUM
           FILE STATUS IS FST.

           SELECT INTENTOS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS INUM
           FILE STATUS IS FSI.

           SELECT TRANSFERENCIAS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS TRANSF-NUM
           FILE STATUS IS FSTM.

           SELECT F-MOVIMIENTOS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS MOV-NUM
           FILE STATUS IS FSM.


       DATA DIVISION.
       FILE SECTION.
       FD TARJETAS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "tarjetas.ubd".
       01 TAJETAREG.
           02 TNUM      PIC 9(16).
           02 TPIN      PIC  9(4).

       FD INTENTOS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "intentos.ubd".
       01 INTENTOSREG.
           02 INUM      PIC 9(16).
           02 IINTENTOS PIC 9(1).


       FD TRANSFERENCIAS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "transf.txt".
       01 TRANSF-REG.
           02 TRANSF-NUM           PIC   9(35).
           02 TARJETA-ORIGEN       PIC   9(16).
           02 TARJETA-DESTINO      PIC   9(16).
           02 TRANSF-IMPORTE-ENT   PIC   S9(7).
           02 TRANSF-IMPORTE-DEC   PIC    9(2).
           02 TRANSF-DIA           PIC    9(2).
           02 DIA-ORDEN            PIC    9(2).
           02 TRANSF-MES           PIC    9(2).
           02 TRANSF-ANO           PIC    9(4).
           02 ULTIMA-MENSUALIDAD   PIC    9(2).
           02 ULTIMO-ANO           PIC    9(4).

       FD F-MOVIMIENTOS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "movimientos.ubd".
       01 MOVIMIENTO-REG.
           02 MOV-NUM              PIC  9(35).
           02 MOV-TARJETA          PIC  9(16).
           02 MOV-ANO              PIC   9(4).
           02 MOV-MES              PIC   9(2).
           02 MOV-DIA              PIC   9(2).
           02 MOV-HOR              PIC   9(2).
           02 MOV-MIN              PIC   9(2).
           02 MOV-SEG              PIC   9(2).
           02 MOV-IMPORTE-ENT      PIC  S9(7).
           02 MOV-IMPORTE-DEC      PIC   9(2).
           02 MOV-CONCEPTO         PIC  X(35).
           02 MOV-SALDOPOS-ENT     PIC  S9(9).
           02 MOV-SALDOPOS-DEC     PIC   9(2).


       WORKING-STORAGE SECTION.
       77 FST                      PIC  X(2).
       77 FSI                      PIC  X(2).
       77 FSTM                     PIC  X(2).
       77 FSM                      PIC  X(2).

       78 BLACK   VALUE 0.
       78 BLUE    VALUE 1.
       78 GREEN   VALUE 2.
       78 CYAN    VALUE 3.
       78 RED     VALUE 4.
       78 MAGENTA VALUE 5.
       78 YELLOW  VALUE 6.
       78 WHITE   VALUE 7.

       01 CAMPOS-FECHA.
           05 FECHA.
               10 ANO              PIC  9(4).
               10 MES              PIC  9(2).
               10 DIA              PIC  9(2).
           05 HORA.
               10 HORAS            PIC  9(2).
               10 MINUTOS          PIC  9(2).
               10 SEGUNDOS         PIC  9(2).
               10 MILISEGUNDOS     PIC  9(2).
           05 DIF-GMT              PIC S9(4).

       01 KEYBOARD-STATUS           PIC 9(4).
           88 ENTER-PRESSED          VALUE 0.
           88 PGUP-PRESSED        VALUE 2001.
           88 PGDN-PRESSED        VALUE 2002.
           88 UP-ARROW-PRESSED    VALUE 2003.
           88 DOWN-ARROW-PRESSED  VALUE 2004.
           88 ESC-PRESSED         VALUE 2005.

       77 PRESSED-KEY              PIC  9(4).
       77 PIN-INTRODUCIDO          PIC  9(4).
       77 CHOICE                   PIC  9(1).

       77 LAST-MOV-NUM             PIC  9(35).
       77 TIPO-TRANSF              PIC  9(1).
       77 MES-VAR                  PIC  9(2).

       77 LAST-USER-ORD-MOV-NUM    PIC  9(35).
       77 LAST-USER-DST-MOV-NUM    PIC  9(35).
       77 CENT-SALDO-ORD-USER      PIC  S9(9).
       77 CENT-SALDO-DST-USER      PIC  S9(9).
       77 CENT-IMPOR-USER          PIC  S9(9).

       77 NUM-MENSUALIDADES        PIC   9(4).
       77 BUCLE-MES                PIC   9(4).
       77 BUCLE-ANO                PIC   9(4).

       SCREEN SECTION.
       01 BLANK-SCREEN.
           05 FILLER LINE 1 BLANK SCREEN BACKGROUND-COLOR BLACK.

       01 DATA-ACCEPT.
           05 TARJETA-ACCEPT BLANK ZERO AUTO LINE 08 COL 50
               PIC 9(16) USING TNUM.
           05 PIN-ACCEPT BLANK ZERO SECURE LINE 09 COL 50
               PIC 9(4) USING PIN-INTRODUCIDO.



       PROCEDURE DIVISION.

       IMPRIMIR-CABECERA.

           SET ENVIRONMENT 'COB_SCREEN_EXCEPTIONS' TO 'Y'
           SET ENVIRONMENT 'COB_SCREEN_ESC'        TO 'Y'

           DISPLAY BLANK-SCREEN.

           DISPLAY "Cajero Automatico UnizarBank" LINE 2 COL 26
               WITH FOREGROUND-COLOR IS BLUE.

           MOVE FUNCTION CURRENT-DATE TO CAMPOS-FECHA.

           DISPLAY DIA LINE 4 COL 32.
           DISPLAY "-" LINE 4 COL 34.
           DISPLAY MES LINE 4 COL 35.
           DISPLAY "-" LINE 4 COL 37.
           DISPLAY ANO LINE 4 COL 38.
           DISPLAY HORAS LINE 4 COL 44.
           DISPLAY ":" LINE 4 COL 46.
           DISPLAY MINUTOS LINE 4 COL 47.


       P1.
           DISPLAY "Bienvenido a UnizarBank" LINE 8 COL 28.
           DISPLAY "Por favor, introduzca la tarjeta para operar"
               LINE 10 COL 18.

           DISPLAY "Enter - Aceptar" LINE 24 COL 33.

       P1-ENTER.
           CLOSE TRANSFERENCIAS.
           OPEN I-O TRANSFERENCIAS.
           IF FSTM <> 00 THEN
               PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA
               DISPLAY FSTM LINE 9 COL 25
                   WITH FOREGROUND-COLOR IS BLACK
                        BACKGROUND-COLOR IS RED
               DISPLAY "Error abrir transferencias" LINE 11 COL 32
                   WITH FOREGROUND-COLOR IS BLACK
                        BACKGROUND-COLOR IS RED
               DISPLAY "Enter - Aceptar" LINE 24 COL 33
               GO TO EXIT-ENTER
           END-IF.
           MOVE 0 TO LAST-MOV-NUM
           ACCEPT OMITTED ON EXCEPTION
           IF ENTER-PRESSED
               *>GO TO P2
               GO TO LEER-TRANSF
           ELSE
               GO TO P1-ENTER.


       LEER-TRANSF.
           *>CLOSE F-MOVIMIENTOS.
           READ TRANSFERENCIAS NEXT RECORD AT END GO TO P2.
               *>DISPLAY TRANSF-DIA LINE 28 COL 20.
               *>DISPLAY DIA-ORDEN LINE 28 COL 30.
               *>DISPLAY TRANSF-MES LINE 28 COL 34.
               *>DISPLAY ULTIMA-MENSUALIDAD LINE 29 COL 28.
               IF (TRANSF-MES <> 00) THEN
                  IF ((ANO > TRANSF-ANO OR
                      (ANO = TRANSF-ANO AND MES > TRANSF-MES) OR
                      (ANO = TRANSF-ANO AND MES > TRANSF-MES
                       AND DIA > TRANSF-DIA))) THEN
                           MOVE 1 TO TIPO-TRANSF
                           GO TO PCONSULTA-SALDO
               ELSE
                  COMPUTE MES-VAR = ULTIMA-MENSUALIDAD + 1
                   DISPLAY TARJETA-ORIGEN LINE 30 COL 28.
                  IF((DIA >= TRANSF-DIA AND DIA-ORDEN <= TRANSF-DIA
                      AND ULTIMA-MENSUALIDAD = 00)
                      OR (MES > ULTIMA-MENSUALIDAD AND
                      (DIA > TRANSF-DIA OR MES > MES-VAR) AND
                      ULTIMA-MENSUALIDAD <> 00)) THEN
                           MOVE 2 TO TIPO-TRANSF
                           GO TO PCONSULTA-SALDO
               END-IF.
               GO TO LEER-TRANSF.



       GUARDAR-TRANSF-1.

           MOVE LAST-USER-ORD-MOV-NUM TO MOV-NUM.
           OPEN I-O F-MOVIMIENTOS.
           IF FSM <> 00
               GO TO PSYS-ERR.
           READ F-MOVIMIENTOS INVALID KEY GO PSYS-ERR.

           COMPUTE CENT-SALDO-ORD-USER = (MOV-SALDOPOS-ENT * 100)
                                       + MOV-SALDOPOS-DEC.
           COMPUTE CENT-IMPOR-USER = (TRANSF-IMPORTE-ENT * 100)
                                   + TRANSF-IMPORTE-DEC
           SUBTRACT CENT-IMPOR-USER FROM CENT-SALDO-ORD-USER.
           COMPUTE MOV-SALDOPOS-ENT = (CENT-SALDO-ORD-USER / 100).
           MOVE FUNCTION MOD(CENT-SALDO-ORD-USER, 100)
               TO MOV-SALDOPOS-DEC.


           ADD 1 TO LAST-MOV-NUM.

           MOVE LAST-MOV-NUM TO MOV-NUM.
           MOVE TARJETA-ORIGEN TO MOV-TARJETA.
           MOVE TRANSF-DIA TO MOV-DIA.
           MOVE TRANSF-MES TO MOV-MES.
           MOVE TRANSF-ANO TO MOV-ANO.
           MOVE 0 TO MOV-HOR.
           MOVE 0 TO MOV-MIN.
           MOVE 0 TO MOV-SEG.
           MOVE "Transferimos." TO MOV-CONCEPTO.

           MULTIPLY -1 BY TRANSF-IMPORTE-ENT.
           MOVE TRANSF-IMPORTE-ENT TO MOV-IMPORTE-ENT.
           MULTIPLY -1 BY TRANSF-IMPORTE-ENT.
           MOVE TRANSF-IMPORTE-DEC TO MOV-IMPORTE-DEC.

           WRITE MOVIMIENTO-REG INVALID KEY GO TO PSYS-ERR.

           CLOSE F-MOVIMIENTOS.
           MOVE LAST-USER-DST-MOV-NUM TO MOV-NUM.
           OPEN I-O F-MOVIMIENTOS.
           IF FSM <> 00
               GO TO PSYS-ERR.
           READ F-MOVIMIENTOS INVALID KEY GO PSYS-ERR.
           COMPUTE CENT-SALDO-DST-USER = (MOV-SALDOPOS-ENT * 100)
                                       + MOV-SALDOPOS-DEC.
           ADD CENT-IMPOR-USER TO CENT-SALDO-DST-USER.
           COMPUTE MOV-SALDOPOS-ENT = (CENT-SALDO-DST-USER / 100).
           MOVE FUNCTION MOD(CENT-SALDO-DST-USER, 100)
               TO MOV-SALDOPOS-DEC.

           ADD 1 TO LAST-MOV-NUM.

           MOVE LAST-MOV-NUM TO MOV-NUM.
           MOVE TARJETA-DESTINO TO MOV-TARJETA.
           MOVE TRANSF-IMPORTE-ENT TO MOV-IMPORTE-ENT.
           MOVE TRANSF-IMPORTE-DEC TO MOV-IMPORTE-DEC.
           MOVE TRANSF-DIA TO MOV-DIA.
           MOVE TRANSF-MES TO MOV-MES.
           MOVE TRANSF-ANO TO MOV-ANO.
           MOVE 0 TO MOV-HOR.
           MOVE 0 TO MOV-MIN.
           MOVE 0 TO MOV-SEG.
           MOVE "Nos transfieren." TO MOV-CONCEPTO.

           WRITE MOVIMIENTO-REG INVALID KEY GO TO PSYS-ERR.
           CLOSE F-MOVIMIENTOS.
           GO TO LEER-TRANSF.

       GUARDAR-TRANSF-2.

           IF (ULTIMA-MENSUALIDAD = 00) THEN


               MOVE LAST-USER-ORD-MOV-NUM TO MOV-NUM
               OPEN I-O F-MOVIMIENTOS
               *>IF FSM <> 00
                   *>GO TO PSYS-ERR.
               READ F-MOVIMIENTOS INVALID KEY GO PSYS-ERR

               COMPUTE CENT-SALDO-ORD-USER = (MOV-SALDOPOS-ENT * 100)
                                         + MOV-SALDOPOS-DEC

               COMPUTE CENT-IMPOR-USER = (TRANSF-IMPORTE-ENT * 100)
                                     + TRANSF-IMPORTE-DEC

               SUBTRACT CENT-IMPOR-USER FROM CENT-SALDO-ORD-USER

               COMPUTE MOV-SALDOPOS-ENT = (CENT-SALDO-ORD-USER / 100)
               MOVE FUNCTION MOD(CENT-SALDO-ORD-USER, 100)
                   TO MOV-SALDOPOS-DEC

               ADD 1 TO LAST-MOV-NUM

               MOVE LAST-MOV-NUM TO MOV-NUM
               MOVE TARJETA-ORIGEN TO MOV-TARJETA
               MOVE TRANSF-DIA TO MOV-DIA
               MOVE TRANSF-MES TO MOV-MES
               MOVE TRANSF-ANO TO MOV-ANO
               MOVE 0 TO MOV-HOR
               MOVE 0 TO MOV-MIN
               MOVE 0 TO MOV-SEG
               MOVE "Transferimos." TO MOV-CONCEPTO


               MULTIPLY -1 BY TRANSF-IMPORTE-ENT
               MOVE TRANSF-IMPORTE-ENT TO MOV-IMPORTE-ENT
               MULTIPLY -1 BY TRANSF-IMPORTE-ENT
               MOVE TRANSF-IMPORTE-DEC TO MOV-IMPORTE-DEC

               WRITE MOVIMIENTO-REG INVALID KEY GO TO PSYS-ERR.

               CLOSE F-MOVIMIENTOS.
               MOVE LAST-USER-DST-MOV-NUM TO MOV-NUM
               OPEN I-O F-MOVIMIENTOS.
               *>IF FSM <> 00
                   *>GO TO PSYS-ERR.
               READ F-MOVIMIENTOS INVALID KEY GO PSYS-ERR

               COMPUTE CENT-SALDO-DST-USER = (MOV-SALDOPOS-ENT * 100)
                                         + MOV-SALDOPOS-DEC
               ADD CENT-IMPOR-USER TO CENT-SALDO-DST-USER
               COMPUTE MOV-SALDOPOS-ENT = (CENT-SALDO-DST-USER / 100)
               MOVE FUNCTION MOD(CENT-SALDO-DST-USER, 100)
                   TO MOV-SALDOPOS-DEC.

               ADD 1 TO LAST-MOV-NUM
               MOVE LAST-MOV-NUM TO MOV-NUM
               MOVE TARJETA-DESTINO TO MOV-TARJETA

               MOVE TRANSF-IMPORTE-ENT TO MOV-IMPORTE-ENT
               MOVE TRANSF-IMPORTE-DEC TO MOV-IMPORTE-DEC
               MOVE TRANSF-DIA TO MOV-DIA
               MOVE TRANSF-MES TO MOV-MES
               MOVE TRANSF-ANO TO MOV-ANO
               MOVE 0 TO MOV-HOR
               MOVE 0 TO MOV-MIN
               MOVE 0 TO MOV-SEG
               MOVE "Nos transfieren." TO MOV-CONCEPTO


               WRITE MOVIMIENTO-REG INVALID KEY GO TO PSYS-ERR
               CLOSE F-MOVIMIENTOS


               IF ((TRANSF-MES > ULTIMA-MENSUALIDAD
                   AND TRANSF-ANO = ULTIMO-ANO)
                   OR TRANSF-ANO > ULTIMO-ANO)
                       MOVE TRANSF-MES TO ULTIMA-MENSUALIDAD

               MOVE FUNCTION MAX(TRANSF-ANO ULTIMO-ANO) TO ULTIMO-ANO

               REWRITE TRANSF-REG

               GO TO LEER-TRANSF

           ELSE

               COMPUTE NUM-MENSUALIDADES = ANO - ULTIMO-ANO
               MULTIPLY 12 BY NUM-MENSUALIDADES
               IF (MES > ULTIMA-MENSUALIDAD)
                   ADD MES TO NUM-MENSUALIDADES
                   SUBTRACT ULTIMA-MENSUALIDAD FROM NUM-MENSUALIDADES
               ELSE
                   ADD ULTIMA-MENSUALIDAD TO NUM-MENSUALIDADES
                   SUBTRACT MES FROM NUM-MENSUALIDADES

               IF (DIA < TRANSF-DIA) THEN
                   SUBTRACT 1 FROM NUM-MENSUALIDADES

               MOVE ULTIMA-MENSUALIDAD TO BUCLE-MES
               ADD 1 TO BUCLE-MES
               MOVE ULTIMO-ANO TO BUCLE-ANO
               ADD 1 TO BUCLE-ANO

               MOVE LAST-USER-ORD-MOV-NUM TO MOV-NUM
               OPEN I-O F-MOVIMIENTOS
               IF FSM <> 00
                   GO TO PSYS-ERR
               READ F-MOVIMIENTOS INVALID KEY GO PSYS-ERR

               COMPUTE CENT-SALDO-ORD-USER = (MOV-SALDOPOS-ENT * 100)
                                           + MOV-SALDOPOS-DEC
               COMPUTE CENT-IMPOR-USER = (TRANSF-IMPORTE-ENT * 100)
                                       + TRANSF-IMPORTE-DEC
               *>SUBTRACT CENT-IMPOR-USER FROM CENT-SALDO-ORD-USER.
               *>COMPUTE MOV-SALDOPOS-ENT = (CENT-SALDO-ORD-USER / 100)
               *>MOVE FUNCTION MOD(CENT-SALDO-ORD-USER, 100)
                   *>TO MOV-SALDOPOS-DEC
               CLOSE F-MOVIMIENTOS

               MOVE LAST-USER-DST-MOV-NUM TO MOV-NUM
               OPEN I-O F-MOVIMIENTOS
               IF FSM <> 00
                   GO TO PSYS-ERR
               READ F-MOVIMIENTOS INVALID KEY GO PSYS-ERR
               COMPUTE CENT-SALDO-DST-USER = (MOV-SALDOPOS-ENT * 100)
                                         + MOV-SALDOPOS-DEC
               CLOSE F-MOVIMIENTOS
               *>ADD CENT-IMPOR-USER TO CENT-SALDO-DST-USER


           END-IF.


       BUCLE.

           OPEN I-O F-MOVIMIENTOS

           ADD 1 TO LAST-MOV-NUM

           SUBTRACT CENT-IMPOR-USER FROM CENT-SALDO-ORD-USER.
           COMPUTE MOV-SALDOPOS-ENT = (CENT-SALDO-ORD-USER / 100).
           MOVE FUNCTION MOD(CENT-SALDO-ORD-USER, 100)
               TO MOV-SALDOPOS-DEC.

           MOVE LAST-MOV-NUM TO MOV-NUM
           MOVE TARJETA-ORIGEN TO MOV-TARJETA
           MOVE TRANSF-DIA TO MOV-DIA
           MOVE BUCLE-MES TO MOV-MES
           MOVE BUCLE-ANO TO MOV-ANO
           MOVE 0 TO MOV-HOR
           MOVE 0 TO MOV-MIN
           MOVE 0 TO MOV-SEG
           MOVE "Transferimos." TO MOV-CONCEPTO


           MULTIPLY -1 BY TRANSF-IMPORTE-ENT
           MOVE TRANSF-IMPORTE-ENT TO MOV-IMPORTE-ENT
           MULTIPLY -1 BY TRANSF-IMPORTE-ENT
           MOVE TRANSF-IMPORTE-DEC TO MOV-IMPORTE-DEC

           WRITE MOVIMIENTO-REG INVALID KEY GO TO PSYS-ERR

           ADD 1 TO LAST-MOV-NUM
           ADD CENT-IMPOR-USER TO CENT-SALDO-DST-USER
           COMPUTE MOV-SALDOPOS-ENT = (CENT-SALDO-DST-USER / 100).
               MOVE FUNCTION MOD(CENT-SALDO-DST-USER, 100)
                   TO MOV-SALDOPOS-DEC.

           MOVE LAST-MOV-NUM TO MOV-NUM
           MOVE TARJETA-DESTINO TO MOV-TARJETA
           MOVE TRANSF-IMPORTE-ENT TO MOV-IMPORTE-ENT
           MOVE TRANSF-IMPORTE-DEC TO MOV-IMPORTE-DEC
           MOVE TRANSF-DIA TO MOV-DIA
           MOVE BUCLE-MES TO MOV-MES
           MOVE BUCLE-ANO TO MOV-ANO
           MOVE 0 TO MOV-HOR
           MOVE 0 TO MOV-MIN
           MOVE 0 TO MOV-SEG
           MOVE "Nos transfieren." TO MOV-CONCEPTO


           WRITE MOVIMIENTO-REG INVALID KEY GO TO PSYS-ERR

           ADD 1 TO BUCLE-MES
           IF (BUCLE-MES < 12)
               MOVE FUNCTION MOD(BUCLE-MES,12) TO BUCLE-MES
           IF (BUCLE-MES = 1)
               ADD 1 TO BUCLE-ANO

           SUBTRACT 1 FROM NUM-MENSUALIDADES

           IF (NUM-MENSUALIDADES = 0)
               IF ((TRANSF-MES > ULTIMA-MENSUALIDAD
                   AND BUCLE-ANO = ULTIMO-ANO)
                   OR BUCLE-ANO > ULTIMO-ANO) THEN
                       MOVE BUCLE-MES TO ULTIMA-MENSUALIDAD

                IF (BUCLE-ANO > ULTIMO-ANO) THEN
                   MOVE BUCLE-ANO TO ULTIMO-ANO

                REWRITE TRANSF-REG
                GO TO LEER-TRANSF
           ELSE
               GO TO BUCLE
           END-IF.


       P2.
           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "ESC - Salir"  LINE 24 COL 33.
           INITIALIZE TNUM.
           INITIALIZE PIN-INTRODUCIDO.
           INITIALIZE TPIN.
           DISPLAY "Numero de tarjeta:" LINE 8 COL 15.
           DISPLAY "Inserte el pin de tarjeta:" LINE 9 COL 15.
           ACCEPT DATA-ACCEPT ON EXCEPTION
               IF ESC-PRESSED
                   GO TO IMPRIMIR-CABECERA
               ELSE
                   GO TO P2.

           OPEN I-O TARJETAS.
           IF FST NOT = 00
               GO TO PSYS-ERR.
           READ TARJETAS INVALID KEY GO TO PSYS-ERR.

           OPEN I-O INTENTOS.
           IF FSI NOT = 00
               GO TO PSYS-ERR.
           MOVE TNUM TO INUM.

           READ INTENTOS INVALID KEY GO TO PSYS-ERR.

           IF IINTENTOS = 0
               GO TO PINT-ERR.

           IF PIN-INTRODUCIDO NOT = TPIN
               GO TO PPIN-ERR.

           PERFORM REINICIAR-INTENTOS THRU REINICIAR-INTENTOS.

       PMENU.
           CLOSE TARJETAS.
           CLOSE INTENTOS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "1 - Consultar saldo" LINE 8 COL 15.
           DISPLAY "2 - Consultar movimientos" LINE 9 COL 15.
           DISPLAY "3 - Retirar efectivo" LINE 10 COL 15.
           DISPLAY "4 - Ingresar efectivo" LINE 11 COL 15.
           DISPLAY "5 - Ordenar transferencia" LINE 12 COL 15.
           DISPLAY "6 - Listado de transferencias" LINE 13 COL 15.
           DISPLAY "7 - Comprar entradas de espectaculos" LINE 15 COL 15.
           DISPLAY "8 - Cambiar clave" LINE 17 COL 15.
           DISPLAY "Elija una opcion y pulse Enter:" LINE 20 COL 20.
           DISPLAY "ESC - Salir" LINE 24 COL 34.

       PMENUA1.
           ACCEPT CHOICE LINE 20 COL 52 ON EXCEPTION
               IF ESC-PRESSED
                   GO TO IMPRIMIR-CABECERA
               ELSE
                   GO TO PMENUA1.


           IF CHOICE = 1
               CALL "BANK2" USING TNUM
               GO TO PMENU.

           IF CHOICE = 2
               CALL "BANK3" USING TNUM
               GO TO PMENU.

           IF CHOICE = 3
               CALL "BANK4" USING TNUM
               GO TO PMENU.

           IF CHOICE = 4
               CALL "BANK5" USING TNUM
               GO TO PMENU.

           IF CHOICE = 5
               CALL "BANK6" USING TNUM
               GO TO PMENU.

           IF CHOICE = 6
               CALL "BANK8" USING TNUM
               GO TO PMENU.

           IF CHOICE = 7
               CALL "BANK7" USING TNUM
               GO TO PMENU.

           IF CHOICE = 8
               CALL "BANK9" USING TNUM
               GO TO PMENU.

           GO TO PMENU.


       PSYS-ERR.

           CLOSE TARJETAS.
           CLOSE INTENTOS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "Ha ocurrido un error interno" LINE 9 COL 25
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Vuelva mas tarde" LINE 11 COL 32
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Enter - Aceptar" LINE 24 COL 33.
           GO TO PINT-ERR-ENTER.


       PINT-ERR.

           CLOSE TARJETAS.
           CLOSE INTENTOS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "(9 20) Se ha sobrepasado el numero de intentos"
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Por su seguridad se ha bloqueado la tarjeta" LINE 11 COL 18
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Acuda a una sucursal" LINE 12 COL 30
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Enter - Aceptar" LINE 24 COL 33.

       PINT-ERR-ENTER.
           ACCEPT CHOICE LINE 24 COL 80 ON EXCEPTION
           IF ENTER-PRESSED
               GO TO IMPRIMIR-CABECERA
           ELSE
               GO TO PINT-ERR-ENTER.


       PPIN-ERR.
           SUBTRACT 1 FROM IINTENTOS.
           REWRITE INTENTOSREG INVALID KEY GO TO PSYS-ERR.

           CLOSE TARJETAS.
           CLOSE INTENTOS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "El codigo PIN es incorrecto" LINE 9 COL 26
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Le quedan " LINE 11 COL 30
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY IINTENTOS LINE 11 COL 40
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY " intentos" LINE 11 COL 42
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.

           DISPLAY "Enter - Aceptar" LINE 24 COL 1.
           DISPLAY "ESC - Cancelar" LINE 24 COL 65.

       PPIN-ERR-ENTER.
           ACCEPT CHOICE LINE 24 COL 80 ON EXCEPTION
           IF ENTER-PRESSED
               GO TO P2
           ELSE
               IF ESC-PRESSED
                   GO TO IMPRIMIR-CABECERA
               ELSE
                   GO TO PPIN-ERR-ENTER.


       REINICIAR-INTENTOS.
           MOVE 3 TO IINTENTOS.
           REWRITE INTENTOSREG INVALID KEY GO TO PSYS-ERR.


       PCONSULTA-SALDO.
           OPEN INPUT F-MOVIMIENTOS.
           IF FSM <> 00
              GO TO PSYS-ERR.

           MOVE 0 TO LAST-MOV-NUM.
           MOVE 0 TO LAST-USER-ORD-MOV-NUM.
           MOVE 0 TO LAST-USER-DST-MOV-NUM.

       LECTURA-MOV.
           READ F-MOVIMIENTOS NEXT RECORD AT END GO LAST-MOV-FOUND.

               IF MOV-TARJETA = TARJETA-ORIGEN THEN
                   IF LAST-USER-ORD-MOV-NUM < MOV-NUM THEN
                       MOVE MOV-NUM TO LAST-USER-ORD-MOV-NUM
                   END-IF
               END-IF.
               IF MOV-TARJETA = TARJETA-DESTINO THEN
                   DISPLAY "FOUND" LINE 28 COL 28

                   IF LAST-USER-DST-MOV-NUM < MOV-NUM THEN
                       MOVE MOV-NUM TO LAST-USER-DST-MOV-NUM
                   END-IF
               END-IF.
               IF LAST-MOV-NUM < MOV-NUM
                   MOVE MOV-NUM TO LAST-MOV-NUM.
               GO LECTURA-MOV.

       LAST-MOV-FOUND.
           CLOSE F-MOVIMIENTOS.

           IF TIPO-TRANSF = 1 THEN
               GO TO GUARDAR-TRANSF-1
           ELSE
               GO TO GUARDAR-TRANSF-2
           END-IF.

       EXIT-ENTER.
           ACCEPT PRESSED-KEY LINE 24 COL 80.
           IF ENTER-PRESSED
               EXIT PROGRAM
           ELSE
               GO TO EXIT-ENTER.
