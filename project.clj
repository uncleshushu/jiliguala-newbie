(defproject jiliguala-newbie "0.2.0-SNAPSHOT"
  :description "Jiliguala newbie project"
  :url "https://github.com/uncleshushu/jiliguala-newbie"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.apache.logging.log4j/log4j-slf4j-impl "2.12.0"]
                 [http-kit "2.3.0"]
                 [ring/ring-codec "1.1.2"]
                 [cheshire "5.8.1"]
                 [metosin/compojure-api "1.1.12"]
                 [digest "1.4.9"]
                 ;; explicitly set dependency `ordered`'s version to workaround some bug
                 [org.flatland/ordered "1.5.7"]
                 ]
  :ring {:handler jiliguala-newbie.core/app
         :port    3000
         }
  :target-path "target/%s/"
  :profiles {:dev {:resource-paths ["resources/dev" "resources/private"]
                   :dependencies [[javax.servlet/javax.servlet-api "4.0.1"]
                                  [ring/ring-mock "0.4.0"]
                                  ]
                   :plugins      [[lein-ring "0.12.5"]
                                  [lein-cloverage "1.1.1"]
                                  ]
                   :ring         {:nrepl {:start? true :port 3001}
                                  }
                   }
             :prod {:resource-paths ^:replace ["resources/prod" "resources/private"]
                    }
             ;:github-release {:resource-paths ^:replace ["resources/prod"]
                              ;}
             :uberjar {:aot :all
                       ;:uberjar-name "server.jar"
                       ;:main jiliguala-newbie.core
                       }
             }
  )
