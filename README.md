排版地址：https://blog.csdn.net/long8313002/article/details/108422991



使用指南：

build.gradle

implementation "com.zhangzheng.crashProtect:crashprotect:0.0.1"
Application

class MyApplication :Application(){
 
 
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
 
 
        CrashProtect.protectApp(this)
    }
 
}
 

 

 

 

 

 
概述
        当异常产生时，通常我们有两种处理方案，第一种是对它进行捕获，做兼容处理，这种情况需要针对不同的业务具体情况具体分析，另外一种就是直接抛出到虚拟机，杀死整个APP的进程。但是，不管哪一种都会有弊端，第一种会导致开发成本变高、代码变得不够简洁、降低了可读性（有人说try..catch会影响性能，实际测试中微乎其微，不过可能会影响到虚拟机对代码的优化）。另外一种呢，肯定就是影响用户体验了。

 

        那有没有一种方法，可以在两者之间，即不会导致代码质量下降、又可以提高用户体验呢？事实上基于android运行机制，我们一起来讨论一种基于页面crash的保护方案。概述的说：当页面发生异常，整个应用不会退出，而仅仅是当前页面进行退出。

 

 

 
页面异常分类
 

 
Activity启动异常
          activity的启动到生命周期的执行，这部分流程由系统进行控制。哪怕我们在startActivity进行try...catch也没有任何用处（startActivity仅仅只是发起了请求到ActivityManagerService，AMS处理完请求后会再通知app进程）。当这种情况发生了，毋庸置疑我们需要关闭异常页面

 

 
页面事件异常
 

       这个最常见的就是视图的操作事件，比如按钮的点击事件、按道理说如果点击事件中发生了异常，我们仅仅只需要忽略这个异常，当什么都没发生就可以了。但其实这样做是有些冒险的，思考下，如果流程执行到点击事件的一半才发生异常，之前的流程已经改变了页面的状态，那之后的流程将会变的不可预测，如果真的这样做了，将会对线上问题排查造成影响。所以采用的异常策略是：关闭问题页面

 

 
页面线程异常
        页面发起的线程，因为多线程本身就需要考虑安全问题，所以“页面事件异常”中遇到的问题，这里可以不用考虑。所以，对于页面线程异常，我们的策略是忽略

 

 

 
补充
        不仅仅是针对上面的几种情况，可能会有其他逻辑的执行（比如：系统自发的逻辑、广播、延迟任务），我们需要对异常进行兜底处理：

 

       1、判断异常是发生在用户代码层面、而不是系统代码（异常中断在系统代码，会导致整个系统处于不稳定状态，这种情况需要立刻终止APP）

 

       2、匹配异常发生的任务栈，发现和栈顶Activity不匹配，则立刻终止APP（如果由中间的某个Activity发生的异常，比如延迟任务导致的，这种情况下仅仅结束这个Activity会非常奇怪）

 

 

 
核心代码
 

 
事件异常
 



 

         核心代码其实就是Looper.loop()，android系统是基于事件驱动运行起来的，本质上来说相当于一个死循环，我比较喜欢把这个机制叫做“引擎”。我简化了loop()的代码，只保留了核心逻辑，非常简单，大家一起看下：

 



 

             典型的生产消费者模型，当没有事件的时候会进行阻塞，有事件过来会交给Handler来进行处理（大家可以连着Handler+MessageQueue+Looper一起看）。

 

         当发生crash时，loop()发生异常，会退出死循环的。所以我做的就是捕获异常，并重新让它转起来！我为了对异常保护流程进行控制，所以使用的是递归来让它重新运行。

 

 

 
启动异常
 



 

        这部分的实现是对Instrumentation进行代理替换，牵扯到知识点主要的Activity启动流程这块，如果不熟悉的话，大家可以找资料先了解下。代理类如下：

 



 

       截取了部分，详细的大家可以看源码，就是使用try..catch进行保护，没有什么特别的地方。不过因为Android P以后对系统API进行了限制，所以采用了元反射进行处理。

 

 

 
线程异常


 

         这个方法很常用了，全部捕获系统异常的方法，会针对线程进行区分，子线程进行忽略。不过，如果线程的异常最后发生在系统代码中，则杀死自己
