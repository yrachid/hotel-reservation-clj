(ns hotel-reservation-clj.hotel-rating
  (:gen-class))

(defn- price-or-rating
  [hotel-price-a hotel-price-b]
  (if (= (hotel-price-a :price) (hotel-price-b :price))
    (> (hotel-price-a :rating) (hotel-price-b :rating))
    (< (hotel-price-a :price) (hotel-price-b :price))))

(defn- to-priced-hotel
  [booking]
  (fn [hotel] {:hotel (hotel :name)
               :rating (hotel :rating)
               :price (+
                       (* (-> booking :stay :weekends)
                          (-> hotel :rates (get (booking :tier)) :weekend))
                       (* (-> booking :stay :weekdays)
                          (-> hotel :rates (get (booking :tier)) :weekday)))}))

(defn price
  [hotels booking]
  (->> hotels
       (map (to-priced-hotel booking))
       (into (sorted-set-by price-or-rating))
       first))

