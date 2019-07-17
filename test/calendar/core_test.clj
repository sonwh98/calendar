(ns calendar.core-test
  (:require [clojure.test :refer :all]
            [calendar.core :refer :all]))

(defn duration [evt]
  (- (:end evt)
     (:start evt)))

(defn valid-event? [evt]
  (-> evt duration pos?))

(defn sort-events [events]
  (sort (fn [evt1 evt2]
          (- (:start evt1)
             (:start evt2)))
        events))

(defn within?
  "read evt2 within evt1"
  [evt1 evt2] {:pre [(and (valid-event? evt1)
                          (valid-event? evt2))]}
  (let [start1 (:start evt1)
        end1 (:end evt1)
        
        start2 (:start evt2)
        end2 (:end evt2)]
    (and (>= start2 start1)
         (<= end2 end1))))


(defn intersect? [evt1 evt2] {:pre [(and (valid-event? evt1)
                                         (valid-event? evt2))]}
  (let [[evt-a evt-b] (sort-events [evt1 evt2])
        start-a (:start evt-a)
        end-a  (:end evt-a)

        start-b (:start evt-b)
        end-b (:end evt-b)]
    (<= start-b end-a)))

(defn over-lapping? [evt1 evt2] {:pre [(and (valid-event? evt1)
                                            (valid-event? evt2))]}
  (within? evt1 evt2))


(deftest overlap-test
  (testing "evt2 within evt1"
    (is (within?
         {:start 1 :end 5}
         {:start 1 :end 3}))
    (is (not (within?
              {:start 1 :end 5}
              {:start 6 :end 10}))))

  (testing "intersect?"
    (is (not (intersect?
              {:start 1 :end 5}
              {:start 6 :end 10})))

    (is (not (intersect?
              {:start 6 :end 10}
              {:start 1 :end 5})))

    (is (intersect?
         {:start 1 :end 5}
         {:start 4 :end 6}))

    (is (intersect?
         {:start 4 :end 6}
         {:start 1 :end 5}))

    (is (intersect?
         {:start 6 :end 7}
         {:start 1 :end 5}))
    )
  )


(comment
  (clojure.test/run-tests)

  (sort-events [ {:start 1} {:start 10} {:start 5} {:start 0}])
  )
