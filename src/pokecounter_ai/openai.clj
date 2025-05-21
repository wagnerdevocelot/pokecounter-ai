(ns pokecounter-ai.openai
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [environ.core :refer [env]]))

(def ^:private openai-api-key (or (env :openai-api-key) (System/getenv "OPENAI_API_KEY")))
(def ^:private openai-endpoint "https://api.openai.com/v1/chat/completions")

(defn- count-pokemon-in-builds
  "Count the number of Pokemon in the builds string"
  [pokemon-builds]
  (count (re-seq #"(?m)^[A-Za-z][\w\-\s]+\s+@\s+" pokemon-builds)))

(defn- build-prompt [pokemon-builds generation format]
  (let [num-pokemon (count-pokemon-in-builds pokemon-builds)]
    (str "I need Pokemon counter suggestions for the following team/Pokemon in Generation " generation
         " " format " format:\n\n" pokemon-builds
         "\n\nBased on Smogon (https://www.smogon.com/) information from forums, articles, and the Pokedex (https://www.smogon.com/dex/sv/pokemon/), "
         "please provide EXACTLY " num-pokemon " effective counter Pokemon with full builds in Pokemon Showdown format, one for each input Pokemon in the same order. "
         "The counters should be optimized to handle the given Pokemon and be viable in the " format " format. "
         "Each counter must include: name, item, ability, EVs, nature, and 4 moves. "
         "IMPORTANT: Return ONLY the Pokemon builds in the exact Pokemon Showdown format, with no additional text. "
         "Example format:\n\nScizor @ Heavy-Duty Boots\nAbility: Technician\nEVs: 252 Atk / 4 Def / 252 Spe\nJolly Nature\n- Bullet Punch\n- U-turn\n- Knock Off\n- Swords Dance")))

(defn generate-counters [pokemon-builds generation format]
  (if (nil? openai-api-key)
    {:error "OpenAI API key not found in environment variables"}
    (try
      (let [prompt (build-prompt pokemon-builds generation format)
            request-body {:model "gpt-3.5-turbo"
                         :messages [{:role "system"
                                     :content "You are a competitive Pokemon expert with deep knowledge of the metagame."}
                                    {:role "user"
                                     :content prompt}]
                         :temperature 0.7
                         :max_tokens 2000}
            response (client/post openai-endpoint
                      {:body (json/generate-string request-body)
                       :headers {"Authorization" (str "Bearer " openai-api-key)
                                 "Content-Type" "application/json"}
                       :as :json})
            result (get-in response [:body :choices 0 :message :content])]
        {:success true :result result})
      (catch Exception e
        {:error (str "Error calling OpenAI API: " (.getMessage e))}))))