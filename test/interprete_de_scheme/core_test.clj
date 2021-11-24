(ns interprete-de-scheme.core-test
  (:require [clojure.test :refer :all]
            [interprete-de-scheme.core :refer :all]))

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


(deftest
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
    (is (= '(+ + - - * * f +) (actualizar-amb '(+ + - - * * f /) 'f '+)))
    )
  )

(deftest buscar-test
  (testing "Busqueda de claves en ambiente"
    (is (= 3 (buscar 'c '(a 1 b 2 c 3 d 4))))
    (is (= 1 (buscar 'a '(a 1 b 2 c 3 d 4))))
    (is (= 4 (buscar 'd '(a a b b c b d d))))
    )
  (testing "No se encuentra en el ambiente"
    (is (= (list (symbol ";ERROR:") (symbol "unbound") (symbol "variable:") 'c) (buscar 'c '(a 1 b 2 d 4))))
    (is (= (list (symbol ";ERROR:") (symbol "unbound") (symbol "variable:") 'c) (buscar 'c '())))
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
  (testing "Sin bool"
    (is (= "" (restaurar-bool "")))
    (is (= "no hay bool" (restaurar-bool "no hay bool")))
    (is (= "#y #h #g #b t# f#" (restaurar-bool "#y #h #g #b t# f#")))
    )
  (testing "Con bool"
    (is (= "(or #F #f #t #T)" (restaurar-bool "(or %F %f %t %T)")))
    (is (= "(and (or #F #f #t #T) #T)" (restaurar-bool "(and (or %F %f %t %T) %T)")))
    )
  )

(deftest igual?-test
  (testing "Igualdades"
    (is (true? (igual? 'if 'IF)))
    (is (true? (igual? 'if 'if)))
    (is (true? (igual? 'If 'iF)))
    (is (true? (igual? "A" "A")))
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