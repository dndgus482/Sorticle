import pandas as pd
import save
from pandas import DataFrame as df
import MeCab
import os
import numpy as np
import csv
from sklearn.feature_extraction.text import CountVectorizer, TfidfVectorizer
from matplotlib import pyplot as plt
from nltk.corpus import stopwords

from pandas import DataFrame


def getNVM_lemma(text):
    tokenizer = MeCab.Tagger()
    parsed = tokenizer.parse(text)
    word_tag = [w for w in parsed.split("\n")]
    pos = []
    # NNP: 고유명사 / VV :동사 / VA : 형용사 / VX: 보조 용언 / VCP : 긍정지정사 / VCN : 부정지정사 /  MAG :일반 부사
    tags = ['NNG', 'NNP', 'VV', 'VA', 'VX', 'VCP', 'VCN', 'MAG']
    for word_ in word_tag[:-2]:
        word = word_.split("\t")
        tag = word[1].split(",")
        if len(word[0]) < 2:
            continue
        if tag[-1] != '*':
            t = tag[-1].split('/')
            if len(t[0]) > 1 and ('VV' in t[1] or 'VA' in t[1] or 'VX' in t[1]):
                pos.append(t[0])
        else:
            if tag[0] in tags:
                pos.append(word[0])
    return stop_words(pos)


def stop_words(word_token):
    # 불용어 처리
    s_file_name = open("../stopwords.txt", 'r', encoding="cp949")

    stop_list = []

    for line in s_file_name.readlines():
        stop_list.append(line.rstrip())

    s_file_name.close()
    result = []

    for w in word_token:
        if w not in stop_list:
            result.append(w)
    return (result)


""""
    stop = "서울,기자,네이버,뉴스,대학,성균관,오후,뉴시스,구독,교육"
    stop_list = stop.split(',')
"""


def keyword_select(file, keyword):
    texts = []

    texts.append(file)
    docs = pd.concat(texts, ignore_index=True)
    tf_vect = CountVectorizer(tokenizer=getNVM_lemma, min_df=2)
    dtm = tf_vect.fit_transform(docs['contents'])
    words_list = frequency(tf_vect, dtm, keyword)

    # keywords 5개 생성
    text_words = open(keyword + " keyword.txt", 'w')

    for words in range(4):
        text_words.writelines(words_list[words])
        text_words.write("\n")
    text_words.writelines(words_list[words + 1])
    text_words.close()

    csv_create(words_list, keyword, file)


def sort_main(filename):
    csv_file = pd.read_csv("../" + filename + ".csv", header=0, encoding='cp949')
    keyword = filename
    keyword_select(csv_file, keyword)
    for i in range(5):
        keyword = filename + '_' + str(i)
        first = pd.read_csv(keyword + ".csv", header=0, encoding='cp949')
        keyword_select(first, keyword)


# 본 파일 받아서 text_select 돌리기
# 생성된 파일로 받기


# 빈도수 많은 순으로 재정리


def frequency(vect, dtm, keyword):
    vocab = dict()
    for idx, word in enumerate(vect.get_feature_names()):
        vocab[word] = dtm.getcol(idx).sum()
    words = sorted(vocab.items(), key=lambda x: x[1], reverse=True)
    dic_words = dict(words)
    words_list = []

    # 빈도수 빼고 단어만 저장
    for i in range(5):
        for key in dic_words.keys():
            words_list.append(key)
        if len(keyword.split("_")) == 2:
            list_file = open(keyword.split("_")[0] + ' keyword.txt', 'r').read().split('\n')
            for i in range(5):
                if list_file[i] in words_list :
                    words_list.remove(list_file[i])
    if "위하" in words_list:
        words_list.remove("위하")
    if "데히" in words_list:
        words_list.remove("대하")
    if "따르" in words_list:
        words_list.remove("따르")
    return words_list


def csv_create(words_list, keyword, file):
    for repeat in range(5):
        outputfile = keyword + "_" + str(repeat)
        outputfile = str(outputfile)
        output_file1 = open(outputfile + '.csv', 'w', newline='', encoding='cp949')
        keyword1 = file.loc[file['contents'].str.contains(words_list[repeat], na=False)]
        keyword1.to_csv(output_file1, index=False)
