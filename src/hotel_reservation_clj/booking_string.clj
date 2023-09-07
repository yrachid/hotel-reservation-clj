(ns hotel-reservation-clj.booking-string
  (:gen-class)
  (:import [java.time LocalDate]
           [java.time DayOfWeek]
           [java.time.format DateTimeFormatter])
  (:require [clojure.string :as str]))

(def ^:private tiers
  {"regular" :regular
   "rewards" :rewards})

(def ^:private weekend-days #{DayOfWeek/SATURDAY DayOfWeek/SUNDAY})

(def ^:private date-format
  (DateTimeFormatter/ofPattern "ddMMMyyyy"))

(defn- parse-customer-tier
  [customer-tier]
  (-> customer-tier
      str/lower-case
      tiers))

(defn- date-string-to-local-date
  [date-string]
  (-> date-string
      str/trim
      (subs 0 9)
      (LocalDate/parse date-format)))

(defn- weekend?
  [local-date]
  (contains? weekend-days (.getDayOfWeek local-date)))

(defn- count-days-by-type
  [stay date]
  (if (weekend? date)
    (update stay :weekends inc)
    (update stay :weekdays inc)))

(defn- count-nights-of-stay
  [date-string]
  (->> (str/split date-string #",")
       (map date-string-to-local-date)
       (reduce count-days-by-type {:weekends 0 :weekdays 0})))

(defn parse
  [booking-string]
  (let [[tier dates] (str/split booking-string #":")]
    {:tier (parse-customer-tier tier)
     :stay (count-nights-of-stay dates)}))

