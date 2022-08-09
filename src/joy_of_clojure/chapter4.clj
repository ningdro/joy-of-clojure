(ns joy-of-clojure.chapter4)

; 4 标量

; 4.1.1 截断
; 后缀字符M，用来声明这个值需要采用任意精度的表现形式
; 这种做法也不能保证准确无误的精度
(let [imadeuapi 3.141592611111111111111111111111111111111111111111111111111M]
  (println imadeuapi)
  imadeuapi)
;3.141592611111111111111111111111111111111111111111111111111M
;=> 3.141592611111111111111111111111111111111111111111111111111M
(let [imadeuapi 3.141592611111111111111111111111111111111111111111111111111]
  (println imadeuapi)
  imadeuapi)
;3.141592611111111
;=> 3.141592611111111

; 4.1.2 提升
; 发生上溢，clojure能够检测到，将值提升成容纳更大值的数字类型
(def clueless 9)
(class clueless)
;=> java.lang.Long
(class (+ clueless 10000000000000))
;=> java.lang.Long
(class (+ clueless 10000000000000000000000000))
;=> clojure.lang.BigInt
(class (+ clueless 9.0))
;=> java.lang.Double

; 4.1.3 上溢
(+ Long/MAX_VALUE Long/MAX_VALUE)
;Execution error (ArithmeticException) at java.lang.Math/addExact (Math.java:898). long overflow
; 只有上溢是预期行为，才使用
(unchecked-add (Long/MAX_VALUE) (Long/MAX_VALUE))
;=> -2

; 4.1.4 下溢
; 同样需要格外注意，危险性与上溢相同
(float 0.000000000000000000000000000000000000000000000000000001)
;=> 0.0

; 4.1.5 舍入错误
; 浮点值类型不足以存储实际值时
; 爱国者导弹
(let [approx-interval (/ 209715 2097152)
      actual-interval (/ 1 10)
      hours (* 3600 100 10)
      actual-total (double (* hours actual-interval))
      approx-total (double (* hours approx-interval))]
  (- actual-total approx-total))
;=> 0.34332275390625
; clojure能保持精度，当与java交互时，不能保证
; 在运算中引入 double、float，通常会带来舍入错误
(+ 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M)
;=> 1.2M
(+ 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M 0.1)
;=> 1.2000000000000002

; 4.2 有理数
; 核心是一组有理数运算的数学函数
(def a 1.0e50)
(def b -1.0e50)
(def c 17)
(+ (+ a b) c)
;=> 17.0
(+ a (+ b c))
;=> 0.0

; 4.2.2 怎样才是有理数
; 保证计算尽可能精确的最好方式是确保所有内容都是以有理数完成
(def a (rationalize 1.0e50))
(def b (rationalize -1.0e50))
(def c (rationalize 17))
(+ (+ a b) c)
;=> 17N
(+ a (+ b c))
;=> 17N

; 检查是否为有理数
(rational? 1.2)
;=> false
; 转换为有理数
(rationalize 1.2)
;=> 6/5

; 绝对不要使用Java math库
; 不要用 rationalize 处理 java float 和 double 的原生值
; 高精度计算，使用有理数

(numerator (rationalize 1.2))
;=> 6
(denominator (rationalize 1.2))
;=> 5
(rationalize 1.2)
;=> 6/5

; 使用有理数，带来了速度的牺牲

; 4.3 关键字

; 4.3.1 关键字的应用
; 关键字的值总是指向自身
; 符号可能指向任何合法的clojure值或引用

;  作为map的key
(def p {:k 212})
; 做函数
(:k p)
; 做枚举
; :small :medium : large
; 做多重方法的分发值
; 做指令

; 4.3.2 限定关键字
; 关键字不属于任何特定的命名空间
; 当使用两个冒号，clojure假定使用者希望有限定名或者有前缀的关键字

; 4.4 符号解析
(identical? 'goat 'goat)
;=> false
(= 'goat 'goat)
;=> true

; 4.4.1 元数据
; 同名符号通常是不同的实例，每个实例都有自己的元数据
(let [x (with-meta 'goat {:a true})
      y (with-meta 'goat {:a false})]
  [(= x y) (identical? x y) (meta x) (meta y)])
;=> [true false {:a true} {:a false}]

; 4.4.2 符号与命名空间
; 符号也不属于任何特定的命名空间
;(ns where-is)
;=> nil
(def a-symbol 'where-am-i)
a-symbol
;=> where-am-i
(resolve 'a-symbol)
;=> #'where-is/a-symbol
`a-symbol
;=> where-is/a-symbol
; 看上去像一个命名空间限定的符号，因为符号限定是求值的特征，不是符号的属性

; 4.5 正则表达式
; 不要使用re-groups、re-find、re-matcher，这些是非线程安全
; 使用clojure.contrib.string下的函数
