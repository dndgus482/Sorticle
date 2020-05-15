import os
import sys
import urllib.request
import json

client_id = "Ai7a6EzeqlKeo1V9Vwo0"
client_secret = "BSpQtNxWts"
encText = urllib.parse.quote("치킨")
url = "https://openapi.naver.com/v1/search/news?query=" + encText # json 결과
# url = "https://openapi.naver.com/v1/search/blog.xml?query=" + encText # xml 결과
request = urllib.request.Request(url)
request.add_header("X-Naver-Client-Id", client_id)
request.add_header("X-Naver-Client-Secret", client_secret)
response = urllib.request.urlopen(request)
rescode = response.getcode()
if(rescode!=200):
    print("Error Code:" + rescode)

response_body = response.read()
print(response_body.decode('utf-8'))

news_info = json.loads(response_body)

print(news_info['total'])



