import csv
import json
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import os
import pandas as pd

# Use a service account
cred = credentials.Certificate('firestore/sorticle-cb1408f9d61d.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

#
# config = {
#     "apiKey": "AIzaSyDKGNy2kGfnc02qFc1o6jhzjSMZ-pbiyGk",
#     "authDomain": "sorticle.firebaseapp.com",
#     "databaseURL": "https://sorticle.firebaseio.com",
#     "projectId": "sorticle",
#     "storageBucket": "sorticle.appspot.com",
#     "messagingSenderId": "303578128514",
#     "appId": "1:303578128514:web:bb691a6ada7515dd8f149b",
#     "measurementId": "G-VX5WDJ0WSJ"
# }
#
# firebase = pyrebase.initialize_app(config)
#
# db = firebase.database()


def savedata(filename):
    #카테고리 저장
    subkeyword, mainkeyword = getKeyword(filename)
    category_ref = db.collection('category').document('sub')
    # category_ref.set({"category" : filename}, merge=True)
    for i in range(5):
        category_ref = db.collection('category').document('sub').collection(filename).document('sub')
        category_ref.set({"category": mainkeyword}, merge=True)
        for j in range(5):
            category_ref = db.collection('category').document('sub').collection(filename).document('sub').collection(
                mainkeyword[i]).document('sub')
            category_ref.set({"category":subkeyword[i]}, merge=True)
            category_ref = db.collection('category').document('sub').collection(filename).document('sub').collection(
                mainkeyword[i]).document('sub').collection(subkeyword[i][j]).document('sub')
            category_ref.set({"category" : None}, merge=True)


    # 전체뉴스 저장
    news = []
    keyword = []
    mainfile = open("../" + filename + '.csv', 'r',  encoding="cp949")
    reader = csv.reader(mainfile)

    for row in reader: # each row is a list
        news.append(row)

    news[0].append("category")

    for i in news:
        keyword = []
        for j in range(5):
            if mainkeyword[j] in i[4]:
                if mainkeyword[j] not in keyword:
                    keyword.append(mainkeyword[j])
            for k in range(5):
                if subkeyword[j][k] in i[4]:
                    if subkeyword[j][k] not in keyword:
                        keyword.append(subkeyword[j][k])
        i.append(keyword)

    news_ref = db.collection('news')
    for i in range (len(news)-1):
        dict = {}
        data = {"search" : filename, "index" : news[i+1][0], "year" : news[i+1][1], "company" : news[i+1][2], "title" : news[i+1][3], "link" : news[i+1][5]}
        for j in range(len(news[i + 1][6])):
            dict[news[i + 1][6][j]] = True
        data['category'] = dict
        news_ref.add(data)


def getKeyword(filename):
    array = [[0]*5 for i in range(5)]
    temp = open(filename + " keyword.txt", 'r').read().split("\n")
    for i in range(5):
        sub = open(filename + "_" + str(i) + " keyword.txt", "r").read().split("\n")
        for j in range(5):
            array[i][j] = sub[j]
    return array, temp