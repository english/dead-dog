(ns dead-dog.app
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn widget [data]
  (om/component
    (dom/div nil "Hello world!")))

(om/root widget {}
  {:target (.getElementById js/document "app")})
