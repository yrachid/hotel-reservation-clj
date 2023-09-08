(ns hotel-reservation-clj.booking-string-test
  (:require [clojure.test :refer :all]
            [hotel-reservation-clj.booking-string :as booking-string]))

(deftest customer-tier-support
  (testing "Supports Regular as customer tier"
    (is (= :regular (->
                     "Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
                     booking-string/parse
                     :tier))))
  (testing "Supports Rewards as customer tier"
    (is (= :rewards (-> "Rewards: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
                        booking-string/parse
                        :tier)))))

(deftest stay-calculation
  (testing "Keeps weekends as zero when tere are only weekdays"
    (is (= {:weekdays 3 :weekends 0} (-> "Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
                                         booking-string/parse
                                         :stay))))
  (testing "Counts weekends and weekdays"
    (is (= {:weekdays 2 :weekends 2} (-> "Regular: 20Mar2009(fri), 21Mar2009(sat), 22Mar2009(sun), 23Mar2009(mon)"
                                         booking-string/parse
                                         :stay))))
  (testing "Counts a single night stay"
    (is (= {:weekdays 0 :weekends 1} (-> "Regular: 21Mar2009(sat)"
                                         booking-string/parse
                                         :stay)))
    (is (= {:weekdays 1 :weekends 0} (-> "Regular: 20Mar2009(fri)"
                                         booking-string/parse
                                         :stay)))))