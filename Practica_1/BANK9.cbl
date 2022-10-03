       IDENTIFICATION DIVISION.
       PROGRAM-ID. BANK9.

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

           SELECT INTENTOS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS INUM
           FILE STATUS IS FSI.

       DATA DIVISION.
       FILE SECTION.
       FD TARJETAS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "tarjetas.ubd".
       01 TAJETAREG.
           02 TNUM-E      PIC 9(16).
           02 TPIN-E      PIC  9(4).

       FD INTENTOS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "intentos.ubd".
       01 INTENTOSREG.
           02 INUM      PIC 9(16).
           02 IINTENTOS PIC 9(1).


       WORKING-STORAGE SECTION.
       77 FST                      PIC  X(2).
       77 FSI                      PIC  X(2).

       78 BLACK                   VALUE      0.
       78 BLUE                    VALUE      1.
       78 GREEN                   VALUE      2.
       78 CYAN                    VALUE      3.
       78 RED                     VALUE      4.
       78 MAGENTA                 VALUE      5.
       78 YELLOW                  VALUE      6.
       78 WHITE                   VALUE      7.

       01 CAMPOS-FECHA.
           05 FECHA.
               10 ANO               PIC   9(4).
               10 MES               PIC   9(2).
               10 DIA               PIC   9(2).
           05 HORA.
               10 HORAS             PIC   9(2).
               10 MINUTOS           PIC   9(2).
               10 SEGUNDOS          PIC   9(2).
               10 MILISEGUNDOS      PIC   9(2).
           05 DIF-GMT               PIC  S9(4).

       01 KEYBOARD-STATUS           PIC  9(4).
           88 ENTER-PRESSED       VALUE     0.
           88 PGUP-PRESSED        VALUE  2001.
           88 PGDN-PRESSED        VALUE  2002.
           88 UP-ARROW-PRESSED    VALUE  2003.
           88 DOWN-ARROW-PRESSED  VALUE  2004.
           88 ESC-PRESSED         VALUE  2005.
       *> NUEVO
       01 WS-Tarjeta.
           02 TNUM-ID      PIC 9(16).
           02 TPIN-ID      PIC  9(4).
       01 WS-EOF   PIC A(1).
       01 TPIN-USER      PIC  9(4).

       77 NEW-PIN                  PIC  9(4).

       77 LAST-MOV-NUM             PIC  9(35).
       77 PRESSED-KEY              PIC   9(1).

       LINKAGE SECTION.
       77 TNUM                     PIC  9(16).

       SCREEN SECTION.
       01 BLANK-SCREEN.
           05 FILLER LINE 1 BLANK SCREEN BACKGROUND-COLOR BLACK.
       *> NUEVO
       01 ENTRADA-USUARIO.
           05 FILLER BLANK ZERO UNDERLINE
               LINE 12 COL 55 PIC 9(4) USING NEW-PIN.

       PROCEDURE DIVISION USING TNUM.
       IMPRIMIR-CABECERA.

           SET ENVIRONMENT 'COB_SCREEN_EXCEPTIONS' TO 'Y'.

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

       PANTALLA-INICIO SECTION.
           INITIALIZE TPIN-USER.
           *> NUEVO
           OPEN INPUT TARJETAS.
           *>IF FST NOT = 00
             *>  GO TO PSYS-ERR.
           READ TARJETAS INTO WS-Tarjeta
               AT END CLOSE TARJETAS
               NOT AT END
                   PERFORM
                       IF TNUM-ID = TNUM
                           MOVE TPIN-ID TO TPIN-USER
                   END-PERFORM
           END-READ
           CLOSE TARJETAS.


           DISPLAY "Enter - Aceptar" LINE 24 COL 3.
           DISPLAY "ESC - Cancelar" LINE 24 COL 66.
           DISPLAY "Cambia el pin de tu tarjeta" LINE 6 COL 26.
           DISPLAY "El pin de tu cuenta" LINE 10 COL 19.
           DISPLAY TNUM LINE 10 COL 39.
           DISPLAY "es" LINE 10 COL 56.
           DISPLAY TPIN-USER LINE 10 COL 59.

           DISPLAY "Introduzca un nuevo pin: " LINE 12 COL 29.
           ACCEPT ENTRADA-USUARIO ON EXCEPTION
           IF ESC-PRESSED THEN
               EXIT PROGRAM
           ELSE
               GO TO PANTALLA-INICIO
           END-IF.

       ESCRITURA.
           *> NUEVO
           OPEN I-O TARJETAS.
           IF FST NOT = 00
               GO TO PSYS-ERR.
           MOVE TNUM TO TNUM-E
           DELETE TARJETAS

           MOVE TNUM TO TNUM-E
           MOVE NEW-PIN TO TPIN-E
           WRITE TAJETAREG INVALID KEY GO PSYS-ERR
           CLOSE TARJETAS.

       FINALIZACION SECTION.
           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "El nuevo pin de la cuenta es:" LINE 8 COL 17.
           DISPLAY NEW-PIN LINE 8 COL 47.
           DISPLAY "Enter - Aceptar" LINE 24 COL 33.
           GO TO EXIT-ENTER.

       PSYS-ERR.
           CLOSE TARJETAS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "Ha ocurrido un error interno=======" LINE 9 COL 25
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Vuelva mas tarde" LINE 11 COL 32
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Enter - Aceptar" LINE 24 COL 33.

       EXIT-ENTER.
           ACCEPT OMITTED
           IF ENTER-PRESSED
               EXIT PROGRAM
           ELSE
               GO TO EXIT-ENTER.
