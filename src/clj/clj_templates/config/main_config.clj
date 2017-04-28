(ns clj-templates.config.main-config
  (:require [integrant.core :as ig]))

(def main-config
  {:handler/main  {:name "test"}
   :server/jetty  {:handler (ig/ref :handler/main)
                   :opts    {:port 3000 :join? false}}
   :logger/timbre {:appenders {:println {:stream :auto}}}})
