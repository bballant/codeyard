(ns ml4hackers.core)

(use '(incanter core stats io))

(def heights-weights
  (read-dataset "data/01_heights_weights_genders.csv" :header true))

(summary heights-weights)
(col-names heights-weights)
(view heights-weights)  

; just get heights into dataset
(def heights
  (dataset '(:height) ($ :Height heights-weights)))

(summary heights)
(quantile ($ :height heights))

(def h ($ :height heights))

(defn var1ance [h]
  (sum
    (let
      [x (mean h)
       c (count h)]
      (map #(/ (expt (- % x) 2) c) h))))
