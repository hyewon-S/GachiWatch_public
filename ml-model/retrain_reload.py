import subprocess
import requests
import time

# 1) model.py 재학습 실행 (동기)
print("[INFO] 모델 재학습 시작...")
subprocess.run(["python3", "model.py"], check=True)
print("[INFO] 모델 재학습 완료.")

# 2) Flask 서버에 재로딩 API 호출
reload_url = "http://localhost:5000/reload_model"

try:
    print("[INFO] Flask 모델 재로딩 요청 중...")
    response = requests.get(reload_url, timeout=10)
    if response.status_code == 200:
        print("[SUCCESS] Flask 모델 재로딩 완료:", response.json())
    else:
        print("[ERROR] Flask 재로딩 실패 상태코드:", response.status_code)
except Exception as e:
    print("[ERROR] Flask 재로딩 요청 중 예외 발생:", e)
