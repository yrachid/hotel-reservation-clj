(ns hotel-reservation-clj.booking-string
  (:gen-class)
  (:import [java.time LocalDate]
           [java.time DayOfWeek]
           [java.time.format DateTimeFormatter])
  (:require [clojure.string :as str]))

(def ^:private tiers
  {"Regular" :regular
   "Rewards" :rewards})

(def ^:private weekend-days #{DayOfWeek/SATURDAY DayOfWeek/SUNDAY})

(def ^:private date-format
  (DateTimeFormatter/ofPattern "ddMMMyyyy"))

(defn- parse-customer-tier
  [customer-tier]
  (let [tier (tiers customer-tier)]
    (if (nil? tier)
      {:error (format "Invalid customer tier '%s'" customer-tier)}
      {:ok tier})))

(defn- date-string-to-local-date
  [date-string]
  (let [trimmed-date-string (str/trim date-string)]
    (if (< (count trimmed-date-string) 14)
      {:error (format "Invalid date: '%s'" trimmed-date-string)}
      {:ok (LocalDate/parse (subs trimmed-date-string 0 9) date-format)})))

(defn- weekend?
  [local-date]
  (contains? weekend-days (.getDayOfWeek local-date)))

(defn- error?
  [result]
  (not (nil? (result :error))))

(defn- count-nights-of-stay
  [date-string]
  (defn process-date-by-date
    [dates stay]
    (if (empty? dates)
      stay
      (let [current (first dates)
            date (date-string-to-local-date current)]
        (cond
          (error? date) date
          (weekend? (date :ok)) (process-date-by-date (rest dates) (update stay :weekends inc))
          :else (process-date-by-date (rest dates) (update stay :weekdays inc))))))

  (process-date-by-date (str/split date-string #",") {:weekends 0 :weekdays 0}))

(defn- parse-booking-string-chunks
  [tier-string dates-string]
  (let [
        parsed-tier (parse-customer-tier tier-string)
        nights-of-stay-count (count-nights-of-stay dates-string)]
    (cond
      (error? parsed-tier) parsed-tier
      (error? nights-of-stay-count) nights-of-stay-count
      :else {:tier (parsed-tier :ok)
             :stay nights-of-stay-count})))

(defn- malformed-booking-string-error
  [booking-string]
  {:error (format "Malformed booking string '%s'" booking-string)})

(defn parse
  [booking-string]
  (let [
        [tier-chunk dates-chunk] (str/split booking-string #":")
        sanitised-tier-chunk (str/trim (or tier-chunk ""))
        sanitised-dates-chunk (str/trim (or dates-chunk ""))]
    (cond
      (empty? sanitised-tier-chunk) (malformed-booking-string-error booking-string)
      (empty? sanitised-dates-chunk) (malformed-booking-string-error booking-string)
      :else (parse-booking-string-chunks tier-chunk dates-chunk))))

