(ns newbie.repository.mongodb
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [newbie.util :as util]
            [clojure.edn :as edn]
            )
  (:import [com.mongodb MongoOptions ServerAddress]
           (org.bson.types ObjectId)))

(def config (edn/read-string (slurp (clojure.java.io/resource "mongodb.edn"))))
(def conn (let [^MongoOptions opts (mg/mongo-options (:option config))
                ^ServerAddress sa (mg/server-address (:host-address config) (:port config))]
            (mg/connect sa opts)))

(def db (mg/get-db conn (:db config)))
(def cu (:coll_users config))
(def co (:coll_orders config))
(def cp (:coll_products config))

(defn find-user-by-openid [openid]
  (mc/find-one db cu {:openid openid}))

(defn find-product-by-id [pid]
  (mc/find-one db cp {:_id pid}))

(defn add-user [userinfo]
  (mc/insert db cu (-> userinfo
                       util/createdAt
                       util/updatedAt)))

(defn add-order [openid pid]
  (mc/insert db co (-> {:_id    (ObjectId.)
                        :openid openid
                        :pid    pid}
                       util/createdAt
                       util/updatedAt)))