# IDouban
不完全模仿 豆瓣App中书影音信息展示。

目的是 熟练使用Android支持包中的控件以及成熟的网络请求框架. 并对其中的RecyclerView进行重构。

##Android支持包新增控件
- RecyclerView
- CardView
- CoordinatorLayout
- AppBarLayout
- Toolbar
具体可以参看相关源码。

##网络请求框架
- okHttp
- Retrofit
一开始使用okHttp， 后来选择Retrofit （Retrofit + RxJava已实现未提交）。

网络数据来源于豆瓣的公开接口：

[豆瓣电影V2](https://developers.douban.com/wiki/?title=movie_v2)

[豆瓣书籍V2](https://developers.douban.com/wiki/?title=book_v2)

# 代码说明
IDouban中的代码在我的[简书](http://www.jianshu.com/p/98399b00ae78)上有详细的说明。
分解了8篇文章， 一步步讲解如何实现IDouban Demo。

如有想跟随编写足迹来阅读文章的同学可以`git clone`后再`git reset`到特定`commit`。 同时也提供 `git tag`方式获取代码。

到07.29为止，发布了3个release
- release_01 ，实现电影&数据展示功能
- release_02 ，重构RecyclerView相关的Adapter， 抽象成Adapter
- release_03， 重构RecyclerView相关的ViewHolder， 抽象成BaseViewHolder， BaseAdapter

欢迎在issue or 简书上留言， 觉得好，点个赞，加颗星！
