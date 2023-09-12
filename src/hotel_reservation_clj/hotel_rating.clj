(ns hotel-reservation-clj.hotel-rating
  (:gen-class))

(defn- price-or-rating
  [hotel-price-a hotel-price-b]
  (if (= (hotel-price-a :price) (hotel-price-b :price))
    (> (hotel-price-a :rating) (hotel-price-b :rating))
    (< (hotel-price-a :price) (hotel-price-b :price))))

(defn- get-rate
  [day-type hotel booking]
  (get-in hotel [:rates (booking :tier) day-type]))

(def ^:private weekend-rate (partial get-rate :weekend))
(def ^:private weekday-rate (partial get-rate :weekday))

(defn- to-priced-hotel
  [booking]
  (fn [hotel] {:hotel (hotel :name)
               :rating (hotel :rating)
               :price (+
                       (* (get-in booking [:stay :weekends])
                          (weekend-rate hotel booking))
                       (* (get-in booking [:stay :weekdays])
                          (weekday-rate hotel booking)))}))

(defn price
  [hotels booking]
  (->> hotels
       (map (to-priced-hotel booking))
       (into (sorted-set-by price-or-rating))
       first))

