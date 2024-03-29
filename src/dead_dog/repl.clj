(ns dead-dog.repl
  (:require [dead-dog.handler :refer :all]
            [ring.middleware.file :refer :all]
            [ring.middleware.file-info :refer :all]
            [ring.server.standalone :refer :all]))

(defonce server (atom nil))

(defn get-handler []
  ;; #'app expands to (var app) so that when we reload our code,
  ;; the server is forced to re-resolve the symbol in the var
  ;; rather than having its own copy. When the root binding
  ;; changes, the server picks it up without having to restart.
  (-> #'app
    ; Makes static assets in $PROJECT_DIR/resources/public/ available.
    (wrap-file "resources")
    ; Content-Type, Content-Length, and Last Modified headers for files in body
    (wrap-file-info)))

(defn start-server
  "used for starting the server in development mode from REPL"
  []
  (reset! server
          (serve (get-handler)
                 {:port 3000
                  :open-browser? false
                  :auto-reload? true
                  :join true}))
  (println (str "You can view the site at http://localhost:3000")))

(defn stop-server []
  (.stop @server)
  (reset! server nil))
