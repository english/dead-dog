(ns dead-dog.handler
  (:require [clojure.java.jdbc :as sql]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [redirect response file-response]]))

(def db (or (System/getenv "DATABASE_URL")
            "postgresql://dead_dog:dead_dog@localhost:5432/dead_dog_dev"))

(defn create-snitches-table []
  (sql/execute! db ["create table if not exists snitches (
                      snitch_id serial primary key,
                      name varchar(255)
                    );"]))

(defn migrate []
  (create-snitches-table))

(defn create-snitch [name]
  (sql/insert! db :snitches {:name name}))

(defn find-all-snitches []
  (sql/query db ["SELECT * FROM snitches"]))

(defn snitches-index []
  (response {:snitches (map #(select-keys % [:name]) (find-all-snitches))}))

(defroutes app-routes
  (GET "/" [] (file-response "resources/public/index.html"))
  (GET "/snitches" [] (snitches-index))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-json-response (handler/site app-routes)))
