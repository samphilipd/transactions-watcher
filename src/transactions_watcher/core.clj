(ns transactions-watcher.core
  (:require [clojure-watch.core :refer [start-watch]]
            [clj-http.client :as client]
            [transactions-watcher.lib :refer :all]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string :refer [blank?]])
  (:import [java.io File RandomAccessFile])
  (:gen-class))

(def config (atom {}))

(defn upload-for-processing
  "Uploads the csv file for processing by the fosubo transactions importer"
  [filename]
  (try
    (let [response
      (client/post (@config :upload-url)
        {:multipart [{:name "file" :content (clojure.java.io/file filename)}
                     {:name "company_id" :content (@config :company-id)}
                     {:name "auth_token" :content (@config :auth-token)}]})]
      (println "SUCCESS - uploaded: " filename)
      (println "Response status: " (:status response)))
    (catch Exception e (println (str "FAIL - could not upload: " filename "\nReason: " (.getMessage e))))))

(defn on-create
  "Is called every time a file is created in the watch directory"
  [event filename]
  (println event filename)
  (when (and (= event :create) (csv? filename))
    (wait-until-completed filename)
    (upload-for-processing filename)))

(defn options-from-args [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      errors (exit 1 (error-msg errors))
      (blank? (:auth-token options)) (exit 1 (error-msg ["auth token is required, try -h to see usage"]))
      (blank? (:company-id options)) (exit 1 (error-msg ["company id is required, try -h to see usage"]))
      :else options)))

(defn -main
  "Start watching the directory and upload any CSV files that appear"
  [& args]
    (let [{:keys [auth-token company-id upload-url watch-dir] :as options} (options-from-args args)]
      (reset! config options)
      (start-watch [{:path watch-dir
                     :event-types [:create]
                     :bootstrap (fn [path] (println "Starting to watch " path))
                     :callback on-create
                     :options {:recursive true}}])))
