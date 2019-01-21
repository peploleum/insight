
i=1
echo ecoute $i
until nc -z -w 1 refgeo-elasticsearch 9200
do
    echo Waiting for elasticsearch cluster to get initialized $i
    i=$(($i+1))
    sleep 5
done
curl  -X PUT 'refgeo-elasticsearch:9200/gazetter?pretty' -H 'Content-Type: application/json' -d '{
  "settings" : {
        "number_of_shards" : 1
    },
    "mappings" : {
        "doc" : {
            "properties" : {
                "geonameid" : { "type" : "text" },
				"sciiname" : { "type" : "text" },
				"countrycode" : { "type" : "text" },
				"fclass" : { "type" : "text" },
				"fcode" : { "type" : "text" },
				"latitude" : { "type" : "text" },
				"longitude" : { "type" : "text" },
				"name" : { "type" : "text" },
				"population" : { "type" : "text" },
				"location" : { "type" : "geo_point"}
            }
        }
    }
}'
