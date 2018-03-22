(ns clj-wordcloud.core-test
  (:require [clojure.test :refer :all]
            [clj-wordcloud.core :refer :all]))


(defn get-size
  [file]
  (with-open [r (java.io.FileInputStream. file)]
    (let [image (javax.imageio.ImageIO/read r)]
      [(.getWidth image) (.getHeight image)])))


(deftest image-dimensions-test
  (testing "Test width and height of the generated image"
    (let [freq-map   (zipmap (range 100 130) (range 300))
          word-cloud (word-cloud freq-map
                                 {:dimension  {:width  600
                                               :height 600}
                                  :background {:type  :circle
                                               :size  300
                                               :color "0x000000"}
                                  :font       {:type       "Comic Sans MS"
                                               :scale-type :linear
                                               :x-scale    20
                                               :y-scale    20
                                               :padding    5
                                               :colors     ["0x00FF00" "0x0000FF" "0xFFAFFF"
                                                            "0xFFEEFF" "0xEEEEEE"]}})]
      (write-to-file word-cloud "sample.png")
      (is (= (get-size "sample.png") [600 600])))))
