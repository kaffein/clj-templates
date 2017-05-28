(ns clj-templates.search-test
  (:require [integrant.core :as ig]
            [clojure.test :refer :all]
            [clj-templates.test-utils :refer [example-templates instrument-test test-config]]
            [clj-templates.search :as search :refer [base-url]]))

(defn index-test-templates [es-client]
  (doseq [template example-templates]
    (search/index-template es-client template)))

(def test-es-client (atom nil))

(defn reset-system [f]
  (with-redefs [search/base-url [:clj_templates_dev]
                search/index-url [:clj_templates_dev :templates]
                search/search-url [:clj_templates_dev :templates :_search]]
    (let [system (ig/init (select-keys test-config [:search/elastic]))
          es-client (:search/elastic system)]
      (reset! test-es-client es-client)
      (index-test-templates es-client)
      (f)
      (ig/halt! system))))

(use-fixtures :each reset-system instrument-test)

(deftest search
  (testing "We can find all templates"
    (is (= (set example-templates)
           (set (search/match-all-templates @test-es-client)))))

  (testing "Searching gives a relevant result"
    (is (= (first example-templates)
           (first (search/search-templates @test-es-client "Foo"))))))
