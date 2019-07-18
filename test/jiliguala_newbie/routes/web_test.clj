(ns jiliguala-newbie.routes.web-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [jiliguala-newbie.routes.web :as web]))

(deftest test-routes-web
  (testing "GET /"
    (let [{:keys [status body]} (web/web (mock/request :get "/"))]
      (is (= status 200))
      (is (= body "Hello world!")))))
