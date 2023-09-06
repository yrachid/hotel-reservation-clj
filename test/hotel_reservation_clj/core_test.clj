(ns hotel-reservation-clj.core-test
  (:require [clojure.test :refer :all]
            [hotel-reservation-clj.core :as hotel]))

(deftest determine-regular-weekday-only
  (def booking "Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)")

  (def processed-booking (hotel/rate booking))

  (testing "Determines customer rate as regular"
    (is (= :regular (processed-booking :rating))))

  (testing "Classifies date types"
    (def expected-schedule {:weekdays 3 :weekends 0})
    (is (= expected-schedule (processed-booking :schedule)))))

; (deftest determine-reward-rate
;   (testing "Determines booking rate for rewards customer"
;     (def booking "Rewards: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)")
; 
;     (def expected-rating {:rating :rewards})
; 
;     (is (= expected-rating (hotel/rate booking)))))
