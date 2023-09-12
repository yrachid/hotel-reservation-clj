# hotel-reservation-clj

# Problem Statement

## Hotel Reservation Problem

A hotel chain operating in Miami wishes to offer room reservation services over the internet. They have three hotels: Lakewood, Bridgewood
and Ridgewood. Each hotel has separate weekday and weekend(Saturday and Sunday) rates. There are special rates for rewards customers as a
part of loyalty programs. Each hotel has a rating assigned to it.

* __Lakewood__
  * __Rating__: 3
  * __Weekday__ rates:
    * 110 for Regular;
    * 80 for Rewards.
  * __Weekend__ rates:
    * 90 for Regular;
    * 80 for Rewards.

* __Bridgewood__
  * __Rating__: 4
  * __Weekday__ rates:
    * 160 for Regular
    * 110 for Rewards.
  * __Weekend__ rates:
    * 60 for Regular;
    * 50 for Rewards.

* Ridgewood
  * __Rating:__ 5
  * __Weekday__ rates:
    * 220 for Regular;
    * 100 for Rewards.
  * __Weekend__ rates:
    * 150 for Regular;
    * 40 for Rewards.

Write a program to help an online customer find the cheapest hotel.

The input to the program will be a range of dates for a regular or rewards customer. The
output should be the cheapest available hotel. In case of a tie, the hotel with the highest
rating should be returned.

INPUT FORMAT:

```
<customer_type>: <date1>, <date2>, <date3>, ...
```

OUTPUT FORMAT:

```
<name_of_the_cheapest_hotel>
```

Examples:

INPUT 1:

```
Regular: 16Mar2009(mon), 17Mar2009(tues), 18Mar2009(wed)
```

OUTPUT 1:

```
Lakewood
```

INPUT 2:

```
Regular: 20Mar2009(fri), 21Mar2009(sat), 22Mar2009(sun)
```
OUTPUT 2:

```
Bridgewood
```

INPUT 3:

```
Rewards: 26Mar2009(thur), 27Mar2009(fri), 28Mar2009(sat)
```

OUTPUT 3:

```
Ridgewood
```

## Usage

    $ echo <input> | java -jar hotel-reservation-clj-0.1.0-standalone.jar
