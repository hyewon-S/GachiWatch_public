from flask import Flask, request, jsonify
import joblib

app = Flask(__name__)

model_data = joblib.load("model.pkl")
cosine_sim = model_data['cosine_sim']
titles = model_data['titles']
content_ids = model_data['content_ids']  # 추가

def recommend(title=None, content_id=None, top_n=any):
    if content_id is not None:
        if content_id not in content_ids:
            return []
        idx = content_ids.index(content_id)
    elif title is not None:
        if title not in titles:
            return []
        idx = titles.index(title)
    else:
        return []

    sim_scores = list(enumerate(cosine_sim[idx]))
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
    sim_scores = sim_scores[1:top_n+1]
    recommended = []
    for i, score in sim_scores:
        recommended.append({
            "content_id": content_ids[i],
            "title": titles[i],
            "score": score
        })
    return recommended

@app.route("/recommend", methods=["GET"])
def recommend_api():
    title = request.args.get("title")
    content_id = request.args.get("content_id", type=int)
    top_n = request.args.get("top_n", type=int)

    recs = recommend(title=title, content_id=content_id, top_n=top_n)
    if not recs:
        return jsonify({"error": "content not found"}), 404
    return jsonify({"recommendations": recs})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
