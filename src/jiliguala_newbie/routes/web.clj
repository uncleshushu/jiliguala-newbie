(ns jiliguala-newbie.routes.web
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            ))

(def web
  (context "/" []
    :tags ["web"]

    ;; index page
    (GET "/" []
      :return String
      :summary "index page"
      (ok "Hello world!"))
    ))
