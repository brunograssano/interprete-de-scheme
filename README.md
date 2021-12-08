# Interprete de scheme

Trabajo practico de la materia Lenguajes Formales (75.14) que consistio en el desarrollo de un interprete del
lenguaje Scheme en Clojure.


## Introducción Scheme <img title="scheme logo" alt="scheme logo" height="30px" src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/Lambda_lc.svg/182px-Lambda_lc.svg.png">



>Scheme es un dialecto minimalista de la familia de lenguajes de programación de Lisp, creado 
durante la década de 1970 en el MIT AI Lab y publicado por sus desarrolladores, Guy L. Steele y 
Gerald J. Sussman, a través de una serie de memorandos ahora conocidos como Lambda Papers. 
Fue el primer dialecto de Lisp en utilizar variables de ámbito léxico (lexically scoped).
>
>Existen dos estándares que definen este lenguaje de programación: el oficial del IEEE y un estándar 
de facto conocido como Revised n-th Report on the Algorithmic Language Scheme, abreviado como 
RnRS, donde n es el número de la revisión. El más reciente es el R7RS, que fue publicado en 2013.
>
>La filosofía minimalista de estos estándares conlleva ventajas e inconvenientes. Por ejemplo, escribir 
un intérprete completo de Scheme es más fácil que implementar uno de Common Lisp; empotrar Lisp 
en dispositivos con poca memoria también es más factible usando Scheme en lugar de Common Lisp. 
A los aficionados a Scheme les divierte mucho señalar que su estándar, de alrededor de 50 páginas, 
es más corto que el índice del libro Common Lisp: The Language, de Guy L. Steele.
>
>Scheme tiene una base de usuarios diversa debido a su estructura compacta y su elegancia. Sin 
embargo, también hay una gran divergencia entre las implementaciones prácticas, tanto que el 
Scheme Steering Committee lo ha llamado "el lenguaje de programación menos portable del mundo" 
(the world's most unportable programming language) y lo considera "una familia de dialectos", en 
lugar de un lenguaje único.

## Enunciado

El objetivo del presente trabajo es construir un intérprete de Scheme que corra en la JVM
(Java Virtual Machine). Por ello, el lenguaje elegido para su implementación es Clojure.

Características del interprete a implementar:

##### Valores de verdad
* \#f: Valor falso, nil es un sinónimo
* \#t: Valor verdadero

##### Formas especiales o magic forms
* cond: evalúa múltiples condiciones
* define: define variables o funciones y las liga al símbolo
* exit: sale del interprete
* if: evalúa una condición
* lambda: define una función anónima
* load: carga un archivo al interprete
* or: evalúa expresiones mientras que no obtenga falso (#f)
* quote: impide la evaluación
* set!: redefine un símbolo


##### Funciones
* \+: retorna la suma de los argumentos.
* \-: retorna la resta de los argumentos.
* <: retorna verdadero si los números están ordenados.
* \>: retorna verdadero si los números están ordenados.
* \>=: retorna verdadero si los números están ordenados.
* append: fusiona las listas dadas.
* car: da el elemento en la primer posición de una lista.
* cdr: da la lista sin el elemento en la primera posición.
* cons: inserta en la cabeza de una lista.
* display: imprime por pantalla.
* env: retorna el ambiente actual. (No esta disponible en Scheme)
* equal?: retorna verdadero si los elementos son iguales. Scheme es CASE-INSENSITIVE
* eval: retorna la evaluacion de una lista.
* length: retorna la longitud de una lista.
* list: crea una lista formada por los argumentos.
* list?: retorna verdadero si el argumento es una lista.
* newline: imprime un salto de linea.
* not: niega un valor de verdad.
* null?: retorna verdadero si un elemento es ().
* read: lee la lectura de un elemento.
* reverse: retorna la lista invertida

### Condiciones adicionales
* Se busca que se comporte de manera similar a SCM version 5f2, (C) 1990-2006 FSF.
* Se entregan los archivos `jarras.scm`,`breadth.scm` y `demo.scm` que deben ser interpretados correctamente.

Con este trabajo práctico, se espera que las/los estudiantes adquieran conocimientos profundos 
sobre el proceso de interpretación de programas y el funcionamiento de los intérpretes de 
lenguajes de programación y que, a la vez, pongan en práctica los conceptos del paradigma de 
Programación Funcional vistos durante el cuatrimestre.

## Instalación

Para poder trabajar más cómodamente con el proyecto es necesario utilizar [Leiningen](https://leiningen.org/).

## Uso

El interprete se puede iniciar de las siguientes formas:
* `lein run` para iniciar el interprete a través de Leinigen
* En un REPL llamando a `(load-file "scheme.clj")` y luego a `(repl)`
* Si se quiere correr a través de un `.jar` primero llamar a `lein uberjar` y luego `java -jar nombre-del-jar-generado.jar`

## Pruebas

Se realizaron varias pruebas unitarias de las diferentes funciones implementadas. 
Estas se pueden correr con `lein test`.

Respecto de los archivos entregados inicialmente, los mismos se pueden cargar en el interprete como sigue:
* `(load "demo")`: carga la demo general del interprete. En el enunciado `pdf` se puede ver la salida esperada.
* `(load "jarras")`: carga el programa del problema de las jarras de agua. Se inicia con `(breadth-first bc)` y
 se espera que del estado inicial `(0 0)` se llegue al final `(4 0)` como sigue:
 `((0 0) (5 0) (0 5) (5 5) (2 8) (2 0) (0 2) (5 2) (0 7) (5 7) (4 8) (4 0))` (11 pasos)

## Ejemplos adicionales

Se agregan dos ejemplos adicionales para ejecutar en Scheme.
* `(load "fibonacci")`: calcula números de fibonacci.
* `(load "sublista")`: obtiene la sublista dada una lista, una posición inicial y una longitud.


## Licencia

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
