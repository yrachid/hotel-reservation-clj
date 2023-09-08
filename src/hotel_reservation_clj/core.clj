(ns hotel-reservation-clj.core
  (:gen-class)
  (:require [hotel-reservation-clj.booking-string :as booking-string]
            [hotel-reservation-clj.hotel-rating :as rating]))

(def ^:private hotels [{:name "Lakewood"
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

(def ^:private price-booking (partial rating/price hotels))

(defn -main
  []
  (->> java.io.BufferedReader. *in*
       line-seq
       (map booking-string/parse)
       (map price-booking)
       (map :hotel)
       println))
