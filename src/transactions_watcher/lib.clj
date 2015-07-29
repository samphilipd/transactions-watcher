(ns transactions-watcher.lib
  (:require [clojure.string :as string :refer [blank?]]))

(defn wait-until-completed
  "To avoid uploading partially completed files (e.g. from SCP copy) we should wait
  some period after the create event occurs before triggering the upload.
  Ideally we would wait until we can get an exclusive lock on the file, for now
  30 seconds should be easily enough time."
  [filename]
  (Thread/sleep 30000))

(defn csv?
  "Returns true if the string filename ends in .csv"
  [filename]
  (boolean (re-find #"\.csv\Z" filename)))

(def present? (complement blank?))

(def cli-options
  [["-d" "--watch-directory DIRECTORY" "Directory to watch for uploaded files"
    :default "."
    :id :watch-dir
    :validate [#(.isDirectory (clojure.java.io/file %)) "Must be a valid directory (is it readable by this user?)"]]
   ["-u" "--upload-url URL" "Endpoint URL to POST to"
    :default "http://localhost:3000/api/v1/upload_transactions"
    :id :upload-url]
   ["-t" "--token TOKEN" "Auth token for the fosubo API (required)"
    :id :auth-token
    :validate [present? "Auth token is required"]]
   ["-c" "--company-id COMPANY_ID" "The id of the company in the fosubo database (required)"
    :id :company-id
    :validate [present? "A company id is required"]]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Sam's directory watcher and file uploader."
        "Watches for addition of CSV
         transaction files to the specified directory and POSTs them to the
         Fosubo API."
        ""
        "Usage: java -jar transactions-watcher.jar [options]"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))
