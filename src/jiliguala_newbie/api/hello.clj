(ns jiliguala-newbie.api.hello
  (:require [clojure.tools.logging :as log]))

(defn greet
  ^String [{:keys [title name]}]
  (format "Hello, %s%s!"
          (if title
            (str title " ")
            "")
          name))

(defn plus
  ^Long [x y]
  {:result (+ x y)})

(def echo identity)
