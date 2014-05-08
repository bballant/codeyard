(ns one.presentation)

; clojure
                                        ;              xxxxxxxxxxx               
                                        ;           xxxxxxxxxxxxxxxxx            
                                        ;         xxxxxxx      xxxxxxxx          
                                        ;       xxxx                 xxxx
(def lambda "λ")                        ;      xxxx                   xxxx       
(def title "Clojure")                   ;    xxxxx     xxxxx            xxxx     
(def pronounced "Pronounced: Clozhure") ;    xxx       xxxxxx           xxxx     
(def i-admit                            ;   xxxx           xxx            xxx    
  "I'm not sure how it's pronounced")   ;   xxx            xxx            xxx    
                                        ;  xxxx           xxxxx            xxx   
                                        ;  xxx           xxxxxxx           xxx   
                                        ;  xxx          xxx  xxx           xxx   
                                        ;  xxx         xxxx   xxx          xxx   
                                        ;  xxx        xxx      xx          xxx   
                                        ;   xxxx     xxxx      xxx  x    xxxx    
                                        ;   xxxx    xxxx        xxxxx    xxx     
                                        ;     xxxx  xxx         xx     xxxx      
                                        ;     xxxxx                   xxxxx      
                                        ;      xxxxxx               xxxxxx       
                                        ;        xxxxxxx         xxxxxxx         
                                        ;            xxxxxxxxxxxxxxxx            
                                        ;             xxxxxxxxxxxx       


; --------------------------------------------------------------------------------

(def is-closure
  "the best programming language ever invented?")














; --------------------------------------------------------------------------------

(def answer "Yes")

               _____
;       _..--'''@   @'''--.._
;     .'   @_/-//-\/>/>'/ @  '.
;    (  @  /_<//<'/----------^-)
;    |'._  @     //|###########|
;    |~  ''--..@|',|}}}}}}}}}}}|
;    |  ~   ~   |/ |###########|
;    | ~~  ~   ~|./|{{{{{{{{{{{|
;     '._ ~ ~ ~ |,/`````````````
;        ''--.~.|/










; --------------------------------------------------------------------------------


                                   ;         .   *   ..  . *  *
(def more-about-clojure            ;       *  * @()Ooc()*   o  .
  ["Clojure is a LISP"             ;           (Q@*0CG*O()  ___
    "Runs on the JVM!"             ;          |\_________/|/ _ \
    "Functional"                   ;          |  |  |  |  | / | |
    "Opinionated"                  ;          |  |  |  |  | | | |
    "Simple",                      ;          |  |  |  |  | | | |
    "Beautiful"])                  ;          |  |  |  |  | | | |
                                   ;          |  |  |  |  | | | |
                                   ;          |  |  |  |  | \_| |
                                   ;          |  |  |  |  |\___/            
                                   ;          |\_|__|__|_/|
                                   ;           \_________/







; --------------------------------------------------------------------------------
       
                                                 ;           ;M";::;; 
                                                 ;          ,':;: ""'. 
                                                 ;         ,M;. ;MM;;M: 
(def a-list-of-scalars                           ;         ;MMM::MMMMM: 
  '(1 "hey" 2 :yeah 3 \B "car door"))            ;        ,MMMMM'MMMMM: 
                                                 ;        ;MMMMM MMMMMM 
; the ' tells clojure not to evaluate the list   ;        MMMMM::MMMMMM: 
                                                 ;        :MM:',;MMMMMM' 
(def a-vector-of-lists                           ;        ':: 'MMMMMMM: 
  ['(1 2 3) '(4 5 6) '("hey" 2)])                ;          '; :MMMMM" 
                                                 ;             ''"""' 
(def a-map                                       ;              . 
  {1 "one", 2 "two", 3 "three"})                 ;              M 
                                                 ;              M 
; user=> (a-map "one")                           ;  .           M           . 
                                                 ;  'M..        M        ,;M' 
(def a-set                                       ;   'MM;.      M       ;MM: 
  #{1 2 "three" :four 0x5})                      ;    :MMM.     M      ;MM: 
                                                 ;    'MMM;     M     :MMM: 
                                                 ;     MMMM.    M     MMMM: 
                                                 ;    :MMMM:    M     MMMM: 
                                                 ;    :MMMM:    M    :MMMM: 
; --------------------------------------------------------------------------------
       
(defn a-function-that-adds-2 [x y]
  (+ x y))

; user=> (a-function-that-adds-2 3 5)
; 8

;                                          _____________________
;                                         |  _________________  |
; * prefix notation                       | | JO           0. | |
; * simpler, less ambiguous               | |_________________| |
;   * i.e. order of ops                   |  ___ ___ ___   ___  |
;                                         | | 7 | 8 | 9 | | + | |
;                                         | |___|___|___| |___| |
;                                         | | 4 | 5 | 6 | | - | |
;                                         | |___|___|___| |___| |
;                                         | | 1 | 2 | 3 | | x | |
;                                         | |___|___|___| |___| |
;                                         | | . | 0 | = | | / | |
;                                         | |___|___|___| |___| |
;                                         |_____________________|

; --------------------------------------------------------------------------------

(defn <3 [who what]
  (clojure.string/join " " [who "loves" what]))

; one.core=> (<3 "brian" "jacqueline")
; "brian loves jacqueline"

;          ******       ******
;        **********   **********
;      ************* *************
;     *****************************
;     *****************************
;     *****************************
;      ***************************
;        ***********************
;          *******************
;            ***************
;              ***********
;                *******
;                  ***
;                   *

; --------------------------------------------------------------------------------

; anonymous functions
(fn [x y] #{x y})

; assign it to a name
(def make-set
  (fn [x y] #{x y}))

; shortcut
(defn make-set [x y] #{x y})

; multiple argument lists
(defn make-set23
  ([x y] #{x y})
  ([x y z] #{x y z}))







 
; --------------------------------------------------------------------------------

; * user recur to loop ((tail) recursive!)

(defn sum-down-from [sum x]
  (if (pos? x)
    (recur (+ sum x) (dec x))
    sum))

;
; loop marks the spot to jump back to

(defn sum-down-from [initial-x]
  (loop [sum 0, x initial-x]
    (if (pos? x)
      (recur (+ sum x) (dec x))
      sum)))






; --------------------------------------------------------------------------------
; Java

; static
(Math/sqrt 9)
;
; new Java thing
(new java.util.HashMap {"foo" 42 "bar" 9 "baz" "quux"})
;=> #<HashMap {baz=quux, foo=42, bar=9}>

; accessing java members w/ . operator
(.divide (java.math.BigDecimal. "42") 2M)
;=> 21M
 
;                  ."`".
;              .-./ _=_ \.-.
;             {  (,(oYo),) }}
;             {{ |   "   |} }
;             { { \(---)/  }}
;             {{  }'-=-'{ } }
;             {{  } -:- { } }
;             {_{ }`===`{  _}
;            ((((\)     (/)))) 

; --------------------------------------------------------------------------------

; macros
; * write code that writes code!

(defmacro do-until [& clauses]
  (when clauses
    (list `when (first clauses)
          (if (next clauses)
            (second clauses)
            (throw (IllegalArgumentException.
                     "do-until requires an even number of forms")))
            (cons 'do-until (nnext clauses)))))

     _  _  _
    {o}{o}{o}
     |  |  |
    \|/\|/\|/
   [~~~~~~~~~]
    |~~~~~~~|
    |_______| 

; --------------------------------------------------------------------------------
; 
; * something cool
;
; incanter: http://incanter.org/
; (use '(incanter core stats charts io))
; (use '(one stats1))
; (dim crss-data)
; (view bz-vs-mval)
;
;
;              (`.         ,-,
;              `\ `.    ,;' /
;               \`. \ ,'/ .'
;         __     `.\ Y /.'
;      .-'  ''--.._` ` (
;    .'            /   `
;   ,           ` '   Q '
;   ,         ,   `._    \
;   |         '     `-.;_'
;   `  ;    `  ` --,.._;
;   `    ,   )   .'
;    `._ ,  '   /_
;       ; ,''-,;' ``-
;        ``-..__\``--`   Thanks!
;
;

; http://www.4clojure.com/
