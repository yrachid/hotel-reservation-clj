(ns hotel-reservation-clj.result
  (:gen-class))

(def success (partial hash-map :ok))
(def error (partial hash-map :error))

(defn success?
  [result]
  (not (-> result :ok nil?)))

(defn error?
  [result]
  (not (-> result :error nil?)))

(defn map
  [result mapper]
  (if (success? result)
    (-> result :ok mapper success)
    result))

(defn zip
  [result-a result-b zipper]
  (cond
    (error? result-a) result-a
    (error? result-b) result-b
    :else (success (zipper (result-a :ok) (result-b :ok)))))
