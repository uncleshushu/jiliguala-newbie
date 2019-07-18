(ns jiliguala-newbie.routes.web
  (:require [clojure.tools.logging :as log]
            [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [jiliguala-newbie.api.wechat :as wechat]))

(def web
  (context "/" []
    :tags ["web"]

    ;; index page
    (GET "/" []
      :return String
      :summary "index page"
      (ok "Hello world!"))))
