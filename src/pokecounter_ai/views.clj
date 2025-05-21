(ns pokecounter-ai.views
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.form :as form]))

(def pokemon-generations
  ["1" "2" "3" "4" "5" "6" "7" "8" "9"])

(def pokemon-formats
  ["OU" "Ubers" "UU" "RU" "NU" "PU" "LC" "Monotype" "National Dex"])

(defn layout [title & content]
  (html5
   [:head
    [:meta {:charset "UTF-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    [:title title]
    (include-css "https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css")
    (include-js "https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js")]
   [:body
    [:div.container.mt-5
     [:h1.mb-4 "PokeCounterAI"]
     content]
    (include-js "/js/main.js")]))

(defn index-page []
  (layout "PokeCounterAI - Pokemon Counter Generator"
          [:div.row
           [:div.col-md-12
            [:form#counter-form {:action "/generate-counters" :method "POST"}
             [:div.mb-3
              [:label.form-label {:for "pokemon-builds"} "Pokémon Build(s)"]
              [:textarea#pokemon-builds.form-control {:name "pokemon-builds" :rows 15
                                                     :placeholder "Enter up to 6 Pokemon builds, in Pokemon Showdown format..."}]]

             [:div.row.mb-3
              [:div.col-md-6
               [:label.form-label {:for "generation"} "Generation"]
               (form/drop-down {:class "form-select"}
                              "generation"
                              pokemon-generations)]
              [:div.col-md-6
               [:label.form-label {:for "format"} "Format"]
               (form/drop-down {:class "form-select"}
                              "format"
                              pokemon-formats)]]

             [:button.btn.btn-primary.mb-4 {:type "submit"} "Generate Counters"]]

            [:div#results.mt-4
             [:h2 "Counter Results"]
             [:div#counters.d-none
              [:pre#counter-results.p-3.bg-light.rounded]
              [:button#copy-button.btn.btn-secondary.mt-2 {:data-clipboard-target "#counter-results"}
               "Copy to Clipboard"]]]]]))

(defn results-page [input results]
  (layout "PokeCounterAI - Results"
          [:div.row
           [:div.col-md-12
            [:h2 "Your Input"]
            [:pre.p-3.bg-light.rounded input]
            [:h2.mt-4 "Generated Counters"]
            [:pre#counter-results.p-3.bg-light.rounded results]
            [:button#copy-button.btn.btn-secondary.mt-2
             {:data-clipboard-target "#counter-results"}
             "Copy to Clipboard"]
            [:div.mt-4
             [:a.btn.btn-primary {:href "/"} "Generate More Counters"]]]]))