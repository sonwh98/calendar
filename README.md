# Calendar

An event is a map with two keys :start and :end
Assumptions: start and end time are inclusive therefore 
{:start 1 :end 5} {:start 5 :end 10} are overlapping because they share start/end of 5

generate-overlapping is a function that takes sequence of events and returns a set of
overlapping event pairs

## Test

```
lein test
```
