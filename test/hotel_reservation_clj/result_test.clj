(ns hotel-reservation-clj.result-test
  (:require [clojure.test :refer :all]
            [hotel-reservation-clj.result :as result]))

(deftest success-and-error
  (testing "Creates and error result"
    (is (= {:error "Invalid"} (result/error "Invalid"))))

  (testing "Creates a successful result"
    (is (= {:ok 1} (result/success 1)))))

(deftest result-map
  (testing "Maps over successful value"
    (is (= {:ok 2} (-> {:ok 1}
                       (result/map inc)))))

  (testing "Keeps error result on map"
    (is (= {:error "Invalid"} (-> {:error "Invalid"}
                                  (result/map inc))))))

(deftest zip
  (testing "Short-circuits if first result is error"
    (let [result-a {:error "invalid"}
          result-b {:ok 1}]
      (is (= {:error "invalid"}
             (result/zip result-a result-b +)))))

  (testing "Short-circuits if second result is error"
    (let [result-b {:ok 1}
          result-a {:error "invalid"}]
      (is (= {:error "invalid"}
             (result/zip result-a result-b +)))))

  (testing "Combines two successful results"
    (let [result-b {:ok 1}
          result-a {:ok 2}]
      (is (= {:ok 3}
             (result/zip result-a result-b +))))))
