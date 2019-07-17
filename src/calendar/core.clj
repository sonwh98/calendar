(ns calendar.core
  (:gen-class))

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
    (set (flatten (for [e1 sorted-events
                        :let [other (remove #(= % e1) sorted-events)
                              pairs (remove nil? (for [e2 (remove #(= % e1) sorted-events)]
                                                   (if (overlap? e1 e2)
                                                     #{e1 e2}
                                                     nil)))]]
                    pairs)))))

