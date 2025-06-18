import cx_Oracle
import oracledb
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
from sklearn.metrics.pairwise import cosine_similarity
import joblib

#dsn = cx_Oracle.makedsn("dblab.dongduk.ac.kr", 1521, service_name="orclpdb")
#conn = cx_Oracle.connect(user="s250107", password="42702", dsn=dsn)

#oracledb.init_oracle_client()

username = "s250107"
password = "42702"
dsn = "dblab.dongduk.ac.kr/orclpdb"

#connection = oracledb.connect(user=username, password=password, dsn=dsn)
#conn = oracledb.connect(user=username, password=password, dsn=dsn)

# 2. 콘텐츠 + 장르 조인 쿼리
genre_map = {
    878: "SF",
    10770: "TV 영화",
    10751: "가족",
    27: "공포",
    99: "다큐멘터리",
    18: "드라마",
    10749: "로맨스",
    12: "모험",
    9648: "미스터리",
    80: "범죄",
    37: "서부",
    53: "스릴러",
    16: "애니메이션",
    28: "액션",
    36: "역사",
    10402: "음악",
    10752: "전쟁",
    35: "코미디",
    14: "판타지"
}


# 3. DB 쿼리문
query_content = """
SELECT CONTENT_ID, TITLE, INTRO FROM CONTENT
"""

query_content_genre = """
SELECT CONTENT_ID, GENRE_ID FROM CONTENT_GENRE
"""

# 4. DB 연결 및 데이터 조회
conn = oracledb.connect(user=username, password=password, dsn=dsn)
df_content = pd.read_sql(query_content, conn)
df_genre = pd.read_sql(query_content_genre, conn)
conn.close()

# 5. 장르 ID를 장르 이름으로 매핑
df_genre['GENRE_NAME'] = df_genre['GENRE_ID'].map(genre_map)

# 6. 콘텐츠별 장르 합치기 (구분자 '|')
genre_agg = df_genre.groupby('CONTENT_ID')['GENRE_NAME'].apply(lambda x: '|'.join(x.dropna())).reset_index()

# 7. 콘텐츠와 장르 병합
df = pd.merge(df_content, genre_agg, on='CONTENT_ID', how='left')

# 8. 특징 문자열 생성 (INTRO + 장르)
df['features'] = df.apply(lambda row: f"{row['INTRO'] or ''} {row['GENRE_NAME'] or ''}", axis=1)

# 9. TF-IDF 벡터화 (한글은 stop_words='english' 무관, 필요시 커스텀 가능)
tfidf = TfidfVectorizer(stop_words='english')
tfidf_matrix = tfidf.fit_transform(df['features'])

# 10. 코사인 유사도 계산
cosine_sim = cosine_similarity(tfidf_matrix, tfidf_matrix)

# 11. 모델 저장 (유사도 행렬, 콘텐츠 정보, tfidf 벡터라이저)
joblib.dump({
    'cosine_sim': cosine_sim,
    'content_ids': df['CONTENT_ID'].tolist(),
    'titles': df['TITLE'].tolist(),
    'tfidf': tfidf,
    'features': df['features'].tolist()
}, "model.pkl")

print("✅ 학습 완료 및 model.pkl 저장 완료")