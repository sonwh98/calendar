(ns calendar.core-test
  (:require [clojure.test :refer :all]
            [calendar.core :refer :all]))

(defn duration [evt]
  (- (:end evt)
     (:start evt)))

(defn within?
  "read evt2 within evt1"
  [evt1 evt2]
  (let [start1 (:start evt1)
        end1 (:end evt1)
        
        start2 (:start evt2)
        end2 (:end evt2)]
    (and (>= start2 start1)
         (<= end2 end1))))


(defn intersect? [evt1 evt2]
  (let [start1 (:start evt1)
        end1 (:end evt1)
        
        start2 (:start evt2)
        end2 (:end evt2)]
    (and (>= start2 start1)
         (>= end2 end1)
         (<= (+ (:start evt1)
                (duration evt1))
             end2)
         ))
  )

(defn over-lapping? [evt1 evt2] {:pre [(and (-> evt1 duration pos?)
                                            (-> evt2 duration pos?))]}
  
  (within? evt1 evt2))

(deftest overlap-test
  (testing "evt2 within evt1"
    (is (within?
         {:start 1 :end 5}
         {:start 1 :end 6}))
    
    )
  )


(comment
  (clojure.test/run-tests)
  )
