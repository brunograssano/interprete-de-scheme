(ns interprete-de-scheme.core
  (:gen-class)
  (:require [interprete-de-scheme.scheme :refer [repl]])
  )


(defn -main
  "Funcion principal del interprete de scheme"
  [& args]
  (repl)
  )
