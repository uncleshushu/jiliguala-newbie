(ns newbie.domain.schema
  (:require
    [schema.core :as s]
    ))

(s/defschema Base
  {:result s/Str})

(s/defschema Response
  {:isSuccess s/Bool
   :errcode s/Int
   :errmsg s/Str
   :result {s/Keyword s/Any}})

(defn hello-world
  []
  {:result "Hello World"})




