
(display "> (define (fib-rec n anterior acumulado)") (newline)
(display "    (cond") (newline)
(display "      ((equal? n 0) acumulado)") (newline)
(display "      (#t (fib-rec (- n 1) acumulado (+ anterior acumulado))") (newline)
(display "   )))") (newline)

(display
    (define (fib-rec n anterior acumulado)
      (cond
         ((equal? n 0) acumulado)
         (#t (fib-rec (- n 1) acumulado (+ anterior acumulado))
        )
      )
    )
)(newline)

(display "> (define (fibonacci n)") (newline)
(display "    (cond") (newline)
(display "      ((< n 0) \"Es negativo\")") (newline)
(display "      ((equal? n 0) 0)") (newline)
(display "      (#t (fib-rec (- n 1) 0 1)") (newline)
(display "   )))") (newline)

(display
    (define (fibonacci n)
      (cond
         ((< n 0) "Es negativo")
         ((equal? n 0) 0)
         (#t (fib-rec (- n 1) 0 1)
        )
      )
    )
) (newline)

(display "Pruebas de fibonacci") (newline)
(display "fib 0: ")(if (equal? 0 (fibonacci 0)) (display "OK") (display "ERROR"))(newline)
(display "fib 1: ")(if (equal? 1 (fibonacci 1)) (display "OK") (display "ERROR"))(newline)
(display "fib 2: ")(if (equal? 1 (fibonacci 2)) (display "OK") (display "ERROR"))(newline)
(display "fib 3: ")(if (equal? 2 (fibonacci 3)) (display "OK") (display "ERROR"))(newline)
(display "fib 4: ")(if (equal? 3 (fibonacci 4)) (display "OK") (display "ERROR"))(newline)
(display "fib 5: ")(if (equal? 5 (fibonacci 5)) (display "OK") (display "ERROR"))(newline)
(display "fib 6: ")(if (equal? 8 (fibonacci 6)) (display "OK") (display "ERROR"))(newline)
(display "fib 7: ")(if (equal? 13 (fibonacci 7)) (display "OK") (display "ERROR"))(newline)
(display "fib 8: ")(if (equal? 21 (fibonacci 8)) (display "OK") (display "ERROR"))(newline)
(display "fib 9: ")(if (equal? 34 (fibonacci 9)) (display "OK") (display "ERROR"))(newline)