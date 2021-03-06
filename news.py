
#pip install pandas
#pip install requests
#pip install openpyxl

import requests
from bs4 import BeautifulSoup
import pandas as pd
from datetime import datetime
RESULT_PATH="C:/Users/hablo/PycharmProjects/News/"
txt_filename = "news.txt"
now = datetime.now()


def crawler(maxpage, query):
    page = 1
    maxpage_t = (int(maxpage) - 1) * 10 + 1  # 11= 2페이지 21=3페이지 31=4페이지 ...81=9페이지 , 91=10페이지, 101=11페이지
    f = open(RESULT_PATH + query + txt_filename, 'w', encoding='utf-8')

    while page < maxpage_t:
        print(int(page/10 + 1),  '/',  maxpage)
        url = "https://search.naver.com/search.naver?where=news&query=" + query + "&sort=0&nso=so%3Ar%2Cp%3A%2Ca%3A&start=" + str(page)
        req = requests.get(url)
        # print(url)
        cont = req.content
        soup = BeautifulSoup(cont, 'html.parser')
        for urls in soup.select("._sp_each_url"):
            try:
                if urls["href"].startswith("https://news.naver.com"):
                    news_detail = get_news(urls["href"])
                    if news_detail[0].startswith("[시선집중]"): #형식이 이상한 뉴스는 제외
                        continue
                    # print(news_detail[4], news_detail[0], news_detail[2])
                    f.write(
                        "{}\t{}\t{}\t{}\t{}\n".format(news_detail[1], news_detail[4], news_detail[0], news_detail[2],
                                                      news_detail[3]))
            except Exception as e:
                # print(e)
                continue
        page += 10
    f.close()


def get_news(n_url):
    news_detail = []
    breq = requests.get(n_url)
    bsoup = BeautifulSoup(breq.content, 'html.parser')
    title = bsoup.select('h3#articleTitle')[0].text
    news_detail.append(title)
    pdate = bsoup.select('.t11')[0].get_text()[:11]
    news_detail.append(pdate)
    _text = bsoup.select('#articleBodyContents')[0].get_text().replace('\n', " ")
    btext = _text.replace("// flash 오류를 우회하기 위한 함수 추가 function _flash_removeCallback() {}", "")
    news_detail.append(btext.strip())
    news_detail.append(n_url)
    pcompany = bsoup.select('#footer address')[0].a.get_text()
    news_detail.append(pcompany)
    return news_detail


def excel_make(query):
    data = pd.read_csv(RESULT_PATH + query +txt_filename, sep='\t', header=None, error_bad_lines=False)
    data.columns = ['years', 'company', 'title', 'contents', 'link']
    print(data)
    xlsx_outputFileName = query + ' result.xlsx'
    data.to_excel(RESULT_PATH + xlsx_outputFileName, encoding='utf-8')

def main():
    maxpage = input("최대 검색할 페이지수 입력하시오: ")
    query = input("검색어 입력: ")
    crawler(maxpage, query)
    excel_make(query)
main()