(ns calendar.core-test
  (:require [clojure.test :refer :all]))

(defn duration [evt]
  (- (:end evt)
     (:start evt)))

(defn valid-event?
  "a valid event must have a positive duration"
  [evt]
  (-> evt duration pos?))

(defn sort-events
  "sort events in ascending order"
  [events]
  (sort (fn [evt1 evt2]
          (- (:start evt1)
             (:start evt2)))
        events))

(defn overlap? [evt1 evt2] {:pre [(and (valid-event? evt1)
                                       (valid-event? evt2))]}
  (let [[evt-a evt-b] (sort-events [evt1 evt2])
        start-a (:start evt-a)
        end-a  (:end evt-a)

        start-b (:start evt-b)
        end-b (:end evt-b)]
    (<= start-b end-a)))

(defn report-overlapping [events]
  (let [sorted-events (sort-events events)]
    (for [e1 sorted-events
          :let [other (remove #(= % e1) sorted-events)
                _ (prn "other = " other)
                pairs (remove nil? (for [e2 (remove #(= % e1) sorted-events)]
                                     (if (overlap? e1 e2)
                                       #{e1 e2}
                                       nil)))
                _ (prn "pairs=" pairs)]]
      pairs)))

(deftest overlap-test
  (testing "no overlap"
    (is (not (overlap?
              {:start 1 :end 5}
              {:start 6 :end 10})))
    (is (not (overlap?
              {:start 6 :end 10}
              {:start 1 :end 5}))))

  (testing "overlap"
    (is (overlap?
         {:start 1 :end 5}
         {:start 4 :end 6}))

    (is (overlap?
         {:start 4 :end 6}
         {:start 1 :end 5})))

  (testing "overlap where one event is within the time of another"
    (is (overlap?
         {:start 2 :end 4}
         {:start 1 :end 5}))

    (is (overlap?
         {:start 1 :end 5}
         {:start 2 :end 4}))))

(deftest report-test
  (testing "no overlapping events"
    (let [events [{:start 1 :end 3}
                  {:start 4 :end 6}
                  {:start 7 :end 10}]]
      (is (empty? (report-overlapping events)))))

  (testing "two overlapping event"
    (let [events [{:start 1 :end 10}
                  {:start 1 :end 3}
                  {:start 4 :end 6}]]
      (vec (set (flatten (report-overlapping events))))
      )
    )
  )

(comment
  (clojure.test/run-tests)
  (sort-events [ {:start 1} {:start 10} {:start 5} {:start 0}])

  
  )
