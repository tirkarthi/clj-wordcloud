# clj-wordcloud [![Build Status](https://travis-ci.org/tirkarthi/clj-wordcloud.svg?branch=master)](https://travis-ci.org/tirkarthi/clj-wordcloud)

A simple clojure wrapper around kumo to generate wordcloud

## Example

Given a map of element and the frequency the following image is generated. More examples/examples.clj.

```clojure
(let [frequency-map (zipmap (range 100 130) (range 300))]
  (word-cloud frequency-map
              {:background {:type :circle :size 300}
               :filename "sample.png" :font-y 100 :padding 10}))
```

![Sample](/examples/sample.png)

## Stability

This library is still in early development phase and the API is subject to change.

## Thanks

Thanks to @kennycason for [kumo](https://github.com/kennycason/kumo) without which this library is not possible

## License

Copyright © 2018 Karthikeyan S

Distributed under the MIT License
