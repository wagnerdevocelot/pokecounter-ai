(defproject pokecounter-ai "0.1.0-SNAPSHOT"
  :description "A web app that generates Pokemon counters using AI"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [hiccup "1.0.5"]
                 [clj-http "3.12.3"]
                 [cheshire "5.11.0"]
                 [org.clojure/data.json "2.4.0"]
                 [ring/ring-json "0.5.1"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler pokecounter-ai.handler/app
         :init pokecounter-ai.handler/init}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
