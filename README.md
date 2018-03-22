# clj-wordcloud [![Clojars Project](https://img.shields.io/clojars/v/xtreak/clj-wordcloud.svg)](https://clojars.org/xtreak/clj-wordcloud) [![Build Status](https://travis-ci.org/tirkarthi/clj-wordcloud.svg?branch=master)](https://travis-ci.org/tirkarthi/clj-wordcloud)

A simple clojure wrapper around kumo to generate wordcloud

## Leiningen

`[xtreak/clj-wordcloud "0.0.1"]`

## Example

Given a map of element and the frequency the following image is generated. More examples/examples.clj.

### Circle

```clojure
(ns examples
  (:require [clj-wordcloud.core :refer :all]))

(let [frequency-map (zipmap (random-words 100) (shuffle (range 300)))
      word-cloud (word-cloud frequency-map
                             {:dimension  {:width  600
                                           :height 600}
                              :background {:type :circle
                                           :size 300
                                           :color "0x000000"}
                              :font       {:type       "Calibre"
                                           :weight     :plain
                                           :scale-type :linear
                                           :x-scale    20
                                           :y-scale    20
                                           :padding    5}})]
  (write-to-file word-cloud "example_circle.png"))
```

![Sample](/examples/example_circle.png)

### Bitmaps

```clojure
(ns examples
  (:require [clj-wordcloud.core :refer :all]))

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
                                              :padding    5}})]
  (write-to-file word-cloud "example_haskell.png"))
```

![Sample](/examples/example_haskell.png)

## TODO

* API parity with Kumo
* clojure.spec for docs and validation
* Cool examples
* Better tests

## Contributing

Contributions are welcome. Please refer to CONTRIBUTING.md.

## Stability

This library is still in early development phase and the API is subject to change. API design comments are welcome.

## Thanks

Thanks to @kennycason for [kumo](https://github.com/kennycason/kumo) without which this library is not possible

## License

Copyright © 2018 Karthikeyan S

Distributed under the MIT License
