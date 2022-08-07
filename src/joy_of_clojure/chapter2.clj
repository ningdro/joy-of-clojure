(ns joy-of-clojure.chapter2)


(defn print-down-form
  "尾递归recur，recur里的表达式都是先执行，然后绑定到函数参数上"
  [x]
  (when (pos? x)
    (println x)
    (recur (dec x))))

; 使用when
; 不需要else部分
; 需要隐式的do，依次执行有副作用的操作
; 除以上两条，可以使用if


; recur 总会循环回最近的外部loop或fn
; recur 只能出现在函数或者fn的结尾位置(结尾位置：即可以作为整个表达式值的位置)
(defn sum-down-form
  ([sum x]
   (if (pos? x)
     (recur (+ sum x) (dec x))
     sum))
  ([init-x]
   (loop [sum 0
          x init-x]
     (if (pos? x)
       (recur (+ sum x) (dec x))
       sum))))

; 2.7 quote
; 2.7.2 quote阻止其实参及其所有子form求值
(def age 9)
(quote age)
(quote (cons 1 [2 3]))

; 注意区分下面的区别
(cons 1 [2 3])
; vs
;(cons 1 (2 3))
; vs
(cons 1 (quote (2 3)))
; vs quote的另一种语法 单引号
(cons 1 '(2 3))

; 语法quote，反引号`
; 符号自动限定
`map
; => clojure.core/map

; 2.7.3 反quote
; 在quote中添加~，反quote告诉clojure，所标记的form需要求值
`(+ 10 (* 2 3))
;=> (clojure.core/+ 10 (clojure.core/* 2 3))
`(+ 10 ~(* 2 3))
;=> (clojure.core/+ 10 6)

; 2.7.4 反quote拼接
(let [x '(2 3)]
  `(1 ~@x))
;=> (1 2 3)
;~@中的@告诉clojure，不要解开序列x

; 2.7.5 auto-gensym
; 未限定符号：在符号名后添加#
`position#
;=> position__2000__auto__

; 2.8 与java互操作

; 2.8.1 访问静态类成员
; clojure不止可以寄宿与jvm，还可以寄宿与python、.Net、Lua等
java.util.Locale/JAPAN
;=> #object[java.util.Locale 0x4aab55e8 "ja_JP"]

; 默认情况下，java.lang包下的所有类都可以直接使用
(java.lang.Math/sqrt 3)
;=> 1.7320508075688772
(Math/sqrt 3)
;=> 1.7320508075688772

; 2.8.2 创建java实例

(new java.awt.Point 0 1)
;=> #object[java.awt.Point 0x4f7cc8f0 "java.awt.Point[x=0,y=1]"]

; clojure 的集合类可以用于初始化java的集合类
(new java.util.HashMap {"foo" 42 "bar" 9})
;=> {"bar" 9, "foo" 42}
; 更常用的方式为
(java.util.HashMap. {"foo" 42 "bar" 9})
;=> {"bar" 9, "foo" 42}
; 类名后加.表示调用构造函数

; 2.8.3 用.访问Java实例成员
; 访问public变量
(.-x (java.awt.Point. 10 20))
;=> 10

; 访问实例属性，只需要在属性或方法名前加一个.

; 2.8.4 设置Java实例属性
(let [origin (java.awt.Point. 0 0)]
  (set! (.-x origin) 15)
  (str origin))
;=> "java.awt.Point[x=15,y=0]"

; 2.8.5 ..宏
; ..的作用与->、->>的作用类似
; java的链式调用a.b().c(arg).d() => (.. a b (c arg) d)

; 2.8.6 doto宏
; 对java写法的一种简化形式
; java.util.HashMap props = new java.util.HashMap();
; props.put("HOME", "/home/me");
; props.put("SRC", "src");
; clojure:
(doto (java.util.HashMap.)
  (.put "HOME" "/home/me")
  (.put "SRC" "src"))
; 编译后，clojure的代码和java的一样，只是代码写法层面简化

; 2.9 异常
; 抛出异常
(defn throw-exception
  []
  (throw (Exception. "I done throwed")))
; 捕获异常
(defn throw-catch
  [f]
  (try
    (f)
    (catch IllegalAccessError e (str "Error comment" (.getMessage e)))
    (finally (println "return"))))

; 2.10 命名空间

; 2.10.1 创建命名空间，使用ns
; *ns*是clojure提供的一个变量，其值为当前的命名空间名称

; 2.10.2 :require加载其他命名空间

; 2.10.3 :refer加载、创建映射 （:require指令的:refer选项）
; 类似python中的from ... import ...
; :rename :use

; 2.10.4 :refer创建映射
;(ns joy-of-clojure.yet-another
;  (:refer clojure.set :rename {union onino}))
;(onino #{1 2} #{4 5})

; 2.10.5 :import加载java类
;(ns joy-of-clojure.java
;  (:import [java.util.HashMap]))

