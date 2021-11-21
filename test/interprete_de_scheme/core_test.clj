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