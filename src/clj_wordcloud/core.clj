(ns clj-wordcloud.core
  (:require [clojure.spec.alpha :as spec])
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

; Frequency-map specs

(spec/def ::frequency-map (spec/map-of string? (spec/and int? #(>= %1 0))))

; Dimension specs
(spec/def ::width (spec/and number? pos?))
(spec/def ::height (spec/and number? pos?))
(spec/def ::dimension (spec/keys :req-un [::width ::height]))

; background specs
(spec/def :circle/type #{:circle})
(spec/def :rectangle/type #{:rectangle})
(spec/def :pixel/type #{:pixel})
(spec/def ::size (spec/and number? pos?))
(spec/def ::bitmap string?)
(spec/def ::color (spec/and string? #(re-matches #"0x[0-9A-F]{1,6}" %1)))

(spec/def ::circle (spec/keys :req-un [:circle/type ::size] :opt-un [::color]))
(spec/def ::rectangle (spec/keys :req-un [:rectangle/type ::size] :opt-un [::color]))
(spec/def ::pixel (spec/keys :req-un [:pixel/type ::bitmap]))

(spec/def ::background (spec/or :circle ::circle :rectangle ::rectangle :pixel ::pixel))

; font specs
(spec/def :font/type string?)
(spec/def :font/weight #{:plain :bold :italic})
(spec/def :font/scale-type #{:linear})
(spec/def :font/x-scale (spec/and number? pos?))
(spec/def :font/y-scale (spec/and number? pos?))
(spec/def :font/padding (spec/and number? pos?))
(spec/def :font/colors (spec/coll-of ::color))

(spec/def ::font (spec/keys :opt-un [:font/type :font/weight :font/scale-type
                               :font/x-scale :font/y-scale :font/padding :font/colors]))

(spec/def ::options (spec/keys :req-un [::dimension ::background] :opt-un [::font]))

(spec/fdef word-cloud
           :args (spec/cat :frequency-map ::frequency-map :options ::options))

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
  ([frequency-map]
   (word-cloud frequency-map {}))
  ([frequency-map options]
   (if-not (spec/valid? ::frequency-map frequency-map)
     (throw (ex-info "Invalid data : " (spec/explain-data ::frequency-map frequency-map))))
   (if-not (spec/valid? ::options options)
     (throw (ex-info "Invalid options : " (spec/explain-data ::options options))))
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
      (.build word-frequencies)))))
