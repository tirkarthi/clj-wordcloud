(ns examples
  (:require [clj-wordcloud.core :refer :all]))


(defn random-words
  [n]
  (->> "/usr/share/dict/words"
       slurp
       clojure.string/lower-case
       clojure.string/split-lines
       shuffle
       (take n)))


(let [frequency-map (zipmap (random-words 100) (shuffle (range 300)))
      word-cloud    (word-cloud frequency-map
                                {:dimension  {:width  600
                                              :height 600}
                                 :background {:type  :circle
                                              :size  300
                                              :color "0x000000"}
                                 :font       {:type       "Calibre"
                                              :weight     :plain
                                              :scale-type :linear
                                              :x-scale    20
                                              :y-scale    20
                                              :padding    5}})]
  (write-to-file word-cloud "example_circle.png"))


(let [frequency-map (zipmap (range 100 150) (shuffle (range 300)))
      word-cloud    (word-cloud frequency-map
                                {:dimension  {:width  600
                                              :height 600}
                                 :background {:type   :pixel
                                              :size   300
                                              :color  "0x000000"
                                              :bitmap "examples/backgrounds/haskell_1.bmp"}
                                 :font       {:type       "Calibre"
                                              :weight     :plain
                                              :scale-type :linear
                                              :x-scale    20
                                              :y-scale    20
                                              :padding    5
                                              :colors     ["0x00FF00" "0x0000FF" "0xFFAFFF"
                                                           "0xFFEEFF" "0xEEEEEE"]}})]
  (write-to-file word-cloud "example_haskell.png"))
