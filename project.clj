(defproject xtreak/clj-wordcloud "0.0.2-SNAPSHOT"
  :description "A simple clojure wrapper around kumo for wordcloud generation"
  :url "http://github.com/tirkarthi/clj-wordcloud"
  :license {:name "MIT public license"
            :url "http://opensource.org/licenses/mit-license.php"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.kennycason/kumo-core "1.13"]]
  :plugins [[lein-codox "0.10.3"]]
  ;; TODO : Add source uri and keep them in sync for docs
  :codox {:output-path "docs"})
