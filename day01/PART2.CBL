      * DISCLAIMER:
      * I wrote this in COBOL after I had done it in Java. Also did I compile
      * this using gnucobol 3.2 as I have not set up my mainframe account yet

       IDENTIFICATION DIVISION.
       PROGRAM-ID. AOC23D01PT2.

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
       
       01  WORD_NUM_FOUND  PIC 9(1) VALUE 0.

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
           MOVE  1 TO IDX.
           MOVE -1 TO ONE.
           MOVE -1 TO TWO.

           PERFORM UNTIL IDX > FUNCTION LENGTH(LYNE)
               MOVE LYNE(IDX:1) TO CURR-CHAR
               IF CURR-CHAR IS NUMERIC
                   PERFORM PROCESS-NUMERIC
               ELSE
                   PERFORM PROCESS-TEXT
               END-IF
               ADD 1 TO IDX
           END-PERFORM.

           COMPUTE ONE = ONE * 10 + TWO.
           ADD ONE TO RESULT.

       PROCESS-NUMERIC.
           IF ONE = -1
               MOVE FUNCTION NUMVAL(CURR-CHAR) TO ONE
               MOVE ONE TO TWO
           ELSE
               MOVE FUNCTION NUMVAL(CURR-CHAR) TO TWO
           END-IF.

       PROCESS-TEXT.
           MOVE 0 TO WORD_NUM_FOUND.

           IF LYNE(IDX:3) = "one"
               MOVE 1 TO WORD_NUM_FOUND
           END-IF.
           IF LYNE(IDX:3) = "two"
               MOVE 2 TO WORD_NUM_FOUND
           END-IF.
           IF LYNE(IDX:5) = "three"
               MOVE 3 TO WORD_NUM_FOUND
           END-IF.
           IF LYNE(IDX:4) = "four"
               MOVE 4 TO WORD_NUM_FOUND
           END-IF.
           IF LYNE(IDX:4) = "five"
               MOVE 5 TO WORD_NUM_FOUND
           END-IF.
           IF LYNE(IDX:3) = "six"
               MOVE 6 TO WORD_NUM_FOUND
           END-IF.
           IF LYNE(IDX:5) = "seven"
               MOVE 7 TO WORD_NUM_FOUND
           END-IF.
           IF LYNE(IDX:5) = "eight"
               MOVE 8 TO WORD_NUM_FOUND
           END-IF.
           IF LYNE(IDX:4) = "nine"
               MOVE 9 TO WORD_NUM_FOUND
           END-IF.

           IF WORD_NUM_FOUND > 0
               IF ONE = -1
                   MOVE WORD_NUM_FOUND TO ONE
                   MOVE ONE TO TWO
               ELSE
                   MOVE WORD_NUM_FOUND TO TWO
               END-IF
           END-IF.
