(ns joy-of-clojure.chapter3)

; 3.1 真值
; and or when 是构建在 if 之上的宏

; 3.1.1 什么是真
; 对clojure来说，除了false与nil，其余都是真

; 3.1.2 不要创建布尔对象
; clojure的true和false，分别是java的Boolean/TRUE、Boolean/FALSE
; Boolean.方式的构造方法已被java弃用，应使用parseBoolean()或者valueOf()
(def evil-false (Boolean. "false"))
evil-false
;=> false
(= false evil-false)
;=> true
(if evil-false 1 2)
;=> 1
(if (Boolean/valueOf "false") 1 2)
;=> 2
(if (Boolean/parseBoolean "false") 1 2)
;=> 2

; 3.1.3 nil vs false
; 区分 nil 与 false，可以使用 nil? / false?

; 3.2 nil双关
; 用 seq 做终止条件检测序列是否为空时惯用的方式
; 如果检查的只是 s 而非 (seq s)，出现空集合时，将会无限循环
(defn print-seq
  [s]
  (when (seq s)
    (println (first s))
    (recur (rest s))))
; rest 只会返回空或非空的sequence，不会返回nil
; next <==> (seq (rest s))
(next [1 2 3])
;=> (2 3)
(seq (rest [1 2 3]))
;=> (2 3)

(print-seq [1 2 3])
;1
;2
;3
;=> nil
(print-seq [])
;=> nil

; ※ 空集合与false值是有差别的

; 3.3 解构

; 3.3.2 解构vector
(def guys-whole-name ["Guy" "Lewis" "Steele"])
(let [[f-name m-name l-name] guys-whole-name]
  (str l-name "," f-name, "," m-name))
;=> "Steele,Guy,Lewis"
; 按位置解构
; 任何实现了CharSequence和java.util.RandomAccess接口的东西，位置解构都可以起作用
(def date-regex #"(\d{1,2}\/(\d{1,2})\/(\d{4}))")
(let [rem (re-matcher date-regex "12/02/1975")]
  (when (.find rem)
    (let [[_ m d] rem]
      {:month m :day d})))
;=> {:month "12/02/1975", :day "02"}
(let [[a b & more] (range 10)]
  (println a b)
  (println more))
;0 1
;(2 3 4 5 6 7 8 9)
;=> nil
(let [[a b & more :as all] (range 10)]
  (println a b)
  (println more)
  (println all))
;0 1
;(2 3 4 5 6 7 8 9)
;(0 1 2 3 4 5 6 7 8 9)
;=> nil

; 3.3.3 解构map
(def guys-name-map
  {:f-name "Guy" :m-name "Lewis" :l-name "Steele"})

; 普通解构
(let [{f-name :f-name
       m-name :m-name
       l-name :l-name} guys-name-map]
  (str l-name "," f-name, "," m-name))

; 使用:keys
(let [{:keys [f-name m-name l-name]} guys-name-map]
  (str l-name "," f-name, "," m-name))

; 使用:as
(let [{:keys [f-name m-name l-name] :as all} guys-name-map]
  (println all)
  (str l-name "," f-name, "," m-name))

; 不存在的key，可以使用:or提供默认值
(let [{:keys [title f-name m-name l-name] :or {title "Mr."}} guys-name-map]
  (str title l-name "," f-name, "," m-name))

; 关联解构
(let [{first-thing 0 last-thing 3} [1 2 3 4]]
  [first-thing last-thing])
;=> [1 4]


