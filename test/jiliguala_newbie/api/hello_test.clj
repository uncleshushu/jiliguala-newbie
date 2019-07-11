(ns jiliguala-newbie.api.hello-test
  (:require [clojure.test :refer :all]
            [jiliguala-newbie.specs.hello :refer :all]
            [jiliguala-newbie.api.hello :refer :all]))


(deftest test-api-hello
  (testing "greet"
    (is (= "Hello, shushu!"
           (greet {:name "shushu"}))
        "without title")
    (is (= "Hello, Mr. shushu!"
           (greet {:name "shushu", :title "Mr."}))
        "with title")
    )

  (testing "plus"
    (is (= 0 (:result (plus 0 0))))
    (is (= 0 (:result (plus -1 1))))
    (is (= 5 (:result (plus 0 5))))
    (is (= -9 (:result (plus 0 -9))))
    (is (= -6 (:result (plus -7 1))))
    (is (= 7 (:result (plus 2 5))))
    )

  (testing "echo"
    (is (= {:a 5 :b 9} (echo {:a 5 :b 9})))
    (is (= 9 (echo 9)))
    )
  )