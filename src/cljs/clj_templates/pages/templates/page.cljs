(ns clj-templates.pages.templates.page
  (:require [clj-templates.util.events :refer [listen]]
            [clj-templates.util.js :refer [target-value]]
            [re-frame.core :refer [dispatch]]
            [clojure.string :as str]))

(defn boot-usage [template-name]
  (str "boot -d boot/new new -t " template-name "-n my-app"))

(defn lein-usage [template-name]
  (str "lein new " template-name "my-app"))

(defn template-panel [{:keys [template-name description build-system homepage downloads]}]
  [:div.template
   [:div
    [(if homepage :a.title :div.title)
     (when homepage {:href homepage}) template-name]
    [:div.description description]]
   [:div.info-row
    [:div.template-attribute [:div.keyword ":downloads "] [:div.code downloads]]
    [:div.template-icons
     (when (= build-system "lein")
       [:img {:src "images/leiningen.jpg" :width "20px"}])
     [:img {:src "images/boot-logo.png" :width "23px"}]]]])

(defn search-input []
  [:input.search-input {:type        "text"
                        :placeholder (str "Search templates")
                        :on-change   #(dispatch [:templates/delayed-search (target-value %)])}])

(defn pagination-link [page current-page-index]
  (let [page-active? (= page current-page-index)]
    [:a.pagination-link {:class    (when page-active? "current-page")
                         :on-click (when-not page-active? #(dispatch [:templates/page-change page]))}
     page]))

(defn pagination [page-count]
  (let [current-page-index (listen [:templates/current-page-index])]
    [:div.pagination
     (for [page (range 1 (inc page-count))]
       ^{:key page} [pagination-link page current-page-index])]))

(defn results [templates query-string error?]
  (cond
    error?
    [:div.results-for (str "Something went wrong when getting templates for \"" query-string "\"")]

    ;; No error, templates returned
    (seq templates)
    [:div.templates-listing
     (for [{:keys [template-name build-system] :as template} templates]
       ^{:key (str template-name build-system)} [template-panel template])]

    ;; No templates, string is not blank
    (not (str/blank? query-string))
    [:div.results-for (str "No results for \"" query-string "\"")]))

(defn results-for-text [templates query-string]
  (when (seq templates)
    (let [result-string (if (str/blank? query-string) "All templates:"
                                                      (str "Results for \"" query-string "\":"))]
      [:div.results-for result-string])))

(defn intro-text []
  [:div.intro-text "Find Clojure templates for "
   [:a {:href "https://leiningen.org/" :target "_blank"} "Leiningen"]
   " and "
   [:a {:href "http://boot-clj.com/" :target "_blank"} "Boot"]
   ". "])

(defn templates []
  (let [templates (listen [:templates/templates])
        query-string (listen [:templates/query-string])
        page-count (listen [:templates/page-count])
        error? (listen [:templates/error?])]
    [:div.templates
     [intro-text]
     [search-input]
     (when (pos? page-count) [pagination page-count])
     [results-for-text templates query-string]
     [results templates query-string error?]
     (when (< 1 page-count) [pagination page-count])]))
