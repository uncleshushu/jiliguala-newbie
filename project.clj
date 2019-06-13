(defproject newbie "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [metosin/compojure-api "1.1.11"]
                 ;; https://mvnrepository.com/artifact/ring/ring-jetty-adapter
                 [ring/ring-jetty-adapter "1.7.1"]
                 ;; https://mvnrepository.com/artifact/ring/ring-devel
                 [ring/ring-devel "1.7.1"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.apache.logging.log4j/log4j-slf4j-impl "2.11.2"]
                 [ring/ring-defaults "0.3.2"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler newbie.handler/app}
  :profiles {
             :dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                  [ring/ring-mock "0.3.2"]]
                   :resource-paths ["resources/dev"]}
             :prod {:resource-paths ["resources/prod"]}}
  )
