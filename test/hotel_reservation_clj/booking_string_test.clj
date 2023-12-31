(ns hotel-reservation-clj.booking-string-test
  (:require [clojure.test :refer :all]
            [hotel-reservation-clj.booking-string :as booking-string]))

(deftest malformed-booking-string
  (testing "Gracefully fails when a malformed booking string is received"
    (is (= {:error "Malformed booking string ' : '"} (booking-string/parse " : ")))
    (is (= {:error "Malformed booking string ':'"} (booking-string/parse ":")))
    (is (= {:error "Malformed booking string ': 16Mar2009(mon)'"} (booking-string/parse ": 16Mar2009(mon)")))
    (is (= {:error "Malformed booking string 'Regular: '"} (booking-string/parse "Regular: ")))
    (is (= {:error "Malformed booking string ''"} (booking-string/parse "")))))

(deftest customer-tier-support
  (testing "Supports Regular as customer tier"
    (is (= :regular (->
                     "Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
                     booking-string/parse
                     :ok
                     :tier))))
  (testing "Supports Rewards as customer tier"
    (is (= :rewards (-> "Rewards: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
                        booking-string/parse
                        :ok
                        :tier))))
  (testing "Validates against invalid tier types"
    (is (= {:error "Invalid customer tier: 'Premium'"}
           (-> "Premium: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
               booking-string/parse))))
  (testing "Is case sensitive"
    (is (= {:error "Invalid customer tier: 'regular'"}
           (-> "regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
               booking-string/parse)))))

(deftest booking-date-validation
  (testing "Validates against booking dates out of format"
    (is (= {:error "Invalid date: '16Bla2009(mon)'"}
           (-> "Regular: 16Bla2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
               booking-string/parse)))
    (is (= {:error "Invalid date: '16ar2009(mon)'"}
           (-> "Regular: 16ar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
               booking-string/parse)))
    (is (= {:error "Invalid date: '36Mar2009(tue)'"}
           (-> "Regular: 16Mar2009(mon), 36Mar2009(tue), 18Mar2009(wed)"
               booking-string/parse)))
    (is (= {:error "Invalid date: '2009-03-16T100'"}
           (-> "Regular: 2009-03-16T100, 17Mar2009(tues), 18Mar2009(wed)"
               booking-string/parse)))))

(deftest stay-calculation
  (testing "Ignores day of week description in date inputs"
    (is (= {:weekdays 3 :weekends 0}
           (-> "Regular: 16Mar2009, 17Mar2009(tues), 18Mar2009(wed)"
               booking-string/parse
               :ok
               :stay)))
    (is (= {:weekdays 3 :weekends 0}
           (-> "Regular: 16Mar2009(mon), 17Mar2009, 18Mar2009(wed)"
               booking-string/parse
               :ok
               :stay))))

  (testing "Keeps weekends as zero when there are only weekdays"
    (is (= {:weekdays 3 :weekends 0} (-> "Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)"
                                         booking-string/parse
                                         :ok
                                         :stay))))
  (testing "Counts weekends and weekdays"
    (is (= {:weekdays 2 :weekends 2} (-> "Regular: 20Mar2009(fri), 21Mar2009(sat), 22Mar2009(sun), 23Mar2009(mon)"
                                         booking-string/parse
                                         :ok
                                         :stay))))
  (testing "Counts a single night stay"
    (is (= {:weekdays 0 :weekends 1} (-> "Regular: 21Mar2009(sat)"
                                         booking-string/parse
                                         :ok
                                         :stay)))
    (is (= {:weekdays 1 :weekends 0} (-> "Regular: 20Mar2009(fri)"
                                         booking-string/parse
                                         :ok
                                         :stay)))))
