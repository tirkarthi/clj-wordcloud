(ns clj-wordcloud.core
  (:import (java.awt Dimension Color)
           (java.awt.image BufferedImage)
           (javax.imageio ImageIO)
           (com.kennycason.kumo WordCloud WordFrequency LayeredWordCloud CollisionMode)
           (com.kennycason.kumo.bg CircleBackground RectangleBackground PixelBoundryBackground
                                   PixelBoundryBackground)
           (com.kennycason.kumo.palette ColorPalette)
           (com.kennycason.kumo.font KumoFont FontWeight)
           (com.kennycason.kumo.font.scale LinearFontScalar)
           (com.kennycason.kumo.nlp FrequencyAnalyzer)))


(defn- background-object
  [options dimension]
  (let [type   (get-in options [:background :type] :circle)
        size   (get-in options [:background :size] 20)
        bitmap (get-in options [:background :bitmap])]
    (case type
      :circle
      (CircleBackground. size)
      :rectangle
      (RectangleBackground. dimension)
      :pixel
      (PixelBoundryBackground. (clojure.java.io/input-stream bitmap))
      (CircleBackground. size))))


(defn- build-word-frequency
  [frequency-map]
  (doto (java.util.ArrayList.)
    (#(doseq [[word count] frequency-map]
        (.add %1 (WordFrequency. (str word) count))))))


(defn- build-font-scalar
  [options]
  (let [scale-type (get-in options [:font :scale-type] :linear)
        x-scale    (get-in options [:font :x-scale] 10)
        y-scale    (get-in options [:font :y-scale] 10)]
    (case scale-type
      :linear
      (LinearFontScalar. x-scale y-scale))))


(defn- build-font
  [options]
  (let [font-map    {:plain FontWeight/PLAIN, :bold FontWeight/BOLD, :italic FontWeight/ITALIC}
        font-type   (get-in options [:font :type] "Arial")
        font-weight (get-in options [:font :weight] :plain)]
    (KumoFont. font-type (get font-map font-weight FontWeight/PLAIN))))


(defn- build-dimension
  [options]
  (let [width  (get-in options [:dimension :width] 100)
        height (get-in options [:dimension :height] 100)]
    (Dimension. width height)))


(defn- build-color-palette
  [options]
  (let [default-colors ["0xFFFF00" "0x008000" "0x0000FF"]
        colors         (->> default-colors
                            (concat (get-in options [:font :colors] []))
                            (take 5))
        color-palette  (map #(Color/decode %1) colors)]
    (ColorPalette. color-palette)))


(defn write-to-file
  "Writes the word cloud object as png image to the given location.
  Supply file name with extension png like example.png"

  [word-cloud filename]
  (.writeToFile word-cloud filename))


(defn word-cloud
  "
  Takes a map of string and the score along with options to return a wordcloud object
  Options spec as below :

  {:dimension  {:width  600              ; width of the image
                :height 600}             ; height of the image
   :background {:type :circle            ; type. Takes :circle, :pixel (bitmaps), :rectangle
                :size 300                ; size of the circle
                :color \"0x000000\"}     ; Background color as hex
    :font       {:type       \"Calibre\" ; Font type
                 :weight     :plain      ; Font weight. Takes :plain, :bold, :italic
                 :scale-type :linear     ; linear for now
                 :x-scale    20          ; x scale of font
                 :y-scale    20          ; y scale of font
                 :padding    5}}         ; padding between entries
  "
  [frequency-map options]
  (let [word-frequencies (build-word-frequency frequency-map)
        dimension        (build-dimension options)
        word-cloud       (WordCloud. dimension CollisionMode/PIXEL_PERFECT)
        background       (background-object options dimension)
        font-scalar      (build-font-scalar options)
        kumo-font        (build-font options)
        color-palette    (build-color-palette options)]
    (doto word-cloud
      (.setPadding (get-in options [:font :padding] 10))
      (.setBackgroundColor (Color/decode (get-in options [:background :color] "0x000000")))
      (.setColorPalette color-palette)
      (.setBackground background)
      (.setFontScalar font-scalar)
      (.setKumoFont kumo-font)
      (.build word-frequencies))))
