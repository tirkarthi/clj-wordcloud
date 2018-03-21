(ns clj-wordcloud.core
  (:import (com.kennycason.kumo WordCloud WordFrequency)
           (java.awt Dimension)
           (com.kennycason.kumo WordCloud WordFrequency CollisionMode)
           (com.kennycason.kumo.bg CircleBackground RectangleBackground)
           (com.kennycason.kumo.font.scale LinearFontScalar)
           (com.kennycason.kumo.nlp FrequencyAnalyzer)))


(defn- background-object
  [options dimension]
  (let [type (get-in options [:background :type] :circle)
        size (get-in options [:background :size] 20)]
    (case type
      :circle
      (CircleBackground. size)
      :rectangle
      (RectangleBackground. dimension))))


(defn- build-word-frequency
  [frequency-map]
  (doto (java.util.ArrayList.)
    (#(doseq [[word count] frequency-map]
        (.add %1 (WordFrequency. (str word) count))))))


(defn word-cloud
  [frequency-map options]
  (let [word-frequencies (build-word-frequency frequency-map)
        dimension        (Dimension. (:width options 600) (:height options 600))
        word-cloud       (WordCloud. dimension CollisionMode/PIXEL_PERFECT)
        background       (background-object options dimension)
        font             (LinearFontScalar. (:font-x options 10) (:font-y options 40))]
    (doto word-cloud
      (.setPadding (:padding options 10))
      (.setBackground background)
      (.setFontScalar font)
      (.build word-frequencies)
      (.writeToFile (:filename options "test.png")))))
