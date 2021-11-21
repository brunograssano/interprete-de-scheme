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
