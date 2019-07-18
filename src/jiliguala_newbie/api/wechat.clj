(ns jiliguala-newbie.api.wechat
  (:require
   [clojure.java.io :as io]
   [clojure.tools.logging :as log]
   [clojure.edn :as edn]
   [ring.util.codec :refer [url-encode]]
   [org.httpkit.client :as http]
   [cheshire.core :as cheshire]
   [digest]
   [jiliguala-newbie.config :as config]))

(def ^:private
;^{:test #(assert (not (empty? conf)))}
  conf
  "wechat api conf"
  ;; TODO: use `delay` to avoid empty `config/wechat-conf` at compile time?
  (if-let [wechat-conf (io/resource "wechat.edn")]
    (->> wechat-conf
         slurp
         edn/read-string)
    @config/wechat-conf))

(def ^:private users
  "users who have authorized our app"
  ;; TODO: defrecord/defstruct?
  (atom {}))


;; TODO: refresh access_token


;; TODO: define error code


(defn show-wechat-conf []
  conf)

(defn auth-wechat-server
  "authenticate wechat server with token signature"
  [signature timestamp nonce echostr]
  (let [digest-code (->> [(:wechat-server-token conf) timestamp nonce]
                         sort
                         (apply str)
                         digest/sha-1)]
    (if (= digest-code signature)
      echostr
      "")))

(def oauth2-url
  "wechat oauth2 url"
  (format
   "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"
   (conf :appID)
   (url-encode (str (conf :app-base-url) "/oauth2-callback"))))

(defn login! [code]
  "get access token for user info"
  (try
    (let [{:keys [appID appsecret]} conf
          url "https://api.weixin.qq.com/sns/oauth2/access_token"
          {:keys [body]} @(http/get url {:query-params {:appid      appID
                                                        :secret     appsecret
                                                        :code       code
                                                        :grant_type "authorization_code"}})
          token-info (cheshire/parse-string body true)]

      (log/info "wechat/get-userinfo-access_token wechat server response:" token-info)

      (if-let [wechat-errcode (token-info :errcode)]
        ;; fail
        (do (log/error "wechat/get-userinfo-access_token failed, wechat errcode:" wechat-errcode)
            {:error-code 600101 :error-msg "Failed to get access_token for userinfo"})
        ;; success
        (let [{openid :openid} token-info]
          (swap! users assoc openid token-info)
          {:openid openid})))

    (catch Exception e
      (log/error "wechat/get-userinfo-access_token error:" (.getMessage e))
      {:error-code 500101 :error-msg "Unexpected exception during getting access_token for userinfo"})))

(defn logout! [openid]
  (swap! users dissoc openid))

(defn logged-in? [openid]
  (contains? @users openid))

(defn get-userinfo [openid]
  (try
    (if (logged-in? openid)
      ;; user has logged in
      (let [url "https://api.weixin.qq.com/sns/userinfo"
            {{access_token :access_token} openid} @users
            {:keys [body]} @(http/get url {:query-params {:access_token access_token
                                                          :openid       openid
                                                          :lang         "zh_CN"}})
            userinfo (cheshire/parse-string body true)]
        (log/info "wechat/get-userinfo wechat server response:" userinfo)
        (if-let [wechat-errcode (userinfo :errcode)]
          ;; fail
          (do (log/error "wechat/get-userinfo failed, wechat errcode:" wechat-errcode)
              {:error-code 600102 :error-msg "Failed to get userinfo"})
          ;; success
          userinfo))

      ;; user has logged out or hasn't authorized out app
      (do (log/warn "wechat/get-userinfo failed: user has logged out or hasn't authorized our app")
          {:error-code 401100 :error-msg "You have logged out or haven't authorized our app"}))

    (catch Exception e
      (log/error "get userinfo error: " (.getMessage e))
      {:error-code 500102 :error-msg "Unexpected exception during getting userinfo"})))