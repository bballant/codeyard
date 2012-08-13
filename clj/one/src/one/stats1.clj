(ns one.stats1)

(use '(incanter core stats charts io))

; load the crss dataset
(def crss-data (read-dataset "data/crss.csv" :header true))

; info about dataset
(dim crss-data)
(col-names crss-data)

; graph number of business vs avg val per member
(def bz-vs-mval
  (bar-chart
    ($ :num-of-bz crss-data)
    ($ :avg-per-m crss-data)
    :x-label "number of businesses"
    :y-label "avg val per member"))

; graph number of businesses vs aov
(def bz-vs-aov
  (line-chart
    ($ :num-of-bz crss-data)
    ($ :avg-or crss-data)
    :x-label "number of businesses"
    :y-label "avg order val"))

; a pie chart of the number of members per biz
(def m-by-nbiz
  (pie-chart
    ($ :num-of-bz crss-data)
    ($ :unique-m crss-data)
    :title "shoppers by num of biz"
    :legend true))
