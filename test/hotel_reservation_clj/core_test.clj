(ns hotel-reservation-clj.core-test
  (:require [clojure.test :refer :all]
            [hotel-reservation-clj.core :as hotel]))

(deftest customer-tier-support
  (defn booking-string
    [customer-tier]
    (str customer-tier ": 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"))

  (testing "Supports REGULAR as customer tier"
    (is (= :regular (->
                      "REGULAR"
                      booking-string
                      hotel/rate
                      :tier))))

  (testing "Supports Regular as customer tier"
    (is (= :regular (->
                      "Regular"
                      booking-string
                      hotel/rate
                      :tier))))

  (testing "Supports ReGuLaR as customer tier"
    (is (= :regular (->
                      "ReGuLaR"
                      booking-string
                      hotel/rate
                      :tier))))

  (testing "Supports Rewards as customer tier"
    (is (= :rewards (->
                      "Rewards"
                      booking-string
                      hotel/rate
                      :tier))))
  )

; (deftest determine-reward-rate
;   (testing "Determines booking rate for rewards customer"
;     (def booking "Rewards: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)")
; 
;     (def expected-rating {:tier :rewards})
; 
;     (is (= expected-rating (hotel/rate booking)))))
