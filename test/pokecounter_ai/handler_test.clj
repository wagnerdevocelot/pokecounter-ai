(ns pokecounter-ai.handler-test
  (:require [clojure.test :refer [deftest is testing]]
            [ring.mock.request :as mock]
            [pokecounter-ai.handler :refer [app]]
            [pokecounter-ai.openai :as openai]
            [pokecounter-ai.views :as views]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (string? (:body response))) "Response body should be a string (HTML)"))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))

(deftest generate-counters-route-test
  (let [captured-args (atom nil)]
    (with-redefs [openai/generate-counters (fn [& args]
                                             (reset! captured-args args)
                                             {:success true :result "Mocked AI Result"})
                  views/results-page (fn [input results]
                                       (str "Results page: " input " -> " results))
                  views/error-page (fn [input error-message]
                                     (str "Error page: " input " -> " error-message))]

      (testing "POST /generate-counters with llm-model"
        (let [form-params {"pokemon-builds" "Pikachu"
                           "generation" "1"
                           "format" "OU"
                           "llm-model" "gpt-4-test"}
              response (app (-> (mock/request :post "/generate-counters")
                                (mock/body form-params) ; Compojure extracts from form-params, not body for POSTs with keywords
                                (assoc :form-params form-params)))] ; Ensure form-params are correctly set for destructuring

        (is (= 200 (:status response)) "Response status should be 200")
        (is (some? @captured-args) "openai/generate-counters should have been called")
        (is (= 4 (count @captured-args)) "openai/generate-counters should be called with 4 arguments")
        (is (= "Pikachu" (nth @captured-args 0)) "First argument should be pokemon-builds")
        (is (= "1" (nth @captured-args 1)) "Second argument should be generation")
        (is (= "OU" (nth @captured-args 2)) "Third argument should be format")
        (is (= "gpt-4-test" (nth @captured-args 3)) "Fourth argument should be llm-model"))

      (testing "POST /generate-counters without llm-model (should pick default in openai ns)"
        (reset! captured-args nil) ; Reset for next call
        (let [form-params {"pokemon-builds" "Charmander"
                           "generation" "2"
                           "format" "UU"}
              ; llm-model is intentionally omitted
              response (app (-> (mock/request :post "/generate-counters")
                                (mock/body form-params)
                                (assoc :form-params form-params)))]

        (is (= 200 (:status response)) "Response status should be 200")
        (is (some? @captured-args) "openai/generate-counters should have been called")
        (is (= 4 (count @captured-args)) "openai/generate-counters should be called with 4 arguments")
        (is (= "Charmander" (nth @captured-args 0)))
        (is (= "2" (nth @captured-args 1)))
        (is (= "UU" (nth @captured-args 2)))
        (is (nil? (nth @captured-args 3)) "Fourth argument (llm-model) should be nil if not provided"))))))
