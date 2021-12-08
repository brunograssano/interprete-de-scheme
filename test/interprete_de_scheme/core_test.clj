(ns interprete-de-scheme.core-test
  (:require [clojure.test :refer :all]
            [interprete-de-scheme.core :refer :all]
            [interprete-de-scheme.scheme :refer :all]))

(deftest verificar-parentesis-test
  (testing "Sin parentesis"
    (is (= 0 (verificar-parentesis "No tengo parentesis")))
    (is (= 0 (verificar-parentesis "")))
    )
  (testing "Con parentesis cerrando en ambos lados"
    (is (= 0 (verificar-parentesis "(Tengo parentesis de forma correcta)")))
    (is (= 0 (verificar-parentesis "(Tengo (parentesis) de forma correcta)")))
    (is (= 0 (verificar-parentesis "(Tengo (parentesis) (de) (f)(o)(r)ma (corr)ecta)")))
    (is (= 0 (verificar-parentesis "(((((((())))))))")))
    )
  (testing "Cerrando solo del lado izquierdo"
    (is (= 1 (verificar-parentesis "(Tengo un parentesis")))
    (is (= 3 (verificar-parentesis "(Tengo( tres( parentesis")))
    (is (= 10 (verificar-parentesis "((((((((((")))
    )
  (testing "Cerrando solo del lado derecho"
    (is (= -1 (verificar-parentesis ")Tengo un parentesis")))
    (is (= -1 (verificar-parentesis "Tengo un parentesis)")))
    (is (= -1 (verificar-parentesis "Tengo un )parentesis")))
    (is (= -1 (verificar-parentesis "))))))))")))
    )
  (testing "Parentesis de ambos tipos"
    (is (= -1 (verificar-parentesis "( ( ) ) )")))
    (is (= -1 (verificar-parentesis "( ( ) ) )((((((")))
    (is (= 1 (verificar-parentesis "()()()(")))
    (is (= 3 (verificar-parentesis "()()()(((")))
    )
  )


(deftest error?-test
  (testing "Sin error o warning"
    (is (false? (error? '())))
    (is (false? (error? '('No 'Tengo 'Error))))
    )
  (testing "Con error"
    (is (true? (error? (list (symbol ";ERROR:") 'Hubo 'Error))))
    )
  (testing "Con warning"
    (is (true? (error? (list (symbol ";WARNING:") 'Hubo 'Error))))
    )
  )


(deftest actualizar-amb-test
  (testing "Agregar al ambiente"
    (is (= '(a 1) (actualizar-amb '() 'a 1)))
    (is (= '(a 1 b 2 c 3 d 4) (actualizar-amb '(a 1 b 2 c 3) 'd 4)))
    )
  (testing "Se mandan listas con error"
    (is (= '(a 1) (actualizar-amb '(a 1) 'b (list (symbol ";ERROR:") 'error))))
    (is (= '(a 1) (actualizar-amb '(a 1) 'b (list (symbol ";WARNING:") 'warning))))
    (is (= '() (actualizar-amb '() 'a (list (symbol ";ERROR:") 'error))))
    )
  (testing "Se remplazan valores del ambiente"
    (is (= '(a 2) (actualizar-amb '(a 1) 'a 2)))
    (is (= '(a 2 b 4 c 5 d 6) (actualizar-amb '(a 2 b 4 c 1 d 6) 'c 5)))
    (is (= '(a 2 b 4 C 5 d 6) (actualizar-amb '(a 2 b 4 C 1 d 6) 'c 5)))
    (is (= '(A 2 b 5 c 1 d 6) (actualizar-amb '(A 2 b 4 c 1 d 6) 'B 5)))
    (is (= '(A 59 B 4 C 1 D 6) (actualizar-amb '(A 2 B 4 C 1 D 6) 'a 59)))
    (is (= '(A 59 B 4 C 1 D 6) (actualizar-amb '(A 2 B 4 C 1 D 6) 'A 59)))
    (is (= '(+ + - - * * f +) (actualizar-amb '(+ + - - * * f /) 'f '+)))
    )
  )

(deftest buscar-test
  (testing "Busqueda de claves en ambiente"
    (is (= 3 (buscar 'c '(a 1 b 2 c 3 d 4))))
    (is (= 1 (buscar 'a '(a 1 b 2 c 3 d 4))))
    (is (= 'd (buscar 'd '(a a b b c b d d))))
    (is (= 7 (buscar 'd '(a 1 b 2 c 3 d 7 e 9))))
    )
  (testing "No se encuentra en el ambiente"
    (is (= (list (symbol ";ERROR:") (symbol "unbound variable:") 'c) (buscar 'c '(a 1 b 2 d 4))))
    (is (= (list (symbol ";ERROR:") (symbol "unbound variable:") 'c) (buscar 'c '())))
    )
  )


(deftest proteger-bool-en-str-test
  (testing "Sin bool"
    (is (= "" (proteger-bool-en-str "")))
    (is (= "no hay bool" (proteger-bool-en-str "no hay bool")))
    (is (= "#y #h #g #b t# f#" (proteger-bool-en-str "#y #h #g #b t# f#")))
    )
  (testing "Con bool"
    (is (= "(or %F %f %t %T)" (proteger-bool-en-str "(or #F #f #t #T)")))
    (is (= "(and (or %F %f %t %T) %T)" (proteger-bool-en-str "(and (or #F #f #t #T) #T)")))
    )
  )

(deftest restaurar-bool-test
  (let [t (symbol "#t"), T (symbol "#T"), f (symbol "#f"), F (symbol "#F")]
    (testing "Sin bool"
      (is (= '() (restaurar-bool (read-string "()"))))
      (is (= '(no hay bool) (restaurar-bool (read-string "(no hay bool)"))))
      (is (= '(+ 1 1) (restaurar-bool (read-string "(+ 1 1)"))))
      (is (= '(- 1 1) (restaurar-bool (read-string "(- 1 1)"))))
      (is (= '(zero? 0) (restaurar-bool (read-string "(zero? 0)"))))
      )
    (testing "Con bool"
      (is (= (list 'or F f t T )
             (restaurar-bool (read-string "(or %F %f %t %T)"))))
      (is (= (list 'and (list 'or F f (symbol "#t") T) T)
             (restaurar-bool (read-string "(and (or %F %f %t %T) %T)"))))
      (is (= (list 'and (list 'or (list F) (list f (list t)) T) T)
             (restaurar-bool (read-string "(and (or (%F) (%f (%t)) %T) %T)"))))
      (is (= "#t (#f (#F (#T)))"
             (restaurar-bool "%t (%f (%F (%T)))")))
      (is (= (list 'and (list 'or (list F) (list f (list t)) "#t" T) T)
             (restaurar-bool (read-string "(and (or (%F) (%f (%t)) \"%t\" %T) %T)"))))
      )
    )
  )

(deftest igual?-test
  (testing "Igualdades"
    (is (true? (igual? 'if 'IF)))
    (is (true? (igual? 'if 'if)))
    (is (true? (igual? 'If 'iF)))
    (is (true? (igual? "A" "A")))
    (is (true? (igual? '(a (A)) '(A (a)) )))
    (is (true? (igual? 1 1)))
    (is (true? (igual? '(1 2) '(1 2))))
    (is (true? (igual? nil nil)))
    (is (false? (igual? 'if "if")))
    (is (false? (igual? 'if "'if")))
    (is (false? (igual? "A" "a")))
    (is (false? (igual? 1 "1")))
    (is (false? (igual? nil 0)))
    )
  )

(deftest fnc-append-test
  (testing "Con argumentos correctos"
    (is (= '() (fnc-append '())))
    (is (= '(1 2 3 4 5 6 7) (fnc-append '( (1 2) (3) (4 5) (6 7)))))
    (is (= '(1 2 3 4 5 6 7 8 9) (fnc-append '( (1) (2) (3) (4) (5) (6) (7) (8) (9) ))))
    )
  (testing "Con argumentos erroneos"
    (is (= (list (symbol ";ERROR:") (symbol "append:") 'Wrong 'type 'in 'arg "Argumento con error") (fnc-append '("Argumento con error"))))
    (is (= (list (symbol ";ERROR:") (symbol "append:") 'Wrong 'type 'in 'arg 8) (fnc-append '((1) (2) (3) (4) (5) (6) (7) 8 (9)))))
    (is (= (list (symbol ";ERROR:") (symbol "append:") 'Wrong 'type 'in 'arg 9) (fnc-append '((1) (2) (3) (4) (5) (6) (7) (8) 9))))
    (is (= (list (symbol ";ERROR:") (symbol "append:") 'Wrong 'type 'in 'arg 'A) (fnc-append '((1) (2) (3) (4) (5) A (7) (8) 9))))
    )
  )


(deftest fnc-equal?-test
  (testing "Resultado verdadero"
    (is (= (symbol "#t") (fnc-equal? '())))
    (is (= (symbol "#t") (fnc-equal? '((symbol "#f")))))
    (is (= (symbol "#t") (fnc-equal? '(1 1))))
    (is (= (symbol "#t") (fnc-equal? '(nil nil))))
    (is (= (symbol "#t") (fnc-equal? '(1 1 1 1 1 1))))
    (is (= (symbol "#t") (fnc-equal? '(A a A a a a a A A))))
    (is (= (symbol "#t") (fnc-equal? '("a" "a" "a" "a"))))
    (is (= (symbol "#t") (fnc-equal? '((1) (1) (1) (1)))))
    )
  (testing "Resultado falso"
    (is (= (symbol "#f") (fnc-equal? '(1 2))))
    (is (= (symbol "#f") (fnc-equal? '(nil 0))))
    (is (= (symbol "#f") (fnc-equal? '(A B))))
    (is (= (symbol "#f") (fnc-equal? '((1) 1))))
    (is (= (symbol "#f") (fnc-equal? '("a" "b" "c" "a"))))
    )
  )

(deftest fnc-sumar-test
  (testing "Se manda a sumar una lista correcta"
    (is (zero? (fnc-sumar '())))
    (is (= 1 (fnc-sumar '(1))))
    (is (= 2 (fnc-sumar '(1 1))))
    (is (= 3 (fnc-sumar '(1 1 1))))
    (is (= 20 (fnc-sumar '(0 1 1 2 3 5 8))))
    )
  (testing "Se manda a sumar una lista con argumentos no validos"
    (is (= (list (symbol ";ERROR:") (symbol "+:") 'Wrong 'type 'in 'arg1 'A) (fnc-sumar '(A))))
    (is (= (list (symbol ";ERROR:") (symbol "+:") 'Wrong 'type 'in 'arg2 'B) (fnc-sumar '(2 B))))
    (is (= (list (symbol ";ERROR:") (symbol "+:") 'Wrong 'type 'in 'arg2 'D) (fnc-sumar '(2 3 4 D))))
    )
  )

(deftest fnc-restar-test
  (testing "Se manda a restar una lista correcta"
    (is (= -1 (fnc-restar '(1))))
    (is (= -5 (fnc-restar '(5))))
    (is (= 0 (fnc-restar '(1 1))))
    (is (= -1 (fnc-restar '(1 1 1))))
    (is (= -20 (fnc-restar '(0 1 1 2 3 5 8))))
    )
  (testing "Se manda a restar una lista con argumentos no validos"
    (is (= (list (symbol ";ERROR:") (symbol "-:") 'Wrong 'number 'of 'args 'given) (fnc-restar '())))
    (is (= (list (symbol ";ERROR:") (symbol "-:") 'Wrong 'type 'in 'arg1 'A) (fnc-restar '(A))))
    (is (= (list (symbol ";ERROR:") (symbol "-:") 'Wrong 'type 'in 'arg2 'B) (fnc-restar '(2 B))))
    (is (= (list (symbol ";ERROR:") (symbol "-:") 'Wrong 'type 'in 'arg2 'D) (fnc-restar '(2 3 4 D))))
    )
  )

(deftest fnc-menor-test
  (testing "Se manda una lista en orden estrictamente creciente"
    (is (= (symbol "#t") (fnc-menor '())))
    (is (= (symbol "#t") (fnc-menor '(1))))
    (is (= (symbol "#t") (fnc-menor '(-3 -2 -1 0 5))))
    (is (= (symbol "#t") (fnc-menor '(1 2))))
    (is (= (symbol "#t") (fnc-menor '(1 2 3))))
    (is (= (symbol "#t") (fnc-menor '(0 1 5 10 11 15 18))))
    )
  (testing "Se manda una lista desordenada (no creciente)"
    (is (= (symbol "#f") (fnc-menor '(2 1))))
    (is (= (symbol "#f") (fnc-menor '(1 2 3 4 5 4 3 2 1))))
    (is (= (symbol "#f") (fnc-menor '(0 100 200 0))))
    )
  (testing "Se manda una lista con argumentos no validos"
    (is (= (list (symbol ";ERROR:") (symbol "<:") 'Wrong 'type 'in 'arg1 'A) (fnc-menor '(A))))
    (is (= (list (symbol ";ERROR:") (symbol "<:") 'Wrong 'type 'in 'arg2 'B) (fnc-menor '(2 B))))
    (is (= (list (symbol ";ERROR:") (symbol "<:") 'Wrong 'type 'in 'arg2 'D) (fnc-menor '(2 3 4 D))))
    (is (= (list (symbol ";ERROR:") (symbol "<:") 'Wrong 'type 'in 'arg2 'D) (fnc-menor '(2 3 4 D 5))))
    )
  )

(deftest fnc-mayor-test
  (testing "Se manda una lista en orden estrictamente decreciente"
    (is (= (symbol "#t") (fnc-mayor '())))
    (is (= (symbol "#t") (fnc-mayor '(1))))
    (is (= (symbol "#t") (fnc-mayor '(5))))
    (is (= (symbol "#t") (fnc-mayor '(2 1))))
    (is (= (symbol "#t") (fnc-mayor '(3 2 1))))
    (is (= (symbol "#t") (fnc-mayor '(18 17 15 13 11 10 9 7 5 3 2 1 0 -3 -6))))
    )
  (testing "Se manda una lista desordenada (no decreciente)"
    (is (= (symbol "#f") (fnc-mayor '(2 1 2))))
    (is (= (symbol "#f") (fnc-mayor '(1 2 3 4 5 4 3 2 1))))
    (is (= (symbol "#f") (fnc-mayor '(100 99 98 99 100))))
    )
  (testing "Se manda una lista con argumentos no validos"
    (is (= (list (symbol ";ERROR:") (symbol ">:") 'Wrong 'type 'in 'arg1 'A) (fnc-mayor '(A))))
    (is (= (list (symbol ";ERROR:") (symbol ">:") 'Wrong 'type 'in 'arg2 'B) (fnc-mayor '(2 B))))
    (is (= (list (symbol ";ERROR:") (symbol ">:") 'Wrong 'type 'in 'arg2 'D) (fnc-mayor '(60 D 58))))
    (is (= (list (symbol ";ERROR:") (symbol ">:") 'Wrong 'type 'in 'arg2 'D) (fnc-mayor '(4 3 2 D 1))))
    )
  )

(deftest fnc-mayor-o-igual-test
  (testing "Se manda una lista en orden decreciente"
    (is (= (symbol "#t") (fnc-mayor-o-igual '())))
    (is (= (symbol "#t") (fnc-mayor-o-igual '(1))))
    (is (= (symbol "#t") (fnc-mayor-o-igual '(5))))
    (is (= (symbol "#t") (fnc-mayor-o-igual '(0 0 0 0 0 0))))
    (is (= (symbol "#t") (fnc-mayor-o-igual '(2 2 2 2 1))))
    (is (= (symbol "#t") (fnc-mayor-o-igual '(3 3 3 2 2 1 1))))
    (is (= (symbol "#t") (fnc-mayor-o-igual '(18 17 17 15 13 11 10 9 9 7 5 3 2 1 0 -3 -6 -6))))
    )
  (testing "Se manda una lista desordenada (no decreciente)"
    (is (= (symbol "#f") (fnc-mayor-o-igual '(2 2 1 1 2))))
    (is (= (symbol "#f") (fnc-mayor-o-igual '(1 2 3 4 5 4 3 2 1))))
    (is (= (symbol "#f") (fnc-mayor-o-igual '(100 99 98 98 98 99 100))))
    )
  (testing "Se manda una lista con argumentos no validos"
    (is (= (list (symbol ";ERROR:") (symbol ">=:") 'Wrong 'type 'in 'arg1 'A) (fnc-mayor-o-igual '(A))))
    (is (= (list (symbol ";ERROR:") (symbol ">=:") 'Wrong 'type 'in 'arg2 'B) (fnc-mayor-o-igual '(2 B))))
    (is (= (list (symbol ";ERROR:") (symbol ">=:") 'Wrong 'type 'in 'arg2 'D) (fnc-mayor-o-igual '(60 D 58))))
    (is (= (list (symbol ";ERROR:") (symbol ">=:") 'Wrong 'type 'in 'arg2 'D) (fnc-mayor-o-igual '(4 3 2 D D 1))))
    )
  )


(deftest evaluar-escalar-test
  (testing "Busqueda con literales"
    (is (= '(5 (a 1 b 2 c 3 d 4)) (evaluar-escalar 5 '(a 1 b 2 c 3 d 4))))
    (is (= '("hola" (a 1 b 2 c 3 d 4)) (evaluar-escalar "hola" '(a 1 b 2 c 3 d 4))))
    (is (= '(1 (a 1 b 2 c 3 d 4)) (evaluar-escalar 1 '(a 1 b 2 c 3 d 4))))
    )
  (testing "Busqueda en el ambiente"
    (is (= '(3 (a 1 b 2 c 3 d 4)) (evaluar-escalar 'c '(a 1 b 2 c 3 d 4))))
    (is (= '("hola" (a 1 b "hola" c 3 d 4)) (evaluar-escalar 'b '(a 1 b "hola" c 3 d 4))))
    (is (= '(7 (a 1 b 2 c 3 d 4 e 7)) (evaluar-escalar 'e '(a 1 b 2 c 3 d 4 e 7))))
    )
  (testing "No se encuentra en el ambiente"
    (is (= (list (list (symbol ";ERROR:") (symbol "unbound variable:") 'c) '(a 1 b 2 d 4)) (evaluar-escalar 'c '(a 1 b 2 d 4))))
    (is (= (list (list (symbol ";ERROR:") (symbol "unbound variable:") 'c) '()) (evaluar-escalar 'c '())))
    )
  )

(deftest evaluar-define-test
  (testing "Casos correctos"
    (is (= (list (symbol "#<unspecified>") '(x 2))
           (evaluar-define '(define x 2) '(x 1))))
    (is (= (list (symbol "#<unspecified>") '(x 1 f (lambda (x) (+ x 1))))
           (evaluar-define '(define (f x) (+ x 1)) '(x 1))))
    (is (= (list (symbol "#<unspecified>") '(x 1 f (lambda (x y) (+ x y))))
           (evaluar-define '(define (f x y) (+ x y)) '(x 1))))
    (is (= (list (symbol "#<unspecified>") '(x 1 f (lambda (x) (display x) (newline) (+ x 1))))
           (evaluar-define '(define (f x) (display x) (newline) (+ x 1)) '(x 1))))
    (is (= (list (symbol "#<unspecified>") '(x x))
           (evaluar-define '(define x 'x) '())))
    )
  (testing "Error de faltante o extra en expresion"
    (is (= (list (list (symbol ";ERROR:") (symbol  (str "define:")) 'missing 'or 'extra 'expression '(define)) '(x 1))
           (evaluar-define '(define) '(x 1))))
    (is (= (list (list (symbol ";ERROR:") (symbol  (str "define:")) 'missing 'or 'extra 'expression '(define x)) '(x 1))
           (evaluar-define '(define x) '(x 1))))
    (is (= (list (list (symbol ";ERROR:") (symbol  (str "define:")) 'missing 'or 'extra 'expression '(define x 2 3)) '(x 1))
           (evaluar-define '(define x 2 3) '(x 1))))
    (is (= (list (list (symbol ";ERROR:") (symbol  (str "define:")) 'missing 'or 'extra 'expression '(define ())) '(x 1))
           (evaluar-define '(define ()) '(x 1))))
    )
  (testing "Error de bad variable"
    (is (= (list (list (symbol ";ERROR:") (symbol  (str "define:")) 'bad 'variable '(define () 2)) '(x 1)) (evaluar-define '(define () 2) '(x 1))))
    (is (= (list (list (symbol ";ERROR:") (symbol  (str "define:")) 'bad 'variable '(define 2 x)) '(x 1)) (evaluar-define '(define 2 x) '(x 1))))
    )
  )

(deftest evaluar-set!-test
  (testing "Casos correctos"
    (is (= (list (symbol "#<unspecified>") '(x 1)) (evaluar-set! '(set! x 1) '(x 0))))
    (is (= (list (symbol "#<unspecified>") '(x 2 + +)) (evaluar-set! '(set! x (+ 1 1)) '(x 0 + +))))
    (is (= (list (symbol "#<unspecified>") '(x 0 y 3 z 3)) (evaluar-set! '(set! y 3) '(x 0 y 1 z 3))))
    )
  (testing "Error de faltante o extra en expresion"
    (is (= (list (list (symbol ";ERROR:") (symbol  (str "set!:")) 'missing 'or 'extra 'expression '(set!)) '(x 1))
           (evaluar-set! '(set!) '(x 1))))
    (is (= (list (list (symbol ";ERROR:") (symbol  (str "set!:")) 'missing 'or 'extra 'expression '(set! x)) '(x 1))
           (evaluar-set! '(set! x) '(x 1))))
    (is (= (list (list (symbol ";ERROR:") (symbol  (str "set!:")) 'missing 'or 'extra 'expression '(set! x 2 3)) '(x 1))
           (evaluar-set! '(set! x 2 3) '(x 1))))
    )
  (testing "Error de unbound variable"
    (is (= (list (list (symbol ";ERROR:") (symbol "unbound variable:") 'y) '(x 1)) (evaluar-set! '(set! y 2) '(x 1))))
    (is (= (list (list (symbol ";ERROR:") (symbol "unbound variable:") 'y) '(x 1 z 5)) (evaluar-set! '(set! y 3) '(x 1 z 5))))
    )
  (testing "Error de bad variable"
    (is (= (list (list (symbol ";ERROR:") (symbol  (str "set!:")) 'bad 'variable 1) '(x 0)) (evaluar-set! '(set! 1 2) '(x 0))))
    )
  )


(deftest evaluar-or-test
  (testing "Solo falsos"
    (is (= (list (symbol "#f") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
           (evaluar-or (list 'or) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))
    (is (= (list (symbol "#f") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
           (evaluar-or (list 'or (symbol "#f")) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))
    (is (= (list (symbol "#f") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
           (evaluar-or (list 'or (symbol "#f")) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))
    )
  (testing "Solo verdaderos"
    (is (= (list (symbol "#t") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
           (evaluar-or (list 'or (symbol "#t")) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))

    (is (= (list (symbol "#t") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
           (evaluar-or (list 'or (symbol "#t")) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))

    (is (= (list (symbol "#t") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
           (evaluar-or (list 'or (symbol "#t") (symbol "#t") (symbol "#t")) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))

    )
  (testing "Combinados valores de verdad"
    (is (= (list (symbol "#t") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
           (evaluar-or (list 'or (symbol "#t") (symbol "#f")) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))

    (is (= (list (symbol "#t") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
           (evaluar-or (list 'or (symbol "#f") (symbol "#t")) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))

    (is (= (list (symbol "#t") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
           (evaluar-or (list 'or (symbol "#f") (symbol "#t") (symbol "#f")) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))

    (is (= (list (symbol "#t") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
       (evaluar-or (list 'or (symbol "#f") (symbol "#f") (symbol "#t")) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))

    (is (= (list true (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t") '< '<) )
           (evaluar-or (list 'or (symbol "#f") (< 1 2)) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t") '< '<)) ))
    )
  (testing "Mezclados con numeros"
    (is (= (list (symbol "#t") (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
           (evaluar-or (list 'or (symbol "#t") 3) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))

    (is (= (list 2 (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
           (evaluar-or (list 'or (symbol "#f") 2) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))

    (is (= (list 5 (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t")) )
           (evaluar-or (list 'or 5 (symbol "#t") (symbol "#f")) (list (symbol "#f") (symbol "#f") (symbol "#t") (symbol "#t"))) ))
    )
  )

(deftest evaluar-if-test
  (let [T (symbol "#t"), F (symbol "#f")]
    (testing "Casos verdaderos"
      (is (= (list 2 (list 'n 7 T T F F)) (evaluar-if '(if 1 2) (list 'n 7 T T F F))))
      (is (= (list 7 (list 'n 7 T T F F)) (evaluar-if '(if 1 n) (list 'n 7 T T F F))))
      (is (= (list 7 (list 'n 7 T T F F)) (evaluar-if '(if 1 n 8) (list 'n 7 T T F F))))
      (is (= (list 7 (list 'n 7 T T F F)) (evaluar-if (list 'if (symbol "#t") 'n 8) (list 'n 7 T T F F))))
      (is (= (list 7 (list 'n 7 T T F F)) (evaluar-if (list 'if (symbol "#T") 'n 8) (list 'n 7 T T F F))))
      (is (= (list (symbol "#<unspecified>") (list 'n 8 T T F F)) (evaluar-if (list 'if (symbol "#T") '(set! n 8) 1) (list 'n 7 T T F F))))
      (is (= (list 2 (list '+ '+ T T F F)) (evaluar-if '(if 1 (+ 1 1) 8) (list '+ '+ T T F F))))
      )
    (testing "Casos falsos"
      (is (= (list (symbol "#<unspecified>")  (list 'n 7 T T F F))
             (evaluar-if (list 'if (symbol "#f") 2) (list 'n 7 T T F F))))
      (is (= (list (symbol "#<unspecified>") (list 'n 7 T T F F))
             (evaluar-if (list 'if (symbol "#f") 'n) (list 'n 7 T T F F))))
      (is (= (list 8 (list 'n 7 T T F F))
             (evaluar-if (list 'if (symbol "#f") 'n 8) (list 'n 7 T T F F))))
      (is (= (list 1 (list 'n 7 'y 1 T T F F))
             (evaluar-if (list 'if (symbol "#f") 'n 'y) (list 'n 7 'y 1 T T F F))))
      (is (= (list (symbol "#<unspecified>") (list 'n 7 T T F F 'f 3))
             (evaluar-if (list 'if (symbol "#F") 'n '(define f 3)) (list 'n 7 T T F F))))
      (is (= (list (symbol "#<unspecified>") (list 'n 8 T T F F))
             (evaluar-if (list 'if (symbol "#f") 1 '(set! n 8)) (list 'n 7 T T F F))))
      (is (= (list 4 (list '+ '+ T T F F))
             (evaluar-if (list 'if (symbol "#f") 2 (+ 1 3)) (list '+ '+ T T F F))))
      )
    )
  )

(deftest leer-entrada-test
  (testing "Lectura de entrada"
    (is (= "Hola" (with-in-str "Hola" (leer-entrada))))
    (is (= "(Hola mundo)" (with-in-str "(Hola\nmundo)" (leer-entrada))))
    (is (= "( prueba( con( varios niveles) ))" (with-in-str "(\nprueba(\ncon(\nvarios\nniveles)\n))" (leer-entrada))))
    )
  )


(deftest fnc-read-test
  (testing "Lectura de elementos de scheme"
    (is (= 'Hola (with-in-str "Hola" (fnc-read))))
    (is (= '(Hola mundo) (with-in-str "(Hola\nmundo)" (fnc-read))))
    (is (= 5 (with-in-str "5" (fnc-read))))
    (is (= '(+ 1 1) (with-in-str "(+ 1 1)" (fnc-read))))
    (is (= (symbol "#t") (with-in-str "#t" (fnc-read))))
    (is (= (symbol "#T") (with-in-str "#T" (fnc-read))))
    (is (= (symbol "#f") (with-in-str "#f" (fnc-read))))
    (is (= (symbol "#F") (with-in-str "#F" (fnc-read))))
    (is (= (list (symbol "#t") (list (symbol "#f"))) (with-in-str "(#t (#f))" (fnc-read))))
    (is (= "#t #f #F #T" (with-in-str "\"#t #f #F #T\"" (fnc-read))))
    )
  )