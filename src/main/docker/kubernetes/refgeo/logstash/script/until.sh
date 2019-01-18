
i=1
echo ecoute $i
until nc -z -w 1 192.168.65.4 32000
do
    echo Waiting for elasticsearch cluster to get initialized $i
    i=$(($i+1))
    sleep 5
done
curl  -X PUT '192.168.65.4:32000/gazetter?pretty' -H 'Content-Type: application/json' -d '{
  "settings" : {
    "number_of_shards" : 1
  },
  "mappings" : {
    "doc" : {
      "properties" : {
        "asciiname" : { "type" : "text" },
        "countrycode" : { "type" : "text" },
        "latitude" : { "type" : "text" },
        "longitude" : { "type" : "text" },
        "name" : { "type" : "text" },
        "population" : { "type" : "text" },
        "location" : { "type" : "geo_point"}
      }
    }
  }
}'
