(defproject transactions-watcher "0.1.0-SNAPSHOT"
  :description "Watches a directory and uploads any CSV files that appear"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clojure-watch "LATEST"]
                 [clj-http "1.1.0"]
                 [org.clojure/tools.cli "0.3.1"]]
  :main ^:skip-aot transactions-watcher.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
