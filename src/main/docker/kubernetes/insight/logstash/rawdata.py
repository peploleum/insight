# -*- coding: UTF-8 -*-

import json
import sys
import requests
import datetime

try:
    # print(" début script")
    accountUrl = "http://insight.insight:8080/api/account"
    authenticationUrl = "http://insight.insight:8080/api/authentication"
    urlpostrawdata = "http://insight.insight:8080/api/raw-data"
    payload = {
        'j_username': 'admin',
        'j_password': 'admin',
        'remember-me': 'true',
        'submit': 'Login'
    }
    with requests.Session() as session:
        myResponse = session.get(accountUrl, verify=True)
        # print(" Account")
        if myResponse.status_code == 401:
            token = session.cookies.get("XSRF-TOKEN")
            headers = {
                'Accept': 'application/json',
                'Connection': 'keep-alive',
                'X-XSRF-TOKEN': token
            }
            # print(" Autentification " + token)
            authResponse = session.post(url=authenticationUrl, data=payload, verify=True, headers=headers)
            if authResponse.ok:
                date = datetime.datetime.now().isoformat()
                headersRawData = {
                    'Accept': 'application/json',
                    'Content-type': 'application/json',
                    'X-XSRF-TOKEN': authResponse.cookies.get("XSRF-TOKEN")
                }
                payloadRawData = {
                    "rawDataType": sys.argv[1],
                    "rawDataName": sys.argv[2],
                    "rawDataContent": sys.argv[3],
                    "rawDataSourceType": sys.argv[4],
                    "rawDataSubType": sys.argv[5],
                    "rawDataCreationDate": date
                }
                print("postDataRawResponse")
                postDataRawResponse = session.post(url=urlpostrawdata, json=payloadRawData, headers=headersRawData)
                print(postDataRawResponse.json())
            else:
                print("Auth failed")
        else:
            # For successful API call, response code will be 200 (OK)
            if myResponse.ok:
                jData = json.loads(myResponse.content)
                print("The response contains {0} properties".format(len(jData)))
                print("\n")
                for key in jData:
                    print(key + " : " + jData[key])
            else:
                # If response code is not ok (200), print the resulting http error code with description
                myResponse.raise_for_status()
finally:
# print(" Out ")
exit(0)
