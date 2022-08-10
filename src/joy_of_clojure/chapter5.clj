(ns joy-of-clojure.chapter5)

; 5 组合数据类型

; 5.2.1 vector

(vec (range 10))
;=> [0 1 2 3 4 5 6 7 8 9]

(let [v [:a :b :c]]
  (into v [1 2 3]))
;=> [:a :b :c 1 2 3]

; 原生类型 vector
; :int :long :float :double :byte :short : boolean :char
(into (vector-of :int) [Math/PI 2 1.3])
;=> [3 2 1]
(into (vector-of :char) [100 101 102 65])
;=> [\d \e \f \A]
(into (vector-of :int) [100 101 102 65 \A])
;=> [100 101 102 65 65]

; 大vector
; nth可以在常量时间内获取指定索引
(def big-vec (vec (range 10000000)))
(nth big-vec 999999)
;=> 999999
; get 本质上讲vector当作map
(get big-vec 999999)
;=> 999999
(big-vec 999999)
;=> 999999
; 使用vector本身、nth、get效率差不多
; 具体用哪个，需要考虑的地方
; vector本身为nil
(def nil-vec nil)
(nth nil-vec 1)
;=> nil
(get nil-vec 1)
;=> nil
(nil-vec 1)
;Execution error (NullPointerException) at user/eval1993 (form-init13112915209498368412.clj:1).
;Cannot invoke "clojure.lang.IFn.invoke(Object)" because the return value of "clojure.lang.Var.getRawRoot()" is null
; 索引越界
(def out-of-boundary [1 2 3])
(nth out-of-boundary 3)
;Execution error (IndexOutOfBoundsException) at user/eval2001 (form-init13112915209498368412.clj:1).
;null
(get out-of-boundary 3)
;=> nil
(out-of-boundary 3)
;Execution error (IndexOutOfBoundsException) at user/eval2009 (form-init13112915209498368412.clj:1).
;null
; 支持not found
(nth [] 9 "not found")
;=> "not found"
(get [] 9 "not found")
;=> "not found"

(def matrix [[1 2 3]
             [4 5 6]
             [7 8 9]])
; 获取 2行 3列，索引从0开始
(get-in matrix [1 2])
;=> 6
(assoc-in matrix [1 2] 'x)
;=> [[1 2 3] [4 5 x] [7 8 9]]
(update-in matrix [1 2] * 100)
;=> [[1 2 3] [4 5 600] [7 8 9]]

; vector作为栈
; 使用conj不用assoc
; 使用peek不用last
; 使用pop不用dissoc
; 栈的push与pop，对应vector的conj、pop，peek获取栈顶
(def stack [1 2 3 4])
(conj stack 5)
;=> [1 2 3 4 5]
; 栈的pop通常返回最后一个元素，clojure则是返回去掉最后一个元素的sequence
(pop stack)
;=> [1 2 3]
; peek 取栈顶元素，last也能做，但速度慢
(peek stack)
;=> 4
; conj、pop操作的是list的左边
(def stack-list (list 1 2 3 4))
(conj stack-list 5)
;=> (5 1 2 3 4)
(pop stack-list)
;=> (2 3 4)

; 5.2.5 子vector
(def sub-vector [1 2 3 4 5 6])
; 左含右不含
(subvec sub-vector 3 6)
;=> [4 5 6]

; 5.2.7 使用vector需要避免的情况
; 1、vector不是稀疏的，在中间插入，将导致后续的元素需要调整
; 2、vector不是队列
; conj在右端加、rest或next从左端取：rest、next返回的是seq，后续的conj不能如预期执行；使用into转为vector，时间复杂度为O(n)
; 使用subvec做"pop"的操作，但subvec维护的是整个底层vector的引用，该方式弹出的值不会被垃圾回收
; 考虑使用 PersistentQueue


