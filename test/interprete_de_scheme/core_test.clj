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