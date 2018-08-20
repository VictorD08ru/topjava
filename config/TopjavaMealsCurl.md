curl /*getAll meals*/ -v http://localhost:8080/topjava/rest/meals

curl /*get meal 100002*/ -v http://localhost:8080/topjava/rest/meals/100002

curl /*getBetween nulls*/ -v http://localhost:8080/topjava/rest/meals/filter

curl /*getBetween nulls*/ -v 'http://localhost:8080/topjava/rest/meals/filter?startDate=2015-05-30&startTime=14:00'

curl /*update meal 100002*/ -H 'Content-Type: application/json' -X PUT -d '{"id":100002,"dateTime":"2015-05-30T10:00:00","description":"updated breakfast","calories":200}' -v http://localhost:8080/topjava/rest/meals/100002

curl /*delete meal 100007*/ -X DELETE -v http://localhost:8080/topjava/rest/meals/100007

curl /*createWithLocation meal*/ -H 'Content-Type: application/json' -X POST -d '{"dateTime":"2015-06-01T19:00:00","description":"Created dinner","calories":300}' -v http://localhost:8080/topjava/rest/meals