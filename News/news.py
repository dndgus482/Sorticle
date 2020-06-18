
#pip install pandas
#pip install requests
#pip install openpyxl
import shutil
import requests
from bs4 import BeautifulSoup
import pandas as pd
from datetime import datetime
import sort
import os
import save


def crawler(maxpage, query):
    page = 1
    maxpage_t = (int(maxpage) - 1) * 10 + 1  # 11= 2페이지 21=3페이지 31=4페이지 ...81=9페이지 , 91=10페이지, 101=11페이지
    f = open(query + "news.txt", 'w', encoding='cp949')

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
    _btext = _text.replace("// flash 오류를 우회하기 위한 함수 추가 function _flash_removeCallback() {}", "")
    btext = _btext.replace("동영상 뉴스", "")
    news_detail.append(btext.strip())
    news_detail.append(n_url)
    pcompany = bsoup.select('#footer address')[0].a.get_text()
    news_detail.append(pcompany)
    return news_detail


def csv_make(query):
    data = pd.read_csv(query + "news.txt", sep='\t', header=None, error_bad_lines=False, encoding = 'cp949')
    data.columns = ['years', 'company', 'title', 'contents', 'link']
    csv_outputFileName = query + '.csv'
    data.to_csv(csv_outputFileName, encoding='cp949', index_label = "index")

def main():
    #maxpage = input("최대 검색할 페이지수 입력하시오: ")
    maxpage = 20
    query = input("검색어 입력: ")
    crawler(maxpage, query)
    csv_make(query)
    currentPath = os.getcwd()
    os.mkdir(currentPath + "/" + query)
    os.chdir(currentPath + "/" + query)
    sort.sort_main(query)
    save.savedata(query)
    os.chdir(currentPath)
    shutil.rmtree(currentPath + "/" + query)


if __name__=='__main__':
    main()