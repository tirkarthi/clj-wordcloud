(ns examples
  (:require '[clj-wordcloud.core :refer :all]))

(let [frequency-map (zipmap (range 100 130) (range 300))]
  (word-cloud frequency-map
              {:background {:type :circle :size 300}
               :filename "sample.png" :font-y 100 :padding 10}))
