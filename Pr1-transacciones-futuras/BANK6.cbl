       IDENTIFICATION DIVISION.
       PROGRAM-ID. BANK6.

       ENVIRONMENT DIVISION.
       CONFIGURATION SECTION.
       SPECIAL-NAMES.
           CRT STATUS IS KEYBOARD-STATUS.

       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT TARJETAS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS TNUM-E
           FILE STATUS IS FST.

           SELECT F-MOVIMIENTOS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS MOV-NUM
           FILE STATUS IS FSM.

           SELECT TRANSFERENCIAS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS TRANSF-NUM
           FILE STATUS IS FSTM.


       DATA DIVISION.
       FILE SECTION.
       FD TARJETAS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "tarjetas.ubd".
       01 TAJETAREG.
           02 TNUM-E      PIC 9(16).
           02 TPIN-E      PIC  9(4).
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


       WORKING-STORAGE SECTION.
       77 FST                      PIC   X(2).
       77 FSM                      PIC   X(2).
       77 FSTM                     PIC   X(2).

       78 BLACK                  VALUE      0.
       78 BLUE                   VALUE      1.
       78 GREEN                  VALUE      2.
       78 CYAN                   VALUE      3.
       78 RED                    VALUE      4.
       78 MAGENTA                VALUE      5.
       78 YELLOW                 VALUE      6.
       78 WHITE                  VALUE      7.

       01 CAMPOS-FECHA.
           05 FECHA.
               10 ANO              PIC   9(4).
               10 MES              PIC   9(2).
               10 DIA              PIC   9(2).
           05 HORA.
               10 HORAS            PIC   9(2).
               10 MINUTOS          PIC   9(2).
               10 SEGUNDOS         PIC   9(2).
               10 MILISEGUNDOS     PIC   9(2).
           05 DIF-GMT              PIC  S9(4).

       01 KEYBOARD-STATUS          PIC  9(4).
           88 ENTER-PRESSED      VALUE     0.
           88 PGUP-PRESSED       VALUE  2001.
           88 PGDN-PRESSED       VALUE  2002.
           88 UP-ARROW-PRESSED   VALUE  2003.
           88 DOWN-ARROW-PRESSED VALUE  2004.
           88 ESC-PRESSED        VALUE  2005.

       77 PRESSED-KEY              PIC   9(4).

       77 LAST-MOV-NUM             PIC  9(35).
       77 LAST-USER-ORD-MOV-NUM    PIC  9(35).
       77 LAST-USER-DST-MOV-NUM    PIC  9(35).

       77 EURENT-USUARIO           PIC  S9(7).
       77 EURDEC-USUARIO           PIC   9(2).
       77 CUENTA-DESTINO           PIC  9(16).
       77 NOMBRE-DESTINO           PIC  X(35).
       *> NUEVO
       77 DIA-USUARIO           PIC   9(2).
       77 MES-USUARIO           PIC   9(2).
       77 ANO-USUARIO           PIC   9(4).
       77 DIA-MENSUAL-USUARIO   PIC   9(2).
       77 BUCLE-MES             PIC   99 VALUE 0.

       77 CENT-SALDO-ORD-USER      PIC  S9(9).
       77 CENT-SALDO-DST-USER      PIC  S9(9).
       77 CENT-IMPOR-USER          PIC  S9(9).

       77 MSJ-ORD                  PIC  X(35) VALUE "Transferimos".
       77 MSJ-DST                  PIC  X(35) VALUE "Nos transfieren".

       LINKAGE SECTION.
       77 TNUM                     PIC  9(16).

       SCREEN SECTION.
       01 BLANK-SCREEN.
           05 FILLER LINE 1 BLANK SCREEN BACKGROUND-COLOR BLACK.

       01 FILTRO-CUENTA.
           05 FILLER BLANK WHEN ZERO AUTO UNDERLINE
               LINE 12 COL 54 PIC 9(16) USING CUENTA-DESTINO.
           05 FILLER AUTO UNDERLINE
                LINE 14 COL 54 PIC X(15) USING NOMBRE-DESTINO.
           05 FILLER BLANK ZERO AUTO UNDERLINE
      *>          SIGN IS LEADING SEPARATE
               LINE 16 COL 54 PIC 9(7) USING EURENT-USUARIO.
           05 FILLER AUTO UNDERLINE
               LINE 16 COL 63 PIC 9(2) USING EURDEC-USUARIO.
           *> NUEVO
           05 FILLER BLANK ZERO AUTO UNDERLINE
               LINE 19 COL 58 PIC 9(2) USING DIA-USUARIO.
           05 FILLER BLANK ZERO AUTO UNDERLINE
               LINE 19 COL 61 PIC 9(2) USING MES-USUARIO.
           05 FILLER BLANK ZERO AUTO  UNDERLINE
               LINE 19 COL 64 PIC 9(4) USING ANO-USUARIO.
           05 FILLER BLANK ZERO UNDERLINE
               LINE 20 COL 70 PIC 9(2) USING DIA-MENSUAL-USUARIO.

       01 SALDO-DISPLAY.
           05 FILLER SIGN IS LEADING SEPARATE
               LINE 10 COL 33 PIC -9(7) FROM MOV-SALDOPOS-ENT.
           05 FILLER LINE 10 COL 41 VALUE ",".
           05 FILLER LINE 10 COL 42 PIC 99 FROM MOV-SALDOPOS-DEC.
           05 FILLER LINE 10 COL 45 VALUE "EUR".


       PROCEDURE DIVISION USING TNUM.
       INICIO.
           SET ENVIRONMENT 'COB_SCREEN_EXCEPTIONS' TO 'Y'.

           INITIALIZE CUENTA-DESTINO.
           INITIALIZE NOMBRE-DESTINO.
           INITIALIZE EURENT-USUARIO.
           INITIALIZE EURDEC-USUARIO.
           *>NUEVO
           INITIALIZE DIA-USUARIO.
           INITIALIZE MES-USUARIO.
           INITIALIZE ANO-USUARIO.
           INITIALIZE DIA-MENSUAL-USUARIO.
           INITIALIZE LAST-MOV-NUM.
           INITIALIZE LAST-USER-ORD-MOV-NUM.
           INITIALIZE LAST-USER-DST-MOV-NUM.

       IMPRIMIR-CABECERA.
           DISPLAY BLANK-SCREEN.
           DISPLAY "Cajero Automatico UnizarBank" LINE 2 COL 26
               WITH FOREGROUND-COLOR IS 1.

           MOVE FUNCTION CURRENT-DATE TO CAMPOS-FECHA.

           DISPLAY DIA LINE 4 COL 32.
           DISPLAY "-" LINE 4 COL 34.
           DISPLAY MES LINE 4 COL 35.
           DISPLAY "-" LINE 4 COL 37.
           DISPLAY ANO LINE 4 COL 38.
           DISPLAY HORAS LINE 4 COL 44.
           DISPLAY ":" LINE 4 COL 46.
           DISPLAY MINUTOS LINE 4 COL 47.
       *>NUEVO
       MOVIMIENTOS-OPEN.
           OPEN I-O F-MOVIMIENTOS.
           *>IF FSM <> 00 THEN
             *>  GO TO ERROROOROR
           *>END-IF.

       LECTURA-MOVIMIENTOS.
           READ F-MOVIMIENTOS NEXT RECORD AT END GO TO ORDENACION-TRF.
           IF MOV-TARJETA = TNUM THEN
               IF LAST-USER-ORD-MOV-NUM < MOV-NUM THEN
                   MOVE MOV-NUM TO LAST-USER-ORD-MOV-NUM
               END-IF
           END-IF.
           IF LAST-MOV-NUM < MOV-NUM THEN
               MOVE MOV-NUM TO LAST-MOV-NUM
           END-IF.
           GO TO LECTURA-MOVIMIENTOS.

       ORDENACION-TRF.
           CLOSE F-MOVIMIENTOS.

           DISPLAY "Ordenar Transferencia" LINE 8 COL 30.
           DISPLAY "Saldo Actual:" LINE 10 COL 19.

           DISPLAY "Enter - Confirmar" LINE 24 COL 2.
           DISPLAY "ESC - Cancelar" LINE 24 COL 66.

           IF LAST-USER-ORD-MOV-NUM = 0 THEN
               GO TO NO-MOVIMIENTOS
           END-IF.

           MOVE LAST-USER-ORD-MOV-NUM TO MOV-NUM.

           PERFORM MOVIMIENTOS-OPEN THRU MOVIMIENTOS-OPEN.
           READ F-MOVIMIENTOS INVALID KEY GO PSYS-ERR.
           DISPLAY SALDO-DISPLAY.
           CLOSE F-MOVIMIENTOS.

       INDICAR-CTA-DST.
           DISPLAY "Indica la cuenta destino" LINE 12 COL 19.
           DISPLAY "y nombre del titular" LINE 14 COL 19.
           DISPLAY "Indique la cantidad a transferir" LINE 16 COL 19.
           DISPLAY "," LINE 16 COL 61.
           DISPLAY "EUR" LINE 16 COL 66.
           *> NUEVO
           DISPLAY "Indique una opcion (la otra rellenela con 0's):"
               LINE 18 COL 19.
           DISPLAY "Fecha que se producira la transaccion    /  /"
               LINE 19 COL 19.
           DISPLAY "Dia en que se producira la transacion mensualmente"
               LINE 20 COL 19.

           COMPUTE CENT-SALDO-ORD-USER = (MOV-SALDOPOS-ENT * 100)
                                         + MOV-SALDOPOS-DEC.

           ACCEPT FILTRO-CUENTA ON EXCEPTION
           IF ESC-PRESSED THEN
               EXIT PROGRAM
           ELSE
               GO TO INDICAR-CTA-DST
           END-IF.

           COMPUTE CENT-IMPOR-USER = (EURENT-USUARIO * 100)
                                     + EURDEC-USUARIO.

           IF CENT-IMPOR-USER > CENT-SALDO-ORD-USER THEN
                   DISPLAY "Indique una cantidad menor!!" LINE 20 COL 19
                    WITH BACKGROUND-COLOR RED
                   GO TO INDICAR-CTA-DST
           END-IF.

           GO TO REALIZAR-TRF-VERIFICACION.

       NO-MOVIMIENTOS.
           DISPLAY "0" LINE 10 COL 51.
           DISPLAY "." LINE 10 COL 52.
           DISPLAY "00" LINE 10 COL 53.
           DISPLAY "EUR" LINE 10 COL 54.

           DISPLAY "Indica la cuenta destino " LINE 12 COL 19.
           DISPLAY "y nombre del titular" LINE 14 COL 19.
           DISPLAY "Indique la cantidad a transferir" LINE 16 COL 19.
           DISPLAY "," LINE 16 COL 61.
           DISPLAY "EUR" LINE 16 COL 66.
           *> NUEVO
           DISPLAY "Indique una opcion (la otra rellenela con 0's):"
               LINE 18 COL 19.
           DISPLAY "Fecha que se producira la transaccion    /  /"
               LINE 19 COL 19.
           DISPLAY "Dia en que se producira la transacion mensualmente"
               LINE 20 COL 19.

           ACCEPT FILTRO-CUENTA ON EXCEPTION
           IF ESC-PRESSED THEN
               EXIT PROGRAM
           END-IF.

           DISPLAY "Indique una cantidad menor!!" LINE 20 COL 19
            WITH BACKGROUND-COLOR RED.

           GO TO NO-MOVIMIENTOS.

       REALIZAR-TRF-VERIFICACION.
           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "Ordenar Transferencia" LINE 8 COL 30.
           DISPLAY "Va a transferir:" LINE 11 COL 19.
           DISPLAY EURENT-USUARIO LINE 11 COL 38.
           DISPLAY "." LINE 11 COL 45.
           DISPLAY EURDEC-USUARIO LINE 11 COL 46.
           DISPLAY "EUR de su cuenta" LINE 11 COL 49.
           DISPLAY "a la cuenta cuyo titular es" LINE 12 COL 19.
           DISPLAY NOMBRE-DESTINO LINE 12 COL 48.

           DISPLAY "Enter - Confirmar" LINE 24 COL 2.
           DISPLAY "ESC - Cancelar" LINE 24 COL 66.

       ENTER-VERIFICACION.
           ACCEPT PRESSED-KEY  LINE 24 COL 80 ON EXCEPTION
           IF ESC-PRESSED THEN
               EXIT PROGRAM
           ELSE
               GO TO ENTER-VERIFICACION
           END-IF.

       *> NUEVO
       CHECK-MENSUALIDAD.
           IF DIA-MENSUAL-USUARIO <> 00 THEN
                COMPUTE BUCLE-MES = BUCLE-MES + 1
                IF BUCLE-MES > 12 THEN
                    CLOSE F-MOVIMIENTOS
                    GO TO P-EXITO
           ELSE
               GO TO VERIFICACION-CTA-CORRECTA
           END-IF.

       VERIFICACION-CTA-CORRECTA.
           OPEN I-O TARJETAS.
           IF FST <> 00
              GO TO PSYS-ERR.

           MOVE CUENTA-DESTINO TO TNUM-E.
           READ TARJETAS INVALID KEY GO TO USER-BAD.
           CLOSE TARJETAS.

           PERFORM MOVIMIENTOS-OPEN THRU MOVIMIENTOS-OPEN.
           MOVE 0 TO MOV-NUM.
           MOVE 0 TO LAST-USER-DST-MOV-NUM.

       LECTURA-SALDO-DST.
           READ F-MOVIMIENTOS NEXT RECORD AT END
           GO TO CHECK-TRANSFERENCIA.
               IF MOV-TARJETA = CUENTA-DESTINO THEN
                   IF LAST-USER-DST-MOV-NUM < MOV-NUM THEN
                       MOVE MOV-NUM TO LAST-USER-DST-MOV-NUM
                   END-IF
               END-IF.

               GO TO LECTURA-SALDO-DST.

       CHECK-TRANSFERENCIA.
           IF ((DIA-USUARIO <> 0 AND MES-USUARIO <> 0 AND
               DIA-USUARIO <> 0) OR (DIA-MENSUAL-USUARIO <> 00)) THEN
                   OPEN I-O TRANSFERENCIAS
                   GO TO LECTURA-TRANSFERENCIAS
           ELSE
                   GO TO GUARDAR-TRF
           END-IF.

       GUARDAR-TRF.
           CLOSE F-MOVIMIENTOS.
           MOVE LAST-USER-DST-MOV-NUM TO MOV-NUM.
           PERFORM MOVIMIENTOS-OPEN THRU MOVIMIENTOS-OPEN.
           READ F-MOVIMIENTOS INVALID KEY GO PSYS-ERR.

           COMPUTE CENT-SALDO-DST-USER = (MOV-SALDOPOS-ENT * 100)
                                         + MOV-SALDOPOS-DEC.

           MOVE FUNCTION CURRENT-DATE TO CAMPOS-FECHA.

           ADD 1 TO LAST-MOV-NUM.

           *> NUEVO
           MOVE LAST-MOV-NUM   TO MOV-NUM.
           MOVE TNUM           TO MOV-TARJETA.
           IF DIA-MENSUAL-USUARIO = 00 THEN
               MOVE ANO-USUARIO    TO MOV-ANO
               MOVE MES-USUARIO    TO MOV-MES
               MOVE DIA-USUARIO    TO MOV-DIA
           ELSE
               MOVE ANO                 TO MOV-ANO
               MOVE BUCLE-MES           TO MOV-MES
               MOVE DIA-MENSUAL-USUARIO TO MOV-DIA
           END-IF.
           MOVE HORAS          TO MOV-HOR.
           MOVE MINUTOS        TO MOV-MIN.
           MOVE SEGUNDOS       TO MOV-SEG.

           MULTIPLY -1 BY EURENT-USUARIO.
           MOVE EURENT-USUARIO TO MOV-IMPORTE-ENT.
           MULTIPLY -1 BY EURENT-USUARIO.
           MOVE EURDEC-USUARIO TO MOV-IMPORTE-DEC.

           MOVE MSJ-ORD        TO MOV-CONCEPTO.

           SUBTRACT CENT-IMPOR-USER FROM CENT-SALDO-ORD-USER.

           COMPUTE MOV-SALDOPOS-ENT = (CENT-SALDO-ORD-USER / 100).
           MOVE FUNCTION MOD(CENT-SALDO-ORD-USER, 100)
               TO MOV-SALDOPOS-DEC.

           WRITE MOVIMIENTO-REG INVALID KEY GO TO PSYS-ERR.

           ADD 1 TO LAST-MOV-NUM.

           *> NUEVO
           MOVE LAST-MOV-NUM   TO MOV-NUM.
           MOVE CUENTA-DESTINO TO MOV-TARJETA.
           IF DIA-MENSUAL-USUARIO = 00 THEN
               MOVE ANO-USUARIO    TO MOV-ANO
               MOVE MES-USUARIO    TO MOV-MES
               MOVE DIA-USUARIO    TO MOV-DIA
           ELSE
               MOVE ANO                 TO MOV-ANO
               MOVE BUCLE-MES           TO MOV-MES
               MOVE DIA-MENSUAL-USUARIO TO MOV-DIA
           END-IF.
           MOVE HORAS          TO MOV-HOR.
           MOVE MINUTOS        TO MOV-MIN.
           MOVE SEGUNDOS       TO MOV-SEG.

           MOVE EURENT-USUARIO TO MOV-IMPORTE-ENT.
           MOVE EURDEC-USUARIO TO MOV-IMPORTE-DEC.

           MOVE MSJ-DST        TO MOV-CONCEPTO.

           ADD CENT-IMPOR-USER TO CENT-SALDO-DST-USER.
           COMPUTE MOV-SALDOPOS-ENT = (CENT-SALDO-DST-USER / 100).
           MOVE FUNCTION MOD(CENT-SALDO-DST-USER, 100)
               TO MOV-SALDOPOS-DEC.

           WRITE MOVIMIENTO-REG INVALID KEY GO TO PSYS-ERR.

           *> NUEVO
           IF DIA-MENSUAL-USUARIO <> 00 THEN
                GO TO CHECK-MENSUALIDAD
           ELSE
                CLOSE F-MOVIMIENTOS
                GO TO P-EXITO
           END-IF.

           CLOSE F-MOVIMIENTOS.
           GO TO P-EXITO.

       LECTURA-TRANSFERENCIAS.
           READ TRANSFERENCIAS NEXT RECORD
               AT END GO TO REGISTRAR-TRANSF-PENDIENTE.
                   IF LAST-MOV-NUM < TRANSF-NUM THEN
                       MOVE TRANSF-NUM TO LAST-MOV-NUM
                   END-IF.
                   GO TO LECTURA-TRANSFERENCIAS.

       REGISTRAR-TRANSF-PENDIENTE.
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

           CLOSE F-MOVIMIENTOS.
           MOVE LAST-USER-DST-MOV-NUM TO MOV-NUM.
           PERFORM MOVIMIENTOS-OPEN THRU MOVIMIENTOS-OPEN.
           READ F-MOVIMIENTOS INVALID KEY GO PSYS-ERR.

           MOVE FUNCTION CURRENT-DATE TO CAMPOS-FECHA.
           ADD 1 TO LAST-MOV-NUM.
           MOVE LAST-MOV-NUM   TO TRANSF-NUM.
           MOVE TNUM           TO TARJETA-ORIGEN.
           MOVE CUENTA-DESTINO TO TARJETA-DESTINO.
           MOVE EURENT-USUARIO TO TRANSF-IMPORTE-ENT.
           MOVE EURDEC-USUARIO TO TRANSF-IMPORTE-DEC.
           MOVE 0              TO ULTIMA-MENSUALIDAD.
           MOVE ANO            TO ULTIMO-ANO.
           MOVE DIA            TO DIA-ORDEN.
           IF DIA-MENSUAL-USUARIO = 00 THEN
               MOVE ANO-USUARIO    TO TRANSF-ANO
               MOVE MES-USUARIO    TO TRANSF-MES
               MOVE DIA-USUARIO    TO TRANSF-DIA
           ELSE
               MOVE 0                   TO TRANSF-ANO
               MOVE 0                   TO TRANSF-MES
               MOVE DIA-MENSUAL-USUARIO TO TRANSF-DIA
           END-IF.

           WRITE TRANSF-REG.
           IF FSTM <> 00 THEN
               PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA
               DISPLAY FSTM LINE 9 COL 25
                   WITH FOREGROUND-COLOR IS BLACK
                        BACKGROUND-COLOR IS RED
               DISPLAY "Error escribir en transferencias" LINE 11 COL 32
                   WITH FOREGROUND-COLOR IS BLACK
                        BACKGROUND-COLOR IS RED
               DISPLAY "Enter - Aceptar" LINE 24 COL 33
               DISPLAY TRANSF-NUM LINE 25 COL 33
               GO TO EXIT-ENTER
           END-IF.
           CLOSE TRANSFERENCIAS.


       P-EXITO.
           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.

           DISPLAY "Ordenar transferencia" LINE 8 COL 30.
           DISPLAY "Transferencia realizada correctamente!"
               LINE 11 COL 19.
           DISPLAY "Enter - Aceptar" LINE 24 COL 33.

           GO TO EXIT-ENTER.

       PSYS-ERR.
           CLOSE TARJETAS.
           CLOSE F-MOVIMIENTOS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "Ha ocurrido un error interno" LINE 9 COL 25
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Vuelva mas tarde" LINE 11 COL 32
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Enter - Aceptar" LINE 24 COL 33.

       ERROROOROR.
           CLOSE TARJETAS.
           CLOSE F-MOVIMIENTOS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY FSM LINE 9 COL 25
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Vuelva mas tarde" LINE 11 COL 32
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Enter - Aceptar" LINE 24 COL 33.

       EXIT-ENTER.
           ACCEPT PRESSED-KEY LINE 24 COL 80.
           IF ENTER-PRESSED
               EXIT PROGRAM
           ELSE
               GO TO EXIT-ENTER.

       USER-BAD.
           CLOSE TARJETAS.
           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "La cuenta introducida es incorrecta" LINE 9 COL 22
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Enter - Salir" LINE 24 COL 33.
           GO TO EXIT-ENTER.
