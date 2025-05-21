(ns pokecounter-ai.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [pokecounter-ai.views :as views]
            [pokecounter-ai.openai :as openai]
            [environ.core :refer [env]]))

(defn init []
  (println "PokeCounterAI application starting...")
  (when (nil? (or (env :openai-api-key) (System/getenv "OPENAI_API_KEY")))
    (println "WARNING: OpenAI API key not found in .env file or environment variables. API calls will fail.")))

(defroutes app-routes
  (GET "/" [] (views/index-page))

  (POST "/generate-counters" [pokemon-builds generation format]
    (let [result (openai/generate-counters pokemon-builds generation format)]
      (if (:error result)
        {:status 500
         :body {:error (:error result)}}
        (views/results-page pokemon-builds (:result result)))))

  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body)
      (wrap-json-response)
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))