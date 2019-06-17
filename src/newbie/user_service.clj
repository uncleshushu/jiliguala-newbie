(ns newbie.user-service
  (:require
    [clojure.tools.logging :as log]
    [newbie.repository.mongodb :as mg]
    [newbie.domain :as domain]
    )
  )

(defn login
  [openid]
  (let [user (mg/find-user-by-openid openid)]
    (if (nil? user)
      (do (log/warn "unregister user, openid: " openid)
          (domain/failResponse 40200 "unregister user"))
      (do (log/info "user login, openid: " openid)
          (domain/succResponse (.toJson user))))))

(defn register
  [userinfo]
  (let [{:keys [openid]} userinfo
        exist (mg/find-user-by-openid openid)]
    (cond (nil? openid) (do (log/error "openid is empty")
                            (domain/failResponse 40000 "empty openid"))
          (some? exist) (do (log/error "openid has already been registered")
                            (domain/failResponse 40100 "already registered"))
          :else (do (mg/add-user userinfo)
                    (log/info "user registered, openid: " openid)
                    (domain/succResponse "register succeed")))))

(defn order
  [openid pid]
  (let [user (mg/find-user-by-openid openid)
        product (mg/find-product-by-id pid)
        order_able (and user product)]
    (cond (not order_able)
          (do (log/error "invalid openId or productId, openid: " openid ", pid: " pid)
              (domain/failResponse 40200 "invalid order"))

          (nil? (mg/add-order openid pid))
          (do (log/error "error creating order")
              (domain/failResponse 40300 "order creation failed"))

          :else
          (do (log/info "new order created, openid: " openid ", pid: " pid)
              (domain/succResponse {:status "succeed"})))))
