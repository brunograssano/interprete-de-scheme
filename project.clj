(defproject interprete-de-scheme "0.1.0-SNAPSHOT"
  :description "Interprete de Scheme - Lenguajes Formales 75.14 FIUBA - 2C2021"
  :url "https://github.com/brunograssano/interprete-de-scheme"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :main ^:skip-aot interprete-de-scheme.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
