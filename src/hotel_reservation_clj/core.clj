(ns hotel-reservation-clj.core
  (:gen-class)
  (:import [java.time LocalDate]
           [java.time DayOfWeek]
           [java.time.format DateTimeFormatter])
  (:require [clojure.string :as str]))

(def ^:private ratings
  {"regular" :regular
   "rewards" :rewards})

(def ^:private weekend-days #{DayOfWeek/SATURDAY DayOfWeek/SUNDAY})

(def ^:private date-format
  (DateTimeFormatter/ofPattern "ddMMMyyyy"))

(defn- determine-rating
  [customer-type]
  (-> customer-type
      str/lower-case
      ratings))

(defn- extract-date
  [date-string]
  (-> date-string
      str/trim
      (subs  0 9)
      (LocalDate/parse date-format)))

(defn- weekend?
  [local-date]
  (contains? weekend-days (.getDayOfWeek date)))

(defn- consolidate-dates
  [accum date]
  (let [date-type (if (weekend? date) :weekends :weekdays)]
    (update accum date-type inc)))

(defn- determine-schedule
  [date-string]
  (->> (str/split date-string #",")
       (map extract-date)
       (reduce consolidate-dates {:weekends 0 :weekdays 0})))

(defn rate
  [booking]
  (let [[rate dates] (str/split booking #":")]
    {:rating (determine-rating rate)
     :schedule (determine-schedule dates)}))
