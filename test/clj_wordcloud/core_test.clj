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
    (let [word-cloud (word-cloud (zipmap (range 100 130) (range 300))
                                 {:background {:type :circle :size 300}
                                  :filename "sample.png" :font-y 100 :padding 10})]
      (is (= (get-size "sample.png") [600 600])))))
