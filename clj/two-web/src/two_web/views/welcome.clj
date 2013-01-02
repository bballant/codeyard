(ns two-web.views.welcome
  (:require [two-web.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]
        [hiccup.page :only [include-css html5]]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to two-web"]))

(defpage "/my-page" []
         (html5
           [:h1 "Woah, this is my page!"]))
