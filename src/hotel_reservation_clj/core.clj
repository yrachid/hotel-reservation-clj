(ns hotel-reservation-clj.core
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

(defn- day-string-to-local-date
  [date-string]
  (-> date-string
      str/trim
      (subs  0 9)
      (LocalDate/parse date-format)))

(defn- weekend?
  [local-date]
  (contains? weekend-days (.getDayOfWeek local-date)))

(defn- count-days-by-type
  [date-count date]
  (if (weekend? date)
    (update date-count :weekends inc)
    (update date-count :weekdays inc)))

(defn- determine-schedule
  [date-string]
  (->> (str/split date-string #",")
       (map day-string-to-local-date)
       (reduce count-days-by-type {:weekends 0 :weekdays 0})))

(defn rate
  [booking]
  (let [[tier dates] (str/split booking #":")]
    {:tier (parse-customer-tier tier)
     :stay (determine-schedule dates)}))

