### 学习进度：
暂时还停留在work3，还没有进行bonus部分的学习，不过也即将开始进行。在代码过程中主要问题还是出现在，并没有捋清楚自己需要的完整内容，而是完成一部分，发现整体问题后，又重新进行部分代码的编辑，耗时耗力。
### 项目内容：
#### 技术栈：
无

#### 项目目录结构介绍：
**lib中包含**：mysql的connector

**src目录**：
1. Good类，Order类
2. GoodDaoOrder的Dao类（主要用来封装对数据库的新增，删除，查询，修改的数据访问层）
3. OrderService类（服务层，相比Dao较高层次，将多种方法封装起来）
4. Test类（测试类）
5. jabcUtil包（包括数据库的连接，创建sql命令，释放连接，更新，查找，以及事务的开启，提交，回滚，相当于工具的包，封装一些实用的方法和数据结构）
6. dp.properties（配置文件，为数据库的连接提供数据）

#### 项目功能介绍：
**数据访问层：**
1. ***outGoods()***:该方法作用为 从初始数据库中导出物品信息
2. ***outOrders(ArrayList<Good> list)***:该方法作用为 从初始数据库中导出订单信息(根据数据库Order中的外键goodId,在outGoods方法得到的初始商品堆ArrayList<Good> list中寻找订单中的商品有什么)
3. ***insertGood(ArrayList<Good> list, String name, int price)***:该方法作用为 向商品堆中加入新的商品(name为店家Scanner输入导入商品的名称,price为店家Scanner输入导入商品的价格,新增商品的id会根据数据库中MAX(good_id)+1而下最大id数后向下递增;然后运用insert将商品插入到数据库中)
4. ***deleteGood(ArrayList<Good> list, String name)***:该方法作用为 向商品堆中删除商品(name为店家Scanner输入需要删除的商品名称,通过循环在list中寻找含有name的一项所对应的索引,通过remove()方法,将list中索引位置的删除;然后运用delete删除在数据库中Good中相同的商品)
5. ***insertOrder(ArrayList<Good> list, ArrayList<Order> orders)***:该方法作用为 向订单堆中添加新的订单(用sql中MAX(order_id)获取最大订单号id,为了插入时订单号可以向下延,续通过循环不断地获取想要购买的商品,最后通过输入'e'退出选购)
6. ***queryOneOrder(int id)***:该方法作用为 查询单个订单(通过Scanner获取订单id,通过联表查询的方法,同时获取全部的订单和商品的信息)
7. ***queryOrder()***:该方法作用为 查询全部订单(通过联表查询的方法,同时获取全部的订单和商品的信息)
8. ***insertOrderGood(ArrayList<Order> orders, ArrayList<Good> goods, int id, String name)***:该方法作用为 增添某订单中的商品(通过商品名name在商品堆中找到商品,通过订单id在订单堆中找到订单,向该订单中添加该商品,添加完订单中的商品后,更新价格)
9. ***deleteOrderGood(ArrayList<Order> orders, int id, String name)***:该方法作用为 删除某订单中商品(通过商品名name在商品堆中找到商品,通过订单id在订单堆中找到订单,在该订单中删除该商品,删除完订单中的商品后,更新价格)
10. ***updatePrice(ArrayList<Order> orders)***:该方法作用为 更新订单中价格(将各个订单中的价格重新相加,刷新)

#### 项目亮点:
无亮点,纯基础,打牢了未来才能有亮点

#### 项目待改进点:

#### 项目如何启动:
