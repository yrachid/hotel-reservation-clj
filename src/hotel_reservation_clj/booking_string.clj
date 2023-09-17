(ns hotel-reservation-clj.booking-string
  (:gen-class)
  (:import [java.time LocalDate]
           [java.time DayOfWeek]
           [java.time.format DateTimeFormatter DateTimeParseException])
  (:require [clojure.string :as str]
            [hotel-reservation-clj.result :as result]))

(def ^:private tiers
  {"Regular" :regular
   "Rewards" :rewards})

(def ^:private weekend-days #{DayOfWeek/SATURDAY DayOfWeek/SUNDAY})

(def ^:private date-format
  (DateTimeFormatter/ofPattern "ddMMMyyyy"))

(defn- weekend?
  [local-date]
  (contains? weekend-days (.getDayOfWeek local-date)))

(defn- invalid-date-error
  [date]
  (result/error (format "Invalid date: '%s'" date)))

(defn- invalid-customer-tier-error
  [tier]
  (result/error (format "Invalid customer tier: '%s'" tier)))

(defn- malformed-booking-string-error
  [booking-string]
  (result/error (format "Malformed booking string '%s'" booking-string)))

(defn trim-day-of-week
  [date-string]
  (subs date-string 0 9))

(defn- parse-customer-tier
  [customer-tier]
  (let [tier (tiers customer-tier)]
    (if (nil? tier)
      (invalid-customer-tier-error customer-tier)
      (result/success tier))))

(defn- date-string-to-local-date
  [date-string]
  (let [trimmed-date-string (str/trim date-string)]
    (try
      (-> trimmed-date-string
          trim-day-of-week
          (LocalDate/parse date-format)
          result/success)
      (catch Exception ex
        (invalid-date-error trimmed-date-string)))))

(defn- count-nights-of-stay
  [date-string]
  (defn process-date-by-date
    [dates stay]
    (if (empty? dates)
      (result/success stay)
      (let [date (-> dates first date-string-to-local-date)]
        (if (result/error? date)
          date
          (if (-> date :ok weekend?)
            (recur (rest dates) (update stay :weekends inc))
            (recur (rest dates) (update stay :weekdays inc)))))))

  (process-date-by-date (str/split date-string #",") {:weekends 0 :weekdays 0}))

(defn- parse-booking-string-chunks
  [tier-string dates-string]
  (let [parsed-tier (parse-customer-tier tier-string)
        nights-of-stay-count (count-nights-of-stay dates-string)]
    (result/zip parsed-tier nights-of-stay-count (fn [tier stay] {:tier tier :stay stay}))))

(defn parse
  [booking-string]
  (let [[tier-chunk dates-chunk] (str/split booking-string #":")
        sanitised-tier-chunk (str/trim (or tier-chunk ""))
        sanitised-dates-chunk (str/trim (or dates-chunk ""))]
    (cond
      (empty? sanitised-tier-chunk) (malformed-booking-string-error booking-string)
      (empty? sanitised-dates-chunk) (malformed-booking-string-error booking-string)
      :else (parse-booking-string-chunks tier-chunk dates-chunk))))

