(ns newbie.handler
  (:require [compojure.api.sweet :refer :all]
            [newbie.domain :refer :all]
            [ring.util.http-response :refer :all]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            ))

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Newbie"
                    :description "Jiliguala newbie project"}
             :tags [{:name "api", :description "Api"}
                    {:name "weixin", :description "Weixin api"}]}}}

    (context "/api" []
      :tags ["api"]

      (GET "/hello-world" []
        :return Base
        :summary "Hello World"
        (ok (hello-world)))

      (GET "/weixin-info" []
        :return Response
        :tags ["weixin"]
        :query-params [code :- String, state :- String]
        :summary "回调接口，无法直接调用"
        (ok (weixin-info code state)))

      (GET "/weixin-auth-info" []
        :tags ["weixin"]
        :summary "需要在微信客户端中运行"
        (permanent-redirect (weixin-auth-info)))
      )))

(def reload-app
  (wrap-reload #'app))

(defn -main
  [& args]
  (run-jetty reload-app {:port (Integer/valueOf (or (System/getenv "port") "3000"))}))
