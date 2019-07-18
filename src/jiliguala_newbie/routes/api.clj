(ns jiliguala-newbie.routes.api
  (:require [clojure.tools.logging :as log]
            [compojure.api.sweet :refer :all :rename {api cpj-api}]
            [ring.util.http-response :refer :all]
            [jiliguala-newbie.api.hello :as api.hello]
            [jiliguala-newbie.api.wechat :as api.wechat]
            [jiliguala-newbie.specs.hello :as spec.hello]
            [jiliguala-newbie.specs.wechat :as specs.wechat]))

(def api
  (context "/api" []

    (context "/hello" []
      :tags ["hello"]

      (POST "/greet" []
        :return String
        :body [user-info spec.hello/UserInfo]
        :summary "greets the user"
        (ok (api.hello/greet user-info)))

      (GET "/plus" []
        :return {:result Long}
        :query-params [x :- Long, y :- Long]
        :summary "adds two numbers together"
        (ok (api.hello/plus x y)))

      (POST "/echo-pizza" []
        :return spec.hello/Pizza
        :body [pizza spec.hello/Pizza]
        :summary "echoes a Pizza"
        (ok (api.hello/echo pizza))))

    (context "/wechat" []
      :tags ["wechat"]

      (GET "/" []
        :summary "authenticate wechat server"
        :query-params [signature :- String
                       timestamp :- String
                       nonce :- String
                       echostr :- String]
        :return String
        ;; TODO: plain text
        :produces ["text/plain"]
        (let [return-str (api.wechat/auth-wechat-server signature timestamp nonce echostr)]
          (condp = return-str
            echostr (ok echostr)
            "" (bad-request ""))))

      (GET "/conf" []
        :summary "show wechat api conf"
        ;; TODO: remove this later!!!
        (ok (api.wechat/show-wechat-conf)))

      (GET "/auth" {{openid :openid} :session}
        ;; maybe we should use POST?
        :summary "lead user to auth our app (login with wechat openid)"
        (log/info "session when auth:")
        (if (api.wechat/logged-in? openid)
          ;; already logged in
          (ok
           (format
            "<html>
                <body>
                  <h1>You have already authorized our app!</h1>
                  <p><a href=\"api.uncleshsuhu.net/jiliguala-newbie/wechat/userinfo/%s\">See your user info</a></p>
                </body>
               </html>"
            openid))
          ;; no session found
          ;; use 303 see-other to change method to GET
          (let [redirect-resp (see-other api.wechat/oauth2-url)]
            (log/info "/wechat/auth response:" redirect-resp)
            redirect-resp)))

      (DELETE "/auth/:openid" []
        :summary "logout wechat openid"
        :path-params [openid :- String]
        :return String
        (api.wechat/logout! openid)
        (-> (ok "You have logged out!")
            (assoc :session nil)))

      (GET "/oauth2-callback" []
        :summary "wechat oauth2 callback to get userinfo access_token"
        :query-params [code :- String]
        :return String
        (let [{:keys [error-code error-msg openid]} (api.wechat/login! code)]
          (if error-code
            (internal-server-error {:description "Failed to authorize our app!"
                                    :error-code error-code
                                    :error-msg error-msg})
            ;; success
            ;; TODO: text/html
            (-> (ok (format
                     "<html>
                        <body>
                          <h1>Successfully authorized our app!</h1>
                          <p><a href=\"api.uncleshsuhu.net/jiliguala-newbie/wechat/userinfo/%s\">See your user info</a></p>
                        </body>
                       </html>"
                     openid))
                (assoc-in [:session :openid] openid)))))

      (GET "/userinfo/:openid" []
        :summary "get current wechat user's info"
        :path-params [openid :- String]
        (let [{:keys [error-code error-msg openid]} (api.wechat/get-userinfo openid)]
          (if error-code
            (internal-server-error {:description "Failed to get userinfo!"
                                    :error-code error-code
                                    :error-msg error-msg})
            (ok (api.wechat/get-userinfo openid))))))))
