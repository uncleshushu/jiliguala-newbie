(ns jiliguala-newbie.core
  (:require (compojure.api [sweet :refer :all]
                           [exception :as cpj-api-ex]
                           )
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            (jiliguala-newbie.routes [api :as routes.api]
                                     [web :as routes.web]
                                     )
            ))

(def app
  (api
    {:exceptions
     {:handlers
      ;; override compjure.api.middleware/api-middleware-defaults
      ;; and catch specified exceptions
      {;; log all request validation errors as warn
       ::cpj-api-ex/request-validation (cpj-api-ex/with-logging
                                         cpj-api-ex/request-validation-handler :warn)
       ::cpj-api-ex/request-parsing (cpj-api-ex/with-logging
                                         cpj-api-ex/request-parsing-handler :warn)
       ::cpj-api-ex/response-validation (cpj-api-ex/with-logging
                                          cpj-api-ex/response-validation-handler :error)}}

     :swagger
     {:ui   "/swagger"
      :spec "/swagger.json"
      :data {:info {:title       "Jiliguala-newbie"
                    :description "Jiliguala newbie project"}
             :tags [{:name "web", :description "web pages"}
                    {:name "hello", :description "hello-world apis"}
                    ]
              }}}

    ;; api
    routes.api/api

    ;; web
    routes.web/web
    ))
