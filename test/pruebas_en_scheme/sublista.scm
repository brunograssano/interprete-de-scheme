
(display "> (define (drop lista pos-inicial))") (newline)
(display "    (if (equal? pos-inicial 0)") (newline)
(display "      lista") (newline)
(display "      (drop (cdr lista) (- pos-inicial 1))") (newline)
(display "      ))") (newline)

(define (drop lista pos-inicial)
    (if (equal? pos-inicial 0)
        lista
        (drop (cdr lista) (- pos-inicial 1))
    )
)

(display "> (define (take lista acumulado longitud)") (newline)
(display "    (cond") (newline)
(display "      ((equal? longitud 0) (reverse acumulado))") (newline)
(display "      (#t (take (cdr lista) (cons (car lista) acumulado) (- longitud 1)))") (newline)
(display "      ))") (newline)

(define (take lista acumulado longitud)
    (cond
        ((equal? longitud 0) (reverse acumulado))
        (#t (take (cdr lista) (cons (car lista) acumulado) (- longitud 1)))
    )
)

(display "> (define (sublist lista pos-inicial longitud)") (newline)
(display "    (cond") (newline)
(display "      ((> pos-inicial (length lista)) () )") (newline)
(display "      ((< longitud 0) () )") (newline)
(display "      ((> (+ pos-inicial longitud) (length lista)) (take (drop lista pos-inicial) () (- (length lista) pos-inicial)))") (newline)
(display "      (#t (take (drop lista pos-inicial) () longitud))") (newline)
(display "      ))") (newline)

(define (sublist lista pos-inicial longitud)
    (cond
        ((> pos-inicial (length lista)) () )
        ((< longitud 0) () )
        ((> (+ pos-inicial longitud) (length lista)) (take (drop lista pos-inicial) () (- (length lista) pos-inicial)))
        (#t (take (drop lista pos-inicial) () longitud))
    )
)

(display "Pruebas de sublist") (newline)
(display "sublist: ")(if (equal? '(0 1 2 3 4 5) (sublist '(0 1 2 3 4 5) 0 6)) (display "OK") (display "ERROR"))(newline)
(display "sublist: ")(if (equal? '(1 2 3) (sublist '(0 1 2 3 4 5) 1 3)) (display "OK") (display "ERROR"))(newline)
(display "sublist: ")(if (equal? '() (sublist '(0 1 2 3 4 5) 0 0)) (display "OK") (display "ERROR"))(newline)
(display "sublist: ")(if (equal? '() (sublist '(0 1 2 3 4 5) 9 3)) (display "OK") (display "ERROR"))(newline)
(display "sublist: ")(if (equal? '(3 4) (sublist '(0 1 2 3 4 5) 3 2)) (display "OK") (display "ERROR"))(newline)