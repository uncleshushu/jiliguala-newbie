(ns newbie.domain
  (:require [schema.core :as s]
            ))
;; Domain
(s/defschema Base
  {:result s/Str})

(s/defschema Weixin
  {:name s/Str
   :nickname s/Str})

(defn hello-world
  []
  {:result "Hello World"})

(defn weixin-info
  [code state]
  {:name code :nickname state})

