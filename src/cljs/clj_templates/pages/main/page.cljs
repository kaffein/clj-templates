(ns clj-templates.pages.main.page
  (:require [re-frame.core :refer [subscribe]]
            [clj-templates.components.header.component :refer [header]]
            [clj-templates.components.footer.component :refer [footer]]
            [clj-templates.components.error-boundary.component :refer [error-boundary]]
            [clj-templates.util.events :refer [listen]]
            [clj-templates.pages.templates.page :refer [templates]]))

(def page-panels {:templates [templates]})

(defn page-panel [page-name]
  (page-panels page-name))

(defn main-panel []
  (let [active-page (listen [:main/active-page])]
    [error-boundary
     [:div.app
      [header]
      [:main
       [page-panel active-page]]
      [footer]]]))
