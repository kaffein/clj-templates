(ns user
  (:require [clj-templates.core]
            [clj-templates.config.main-config :refer [main-config]]
            [config.dev-config :refer [dev-config]]
            [integrant.repl :refer [clear go halt init reset reset-all set-prep!]]
            [integrant.repl.state :refer [config system]]
            [clojure.repl :refer [apropos doc dir find-doc pst]]
            [integrant.core :as ig]
            [figwheel-sidecar.repl-api :as ra]
            [figwheel-dev]
            [pretty-dev]
            [clj-templates.db.db :as db]
            [clj-templates.logger]
            [clojure.java.io :as io]
            [migratus.core :as migratus]
            [clj-templates.clojars-data :refer [extract-templates-from-gzip-stream adapt-template-to-db]]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.alpha :as s]
            [clj-templates.github-data :as github-data]
            [clj-templates.clojars-data :as clojars-data]
            [clj-templates.jobs :as jobs]
            [cheshire.core :as json]
            [clojure.string :as str]
            [qbits.spandex :as es]
            [qbits.spandex.utils :as es-utils]
            [clj-templates.search :as search]))

(defn migratus-config []
  {:store         :database
   :migration-dir "migrations"
   :db            (:db/postgres system)})

(defn get-config []
  (merge main-config dev-config))

(defn cljs-repl []
  (ra/cljs-repl))

(defn migrate []
  (migratus/migrate (migratus-config)))

(defn rollback []
  (migratus/migrate (migratus-config)))

(defn bootstrap []
  (db/upsert-templates
    (:db/postgres system)
    (map (comp (fn [template] (merge template {:downloads nil :homepage nil})) adapt-template-to-db)
         (extract-templates-from-gzip-stream (io/input-stream "dev/resources/test_feed_big.clj.gz")))))

(integrant.repl/set-prep! get-config)

(comment
  (search/create-index (:search/elastic system))
  (search/delete-index (:search/elastic system))
  (time (jobs/do-jobs (:db/postgres system) (:search/elastic system)))
  (search/match-all-templates (:search/elastic system) 0 30)
  (github-data/get-github-rate-limit)
  (bootstrap)
  (stest/instrument)
  (stest/unstrument))
