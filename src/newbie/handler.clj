(ns newbie.handler
  (:require [compojure.api.sweet :refer :all]
            [newbie.domain :refer :all]
            [ring.util.http-response :refer :all]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Newbie"
                    :description "Jiliguala newbie project"}
             :tags [{:name "base", :description "newbie base api"}
                    {:name "weixin", :description "newbie weixin api"}]}}}

    (context "/api" []
      :tags ["api"]

      (GET "/hello-world" []
        :return Base
        :summary "Hello World"
        (ok (hello-world)))

      (GET "/weixin-info" []
        :return Weixin
        :query-params [code :- String, state :- String]
        :summary "Weixin info with silence oauth"
        (ok (weixin-info code state)))
      )))

(defn -main
  [& args]
  (run-jetty app {:port (Integer/valueOf (or (System/getenv "port") "3000"))}))
