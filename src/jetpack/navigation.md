1.NavDestination 代表 目标页面，可以理解为要跳转的页面对象，有几个主要的字段navigatorName 导航name;idName action当前页面有哪些行为，这些行为是可以跳转到哪里或者是哪个页面
arguments 是当前页面进来的时候，需要什么参数；deepLinks 可以理解为使用uri的形式跳转到当前页面。它的子类有四个，NavGraph（路由的主页，里面包含整个页面跳转的路由页面，使用容器保存所有的页面）；
ActivityNavigator.Destination（代表activity的目标页面），FragmentNavigator.Destination（代表Fragment的目标页面）;DialogFragmentNavigator.Destination（代表Dialog的目标页面）

2.Navigator<D : NavDestination> 导航器对象，主要功能有createDestination()（创建目标页对象） navigate()跳转到哪里
子类有五个，DialogFragmentNavigator，ActivityNavigator，FragmentNavigator  NavGraphNavigator（主页导航器），NoOpNavigator（仅支持创建目标对象的导航器）

3.整个导航的核心主要是NavHostFragment,大体执行方法：
onInflate
    主要获取界面的关键字段defaultNavHost和graphId
    defaultNavHost 是否跟随系统返回栈
    graphId 代表navGraph的xml资源，里面保存了路由的跳转逻辑及各个字段

onCreate
创建NavhostFragment对象，该对象是路由的一个容器对象，他自身创建了frameLayout，用于展示主页的内容及切换不同的view内容，也是一个起点，本质是一个fragment，

    NavHostController(context) 页面跳转控制器
        继承 NavController(context)，创建该对象时,在init方法创建了NavGraphNavigator和ActivityNavigator，并加入到容器里_navigators: MutableMap<String, Navigator<out NavDestination>>，
        string是各个Navigator的注解名，如NavGraphNavigator的注解name 为navigation，同时value为navigator的实现类，且传入泛型destination目标对象。

    onCreateNavHostController
        创建了DialogFragmentNavigator和FragmentNavigator，并加入到容器里_navigators

    navHostController!!.setGraph(graphId)
        此处是重点，graphId为mobile_navigation.xml，先解析该xml，执行了navInflater.inflate(graphResId)
        执行inflate(res, parser, attrs, graphResId)，内部执行val navigator = navigatorProvider.getNavigator<Navigator<*>>(parser.name)
                                                          val dest = navigator.createDestination()
                                                          dest.onInflate(context, attrs)
        通过查看mobile_navigation.xml，可以肯定第一次parser.name肯定为navigation，获取到NavGraphNavigator，然后createDestination，再onInflate,这时候一定要进入
        子类查看NavGraph对象的onInflate的方法，获取了startDestinationId（起始页的id）  startDestIdName(起始页的名称)
        然后进入 if (dest is NavGraph) ，再进入inflate（递归）,深度+1，解析里面的标签，获取一个destination对象，加入到NavGraphDestination对象里，这样起始页的数据就准备好了

        然后回到setGraph里，执行  onGraphCreated(startDestinationArgs)--》  navigate(_graph!!, startDestinationArgs, null, null)
        navigator.navigateInternal()  -->  navigate(entries, navOptions, navigatorExtras),这个方法就要进入子类了，也就是NavgraphNavigator

         for (entry in entries) {
                    navigate(entry, navOptions, navigatorExtras)
                }
         进入navigator.navigate(listOf(startDestinationEntry), navOptions, navigatorExtras)，此处进入子类FragmentNavigator

           for (entry in entries) {
                       navigate(entry, navOptions, navigatorExtras)
                   }
         进入 createFragmentTransaction， 读取目标的数据，动画，参数，className ，通过className 执行 val frag = fragmentManager.fragmentFactory.instantiate(context.classLoader, className)
         构建fragment对象，然后commit，把fragment的展示出来

onCreateView  创建一个FrameLayout 用于fragment展示