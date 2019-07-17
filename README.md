# Calendar

Code to generate overlapping events. An event a map with two keys :start and :end
Assumptions: start and end time are inclusive therefore 
{:start 1 :end 5} {:start 5 :end 10} are overlapping because they share start/end of 5

## Test

```
lein test
```
