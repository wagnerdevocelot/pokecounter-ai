(ns pokecounter-ai.openai
  (:require [clj-http.client :as client]
            [cheshire.core :as json]))

(def ^:private openai-endpoint "https://api.openai.com/v1/chat/completions")

(defn- count-pokemon-in-builds
  "Count the number of Pokemon in the builds string"
  [pokemon-builds]
  (count (re-seq #"(?m)^[A-Za-z][\w\-\s]+\s+@\s+" pokemon-builds)))

(defn- build-prompt [pokemon-builds generation format]
  (let [num-pokemon (count-pokemon-in-builds pokemon-builds)]
    (str "I need " num-pokemon " Pokemon counter suggestions for the following Pokemon in Generation " generation
         " " format " format:\n\n" pokemon-builds
         "\n\nSearch the Smogon website (www.smogon.com) for current competitive information about these Pokemon."
         "\nFor each Pokemon above, provide exactly one effective counter in Pokemon Showdown format."
         "\nReturn the counters in the SAME ORDER as the input Pokemon, so the first counter counters the first Pokemon, etc."
         "\nEach counter must include: name, item, ability, EVs, nature, and 4 moves."
         "\nRespond ONLY with the Pokemon builds in Pokemon Showdown format, with no additional text or explanation."
         "\nExample format:\n\nScizor @ Heavy-Duty Boots\nAbility: Technician\nEVs: 252 Atk / 4 Def / 252 Spe\nJolly Nature\n- Bullet Punch\n- U-turn\n- Knock Off\n- Swords Dance")))

(defn generate-counters [pokemon-builds generation format llm-model]
  (let [api-key (System/getenv "OPENAI_API_KEY")
        selected-model (if (and llm-model (not (empty? llm-model)))
                         llm-model
                         "gpt-3.5-turbo")]
    (if (nil? api-key)
      {:error "OPENAI_API_KEY environment variable not set. Please export it before running the application."}
      (try
        (let [prompt (build-prompt pokemon-builds generation format)
              request-body {:model selected-model
                           :messages [{:role "system"
                                      :content "You are a competitive Pokemon expert with deep knowledge of the metagame."}
                                     {:role "user"
                                      :content prompt}]
                           :temperature 0.7
                           :max_tokens 2000}
              response (client/post openai-endpoint
                        {:body (json/generate-string request-body)
                         :headers {"Authorization" (str "Bearer " api-key)
                                   "Content-Type" "application/json"}
                         :as :json})
              result (get-in response [:body :choices 0 :message :content])]
          {:success true :result result})
        (catch Exception e
          (println "Error calling OpenAI API:" (.getMessage e))
          {:error (str "Error calling OpenAI API: " (.getMessage e))})))))