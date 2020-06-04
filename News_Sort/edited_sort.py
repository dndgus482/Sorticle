csv_file = pd.read_csv("코로나 result .csv",header = 0, encoding='CP949')


def getNVM_lemma(text):
    tokenizer = MeCab.Tagger()
    parsed = tokenizer.parse(text)
    word_tag = [w for w in parsed.split("\n")]
    pos = []
    #NNP: 고유명사 / VV :동사 / VA : 형용사 / VX: 보조 용언 / VCP : 긍정지정사 / VCN : 부정지정사 /  MAG :일반 부사
    tags = ['NNG','NNP', 'VV', 'VA', 'VX', 'VCP', 'VCN', 'MAG']
    for word_ in word_tag[:-2]:
        word = word_.split("\t")
        tag = word[1].split(",")
        if(len(word[0]) < 2 ):
            continue
        if(tag[-1]!='*'):
            t = tag[-1].split('/')
            if(len(t[0])>1 and ('VV' in t[1] or 'VA' in t[1] or 'VX' in t[1])):
                pos.append(t[0])
        else :
            if(tag[0] in tags):
                pos.append(word[0])
    return stop_words(pos)



def stop_words(word_token):
    #불용어 처리
    s_file_name = open("stopwords.txt", 'r', encoding = "CP949")
    
    stop_list = []
    
    for line in s_file_name.readlines():
        stop_list.append(line.rstrip())
        
    s_file_name.close()
    result = []
    
    for w in word_token:
        if w not in stop_list:
            result.append(w)
    return(result)
    
""""
    stop = "서울,기자,네이버,뉴스,대학,성균관,오후,뉴시스,구독,교육"
    stop_list = stop.split(',')
"""


    
    

def keyword_select(file, keyword):
    texts =[]
    
    
    texts.append(file)
    docs = pd.concat(texts, ignore_index = True)
    tf_vect = CountVectorizer(tokenizer = getNVM_lemma, min_df =2)
    dtm = tf_vect.fit_transform(docs['contents'])
    words_list = frequency(tf_vect, dtm)
    
    #keywords 5개 생성
 

    text = "단어_%i"%r
    text_words = open("%s.txt"%text, 'w')
    words = 0

    while words < 5:
        text_words.writelines(words_list[words])
        text_words.write("\n")
        words = words + 1
    text_words.close()
    
    
    csv_create(words_list, keyword)
        

    

def main():
    global r
    r = 0
    
    keyword = "keyword"
    keyword_select(csv_file,keyword)
    keyword = "keyword_1"
    r = r + 1
    first_1 = pd.read_csv("keyword_1.csv",header = 0, encoding='CP949')
    keyword_select(first_1,keyword)
    keyword = "keyword_2"
    r = r + 1
    first_2 = pd.read_csv("keyword_2.csv",header = 0, encoding='CP949')
    keyword_select(first_2,keyword)
    keyword = "keyword_3"
    r = r + 1
    first_3 = pd.read_csv("keyword_3.csv",header = 0, encoding='CP949')
    keyword_select(first_3,keyword)
    keyword = "keyword_4"
    r = r + 1
    first_4 = pd.read_csv("keyword_4.csv",header = 0, encoding='CP949')
    keyword_select(first_4,keyword)
    keyword = "keyword_5"
    r = r + 1
    first_5 = pd.read_csv("keyword_5.csv",header = 0, encoding='CP949')
    keyword_select(first_5,keyword)
    

                    

#본 파일 받아서 text_select 돌리기
#생성된 파일로 받기




# 빈도수 많은 순으로 재정리
    
def frequency(vect,dtm):
    vocab =  dict()
    for idx, word in enumerate(vect.get_feature_names()):
        vocab[word] = dtm.getcol(idx).sum()
    words=sorted(vocab.items(), key = lambda x:x[1], reverse= True)
    dic_words = dict(words)
    words_list = []
    
   
    #빈도수 빼고 단어만 저장
    for i in range(5):
        for key in dic_words.keys() :
            words_list.append(key)
        if r != 0 :
            list_file = open('단어_0.txt', 'r').read().split('\n')
            for i in range(5):
                words_list.remove(list_file[i])
   
    return words_list
    
def csv_create(words_list, keyword):
    repeat = 1
    while repeat < 6:
        outputfile = "%s_%i"%(keyword,repeat)
        outputfile = str(outputfile)
        output_file1= open('%s.csv'%outputfile,'w', newline='',encoding = 'CP949')
        repeat = repeat + 1
        keyword1 = csv_file.loc[csv_file['contents'].str.contains(words_list[repeat-1], na = False)]
        keyword1.to_csv(output_file1, index = False)

    

if __name__=='__main__':
    main()
