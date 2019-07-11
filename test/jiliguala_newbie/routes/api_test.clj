(ns jiliguala-newbie.routes.api-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as cheshire]
            [ring.mock.request :as mock]
            [jiliguala-newbie.core :refer :all]
            ))

(defn- parse-body
  [body]
  (cheshire/parse-string (slurp body) true))

(deftest test-routes-api
  (testing "/api/hello"
    (testing "POST /api/hello/greet"
      (testing "with title"
        (let [{status :status :as response}
                (app (-> (mock/request :post "/api/hello/greet")
                             (mock/json-body {:title "Mr."
                                              :name  "shushu"})))
              body (parse-body (:body response))]
          (is (= status 200))
          (is (= body "Hello, Mr. shushu!"))))
      (testing "without title"
        (let [response
                (app (-> (mock/request :post "/api/hello/greet")
                             (mock/json-body {:name "shushu"})))
              status (:status response)
              body (parse-body (:body response))]
          (is (= status 200))
          (is (= body "Hello, shushu!"))))
      )

    (testing "GET /api/hello/plus"
      (let [{:keys [status body]}
              (app (mock/request :get "/api/hello/plus" {:x 5 :y -6}))
            body (parse-body body)]
        (is (= status 200))
        (is (= body {:result -1})))
      )

    (testing "POST /api/hello/echo-pizza"
      (let [{:keys [status body]}
              (app (-> (mock/request :post "/api/hello/echo-pizza")
                           (mock/json-body {:name "hahaha"
                                            :description "a pizza"
                                            :size :L
                                            :origin {:country "FI"
                                                     :city "HK"}})))
            body (parse-body body)]
        (is (= status 200))
        (is (= body {:name "hahaha"
                     :description "a pizza"
                     :size "L"
                     :origin {:country "FI"
                              :city "HK"}})))
      )
    )
  )
