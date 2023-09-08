(ns hotel-reservation-clj.hotel-rating-test
  (:require [clojure.test :refer :all]
            [hotel-reservation-clj.hotel-rating :as rating]))

(deftest price-a-booking
  (def hotels [{:name "Lakewood"
                :rating 3
                :rates {:regular {:weekend 90 :weekday 110}
                        :rewards {:weekend 80 :weekday 80}}}
               {:name "Bridgewood"
                :rating 4
                :rates {:regular {:weekend 60 :weekday 160}
                        :rewards {:weekend 50 :weekday 110}}}
               {:name "Ridgewood"
                :rating 5
                :rates {:regular {:weekend 150 :weekday 220}
                        :rewards {:weekend 40  :weekday 100}}}])

  (is (= {:hotel "Lakewood" :rating 3 :price 330}
         (->>
          {:tier :regular :stay {:weekends 0 :weekdays 3}}
          (rating/price hotels))))

  (is (= {:hotel "Bridgewood" :rating 4 :price 280}
         (->>
          {:tier :regular :stay {:weekends 2 :weekdays 1}}
          (rating/price hotels))))

  (is (= {:hotel "Ridgewood" :rating 5 :price 240}
         (->>
          {:tier :rewards :stay {:weekends 1 :weekdays 2}}
          (rating/price hotels)))))
