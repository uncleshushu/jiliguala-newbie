(ns jiliguala-newbie.routes.api
  (:require [compojure.api.sweet :refer :all :rename {api cpj-api}]
            [ring.util.http-response :refer :all]
            [jiliguala-newbie.api.hello :as api.hello]
            [jiliguala-newbie.specs.hello :refer :all]
            ))


(def api
  (context "/api" []

      (context "/hello" []
        :tags ["hello"]

        (POST "/greet" []
              :return String
              :body [user-info UserInfo]
              :summary "greets the user"
              (ok (api.hello/greet user-info)))

        (GET "/plus" []
             :return {:result Long}
             :query-params [x :- Long, y :- Long]
             :summary "adds two numbers together"
             (ok (api.hello/plus x y)))

        (POST "/echo-pizza" []
              :return Pizza
              :body [pizza Pizza]
              :summary "echoes a Pizza"
              (ok (api.hello/echo pizza)))
        )
      )
  )
