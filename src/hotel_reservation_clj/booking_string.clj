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

(defn- error?
  [result]
  (not (nil? (result :error))))

(defn- parse-booking-string-chunks
  [tier-string dates-string]
  (let [
        parsed-tier (parse-customer-tier tier-string)
        nights-of-stay-count (count-nights-of-stay dates-string)]
    (cond
      (error? parsed-tier) parsed-tier
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

