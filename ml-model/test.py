import joblib
import numpy as np

model_data = joblib.load("model.pkl")

cosine_sim = model_data['cosine_sim']
content_ids = model_data['content_ids']
titles = model_data['titles']

print(f"콘텐츠 수: {len(content_ids)}")
print("첫 5개 콘텐츠 제목:")
print(titles[:5])

print("코사인 유사도 행렬 크기:", cosine_sim.shape)
print("첫 콘텐츠와 나머지 콘텐츠 유사도:")
print(cosine_sim[0])
