(ns one.core)

(defn -main
  "I don't do a whole lot."
  [& args]
  (println "Hello, World!"))

;(use 'lamina.core 'aleph.tcp 'gloss.core)
; 
;(defn handler [ch client-info]
;  (receive-all ch
;    #(enqueue ch (str "You said " %))))
;
;(start-tcp-server handler {:port 10000, :frame (string :utf-8 :delimiters ["\r\n"])})
