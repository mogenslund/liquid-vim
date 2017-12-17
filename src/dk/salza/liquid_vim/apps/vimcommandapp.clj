(ns dk.salza.liquid-vim.apps.vimcommandapp
  (:require [dk.salza.liq.editor :as editor]
            [dk.salza.liq.keys :as keys]
            [dk.salza.liq.coreutil :refer :all]))


(defn exitapp
  []
  (editor/delete-window)
  (editor/switch-to-buffer "-command-")
  (editor/kill-buffer)
  (editor/request-fullupdate)
  (editor/updated))

(def commands
  {:q editor/quit
   :quit editor/quit
   :q! editor/force-quit
   :quit! editor/force-quit
   :w editor/save-file
   :wq #(do (editor/save-file) (editor/quit))
   :wq! #(do (editor/save-file) (editor/force-quit))
   :edit #(editor/find-file %)
  })


(defn execute
  []
  (let [content (editor/get-content)
        [a cmd param] (re-find #":([^ ]*) ?(.*)" content)
        fun (commands (keyword cmd))]
    (exitapp)
    (when fun
      (if (= param "")
        (fun)
        (fun param)))
  ))

(def keymap
  (merge
    {:cursor-color :green
     :C-g exitapp
     :esc exitapp
     :enter execute
     :tab :typeahead-based-on-commands-keys
     :space #(editor/insert " ")
    }
    (keys/alphanum-mapping editor/insert)
    (keys/symbols-mapping editor/insert)))

(defn run
  []
  (editor/split-window-below 0.9)
  (editor/other-window)
  (editor/new-buffer "-command-")
  (editor/set-keymap keymap)
  (editor/insert ":"))
