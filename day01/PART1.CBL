      * DISCLAIMER:
      * I wrote this in COBOL after I had done it in Java. Also did I compile
      * this using gnucobol 3.2 as I have not set up my mainframe account yet

       IDENTIFICATION DIVISION.
       PROGRAM-ID. AOC23D01PT1.

       ENVIRONMENT DIVISION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT INPUT-FILE ASSIGN TO "input.txt"
           ORGANIZATION IS LINE SEQUENTIAL.

       DATA DIVISION.
       FILE SECTION.
       FD  INPUT-FILE LABEL RECORDS ARE OMITTED.
       01  LYNE            PIC X(60).
       
       WORKING-STORAGE SECTION.
       01  FILE-STATUS     PIC 9 VALUE 0.
       
       01  IDX             PIC 9(3) VALUE 1.
       01  CURR-CHAR       PIC X.

       01  ONE             PIC S9(2) VALUE -1.
       01  TWO             PIC 9(1) VALUE 0.
       01  RESULT          PIC 9(9) VALUE 0.
       
       PROCEDURE DIVISION.
       MAIN.
           OPEN INPUT INPUT-FILE
           PERFORM UNTIL FILE-STATUS = 1
               READ INPUT-FILE
                   AT END SET FILE-STATUS TO 1
                   NOT AT END PERFORM PROCESS-LINE
               END-READ
           END-PERFORM.
           CLOSE INPUT-FILE.
           DISPLAY RESULT.
           STOP RUN.
       
       PROCESS-LINE.
           MOVE -1 TO ONE.
           MOVE -1 TO TWO.

           PERFORM VARYING IDX FROM 1 BY 1 UNTIL IDX >
           FUNCTION LENGTH (LYNE)
               MOVE LYNE(IDX:1) TO CURR-CHAR
               IF CURR-CHAR IS NUMERIC
                   IF ONE = -1
                       MOVE FUNCTION NUMVAL(CURR-CHAR) TO ONE
                       MOVE ONE TO TWO
                   ELSE
                       MOVE FUNCTION NUMVAL(CURR-CHAR) TO TWO
                   END-IF
               END-IF
           END-PERFORM.
           COMPUTE ONE = ONE * 10 + TWO.
           ADD ONE TO RESULT.
