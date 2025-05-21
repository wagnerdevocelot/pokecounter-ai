(ns pokecounter-ai.openai-test
  (:require [clojure.test :refer :all]
            [pokecounter-ai.openai :as openai]
            [clj-http.client :as client]
            [cheshire.core :as json]))

(deftest generate-counters-test
  (let [captured-request (atom nil)]
    (with-redefs [System/getenv (fn [var-name]
                                  (if (= var-name "OPENAI_API_KEY")
                                    "test-api-key"
                                    (System/getenv var-name)))
                  client/post (fn [url req-options]
                                (reset! captured-request (json/parse-string (:body req-options) true))
                                {:status 200
                                 :body {:choices [{:message {:content "Mocked response"}}]}})]

      (testing "with a specified llm-model"
        (openai/generate-counters "Pikachu" "1" "OU" "gpt-4")
        (is (some? @captured-request) "Request was made")
        (is (= "gpt-4" (:model @captured-request)) "Correct LLM model is used"))

      (testing "with a nil llm-model"
        (openai/generate-counters "Charmander" "1" "OU" nil)
        (is (some? @captured-request) "Request was made")
        (is (= "gpt-3.5-turbo" (:model @captured-request)) "Defaults to gpt-3.5-turbo for nil model"))

      (testing "with an empty string llm-model"
        (openai/generate-counters "Squirtle" "1" "OU" "")
        (is (some? @captured-request) "Request was made")
        (is (= "gpt-3.5-turbo" (:model @captured-request)) "Defaults to gpt-3.5-turbo for empty string model")))))
