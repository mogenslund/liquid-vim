(ns dk.salza.liquid-vim.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [dk.salza.liq.core :as liq]
            [dk.salza.liquid-vim.apps.vimtextapp :as vimtextapp]
            [dk.salza.liq.editor :as editor])
  (:gen-class))

;;http://tnerual.eriogerg.free.fr/vimqrc.html
(defn -main
  [& args]
  (apply liq/startup args)
  (editor/set-default-keymap @vimtextapp/keymap-normal)
  (editor/set-default-app vimtextapp/run)
  (editor/remove-buffer "scratch")
  (editor/new-buffer "scratch")
  (editor/set-global-key :f5 editor/eval-last-sexp)
  (editor/updated))
