(ns jiliguala-newbie.specs.hello
  (:require [schema.core :as s]
            ))

(s/defschema Pizza
             {:name                         s/Str
              (s/optional-key :description) s/Str
              :size                         (s/enum :L :M :S)
              :origin                       {:country (s/enum :FI :PO)
                                             :city    s/Str}})

(s/defschema UserInfo
             {:name                   s/Str
              (s/optional-key :title) (s/enum "Mr." "Mrs." "Ms.")})