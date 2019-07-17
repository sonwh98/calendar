(ns calendar.core-test
  (:require [clojure.test :refer :all]
            [calendar.core :as calendar]))

(deftest overlap-test
  (testing "no overlap"
    (is (not (calendar/overlap?
              {:start 1 :end 5}
              {:start 6 :end 10})))
    (is (not (calendar/overlap?
              {:start 6 :end 10}
              {:start 1 :end 5}))))

  (testing "overlap"
    (is (calendar/overlap?
         {:start 1 :end 5}
         {:start 4 :end 6}))

    (is (calendar/overlap?
         {:start 4 :end 6}
         {:start 1 :end 5})))

  (testing "overlap where one event is within the time of another"
    (is (calendar/overlap?
         {:start 2 :end 4}
         {:start 1 :end 5}))

    (is (calendar/overlap?
         {:start 1 :end 5}
         {:start 2 :end 4}))))

(deftest report-test
  (testing "no overlapping events"
    (let [events [{:start 1 :end 3}
                  {:start 4 :end 6}
                  {:start 7 :end 10}]]
      (is (empty? (calendar/report-overlapping events)))))

  (testing "two overlapping event"
    (let [events [{:start 1 :end 10}
                  {:start 1 :end 3}
                  {:start 4 :end 6}]
          pairs #{#{{:start 1 :end 10} {:start 1 :end 3}}
                  #{{:start 1 :end 10} {:start 4 :end 6}}}]
      (is (= pairs (calendar/report-overlapping events)))))

  (testing "three overlapping event"
    (let [events [{:start 1 :end 10}
                  {:start 1 :end 3}
                  {:start 4 :end 6}
                  {:start 7 :end 9}
                  {:start 2 :end 8}]
          expected-pairs #{#{{:start 7, :end 9} {:start 2, :end 8}}
                           #{{:start 1, :end 3} {:start 2, :end 8}}
                           #{{:start 1, :end 10} {:start 1, :end 3}}
                           #{{:start 1, :end 10} {:start 2, :end 8}}
                           #{{:start 4, :end 6} {:start 2, :end 8}}
                           #{{:start 1, :end 10} {:start 4, :end 6}}
                           #{{:start 7, :end 9} {:start 1, :end 10}}}
          report (calendar/report-overlapping events)]
      (prn "report" report)
      (is (= expected-pairs report)))
    )

  (testing "multiple overlapping event"
    (let [events [{:start 1 :end 10}
                  {:start 1 :end 3}
                  {:start 4 :end 6}
                  {:start 2 :end 5}]
          pairs #{#{{:start 1, :end 10} {:start 1, :end 3}}
                  #{{:start 1, :end 3} {:start 2, :end 5}}
                  #{{:start 4, :end 6} {:start 2, :end 5}}
                  #{{:start 1, :end 10} {:start 2, :end 5}}
                  #{{:start 1, :end 10} {:start 4, :end 6}}}]
      (is (= pairs (calendar/report-overlapping events))))))

(comment
  (clojure.test/run-tests)
  (sort-events [ {:start 1} {:start 10} {:start 5} {:start 0}])

  
  )
