import joblib

# 1. 모델 데이터 로드
model_data = joblib.load("model.pkl")

cosine_sim = model_data['cosine_sim']
content_ids = model_data['content_ids']
titles = model_data['titles']

# 2. 추천 함수
def recommend(title, top_n=5):
    if title not in titles:
        print(f"'{title}' 콘텐츠를 찾을 수 없습니다.")
        return []
    idx = titles.index(title)
    sim_scores = list(enumerate(cosine_sim[idx]))
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
    sim_scores = sim_scores[1:top_n+1]  # 자기 자신 제외
    recommended = [(titles[i], score) for i, score in sim_scores]
    return recommended

# 3. 테스트 실행
if __name__ == "__main__":
    test_title = input("추천을 받고 싶은 콘텐츠 제목을 입력하세요: ").strip()
    recommendations = recommend(test_title)
    if recommendations:
        print(f"\n'{test_title}'와(과) 비슷한 콘텐츠 추천 {len(recommendations)}개:")
        for title, score in recommendations:
            print(f"- {title} (유사도: {score:.3f})")
