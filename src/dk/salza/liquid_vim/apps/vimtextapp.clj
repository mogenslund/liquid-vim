(ns dk.salza.liquid-vim.apps.vimtextapp
  (:require [dk.salza.liq.editor :as editor]
            [dk.salza.liq.keys :as keys]
            [dk.salza.liq.apps.promptapp :as promptapp]
            [dk.salza.liq.extensions.headlinenavigator]
            [dk.salza.liq.extensions.linenavigator]
            [dk.salza.liq.syntaxhl.clojuremdhl :as clojuremdhl]
            [dk.salza.liq.syntaxhl.javascripthl :as javascripthl]
            [dk.salza.liq.syntaxhl.pythonhl :as pythonhl]
            [dk.salza.liq.syntaxhl.xmlhl :as xmlhl]
            [dk.salza.liq.syntaxhl.latexhl :as latexhl]
            [dk.salza.liq.coreutil :refer :all]
            [dk.salza.liquid-vim.apps.vimcommandapp :as vimcommandapp]))

(def keymap-insert (atom {}))
(def keymap-normal (atom {}))

(reset! keymap-normal
  {:cursor-color :blue
   :space editor/forward-char
   ;:C-s editor/search
   :colon vimcommandapp/run
   :right editor/forward-char
   :left editor/backward-char
   :up editor/backward-line
   :down editor/forward-line
   :C-s #(promptapp/run editor/find-next '("SEARCH"))
   :v editor/selection-toggle
   :g {:g editor/beginning-of-buffer
       :t editor/top-align-page
       :n editor/top-next-headline
       :c #(editor/prompt-append (str "--" (editor/get-context) "--"))
       :i dk.salza.liq.extensions.headlinenavigator/run}
   :dash editor/top-next-headline
   :C-g editor/escape
   :e editor/end-of-word
   :E editor/evaluate-file
   :C-e editor/evaluate-file-raw
   :l editor/forward-char
   :h editor/backward-char
   :k editor/backward-line
   :j editor/forward-line
   :o (fn [] (do (editor/insert-line) (editor/set-keymap @keymap-insert)))
   :i #(editor/set-keymap @keymap-insert)
   :a #(do (editor/forward-char) (editor/set-keymap @keymap-insert))
   :0 editor/beginning-of-line
   :G editor/end-of-buffer
   :dollar #(do (editor/end-of-line) (editor/backward-char))
   :x editor/delete-char
   :m editor/previous-real-buffer 
   :H editor/record-macro
   :n editor/find-next
   :O editor/context-action
   :w editor/forward-word
   :K editor/swap-line-down
   :I #(do (editor/beginning-of-line) (editor/set-keymap @keymap-insert))
   :A #(do (editor/end-of-line) (editor/set-keymap @keymap-insert))
   :r (merge {:space #(editor/replace-char " ")}
             (keys/alphanum-mapping editor/replace-char)
             (keys/symbols-mapping editor/replace-char))
   :1 editor/select-sexp-at-point
   :y {:y #(do (or (editor/copy-selection) (editor/copy-line)) (editor/selection-cancel))}
   :p {:p #(do (editor/insert-line) (editor/paste) (editor/beginning-of-line))
       :h editor/paste}
   :d {:d #(do (or (editor/delete-selection) (editor/delete-line)) (editor/selection-cancel))}
   :s editor/save-file
         :u editor/undo
   :C-w editor/kill-buffer
   :C-t editor/tmp-test
   ;:C-t #(promptapp/run str '("a" "tadaa"))
   })

(reset! keymap-insert
  (merge
    {:cursor-color :green
     :tab #(editor/insert "\t")
     :esc #(do (editor/backward-char) (editor/set-keymap @keymap-normal))
     :right editor/forward-char
     :left editor/backward-char
     :up editor/backward-line
     :down editor/forward-line
     :space #(editor/insert " ")
     :enter #(editor/insert "\n")
     :C-t #(editor/insert "\t")
     :backspace editor/delete
     :C-g editor/escape
     :C-w editor/kill-buffer
     :C-s #(promptapp/run editor/find-next '("SEARCH"))} ;editor/search}
    (keys/alphanum-mapping editor/insert)
    (keys/symbols-mapping editor/insert)))

(defn run
  [filepath]
  (if (editor/get-buffer filepath)
    (editor/switch-to-buffer filepath)
    (let [syntaxhl (cond (nil? filepath) (editor/get-default-highlighter)
                         (re-matches #"^.*\.js$" filepath) javascripthl/next-face
                         (re-matches #"^.*\.java$" filepath) javascripthl/next-face
                         (re-matches #"^.*\.c$" filepath) javascripthl/next-face
                         (re-matches #"^.*\.py$" filepath) pythonhl/next-face
                         (re-matches #"^.*\.xml$" filepath) xmlhl/next-face
                         (re-matches #"^.*\.tex$" filepath) latexhl/next-face
                          :else (editor/get-default-highlighter)) ;; In other cases use clojure/markdown
          ]
      (editor/create-buffer-from-file filepath)
      (editor/set-keymap @keymap-normal)
      (editor/set-highlighter syntaxhl))))
