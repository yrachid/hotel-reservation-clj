(ns hotel-reservation-clj.core-test
  (:require [clojure.test :refer :all]
            [hotel-reservation-clj.core :as hotel]))

(deftest customer-tier-support
  (testing "Supports Regular as customer tier"
    (is (= :regular (->
                      "Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
                      hotel/parse-booking-string
                      :tier))))
  (testing "Supports Rewards as customer tier"
    (is (= :rewards (-> "Rewards: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
                        hotel/parse-booking-string
                        :tier)))))

(deftest stay-calculation
  (testing "Keeps weekends as zero when tere are only weekdays"
    (is (= {:weekdays 3 :weekends 0} (-> "Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
                                         hotel/parse-booking-string
                                         :stay))))
  (testing "Counts weekends and weekdays"
    (is (= {:weekdays 2 :weekends 2} (-> "Regular: 20Mar2009(fri), 21Mar2009(sat), 22Mar2009(sun), 23Mar2009(mon)"
                                         hotel/parse-booking-string
                                         :stay))))
  (testing "Counts a single night stay"
    (is (= {:weekdays 0 :weekends 1} (-> "Regular: 21Mar2009(sat)"
                                         hotel/parse-booking-string
                                         :stay)))
    (is (= {:weekdays 1 :weekends 0} (-> "Regular: 20Mar2009(fri)"
                                         hotel/parse-booking-string
                                         :stay)))))
