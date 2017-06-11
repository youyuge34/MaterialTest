## 一、项目简介项目实现一种基于MVP软件架构的安卓客户端端App——《映象南京》。采用android stdiov2.3开发，兼容支持android  4.0到6.0版本的手机。本软件突破了一贯的MVC架构，使用了耦合性更低，内聚性更高的MVP架构，将Model模型层与View显示层相剥离，并在中间加入了presenter层，使用presenter作为中间代理人来处理M层与V层的交互，使得M层和V层完全没有耦合。首页列表信息的服务器使用了七牛云的对象存储，登陆云端功能使用了leancloud后端云。网络模块使用了okhttp+retrofit2+rxjava的封装，显示模块使用了viewpage+recyclerView+图片轮播器，二维码功能使用了zxing-android库，整个app在github上开源，地址为https://github.com/youyuge34/MaterialTest，任何人都可以参与进app的维护与更新。整个项目代码行数在5500行左右。
## 二、关键技术项目中应用的关键技术：MVP架构设计，retrofit2+rxjava网络模块，leancloud后端云，zxing二维码技术，侧滑返回功能，注册登录与上传功能。## 三、主要功能1. 首页可查看南京的街道、地名、景点的列表信息；2. 首页列表点击后可查看详情；3. 注册与登录功能；4. 扫一扫与从相册识别；5. 制作自己的二维码，并上传到云端；6. 在云端浏览所有人制作的二维码；## 四、创新点1. 加入了注册登录功能，用户能与其他用户进行交互；2. 首页采用三个页面组成，可快速滑动浏览三个不同栏目；3. UI界面风格设计统一，全部为原创设计；4. 二维码内容依托于网络云端，可在生成之后更改内容；5. 二维码与软件相结合，能更广泛地宣传并传承南京的路名、地名文化。本应用界面美观，操作流畅，若是推广出去肯定会大大推动南京的路名、地名的文化传承以及保护工作。
## 五、预览图
- 主界面

![image.png](http://upload-images.jianshu.io/upload_images/3251332-fec3babfd446f540.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 侧滑菜单

![image.png](http://upload-images.jianshu.io/upload_images/3251332-624d0772caba2487.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


- 点击后详情



![image.png](http://upload-images.jianshu.io/upload_images/3251332-b05dda031ebee325.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![image.png](http://upload-images.jianshu.io/upload_images/3251332-4be4ef912b093205.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 云端



![image.png](http://upload-images.jianshu.io/upload_images/3251332-71f69ddada31df17.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 扫一扫



![image.png](http://upload-images.jianshu.io/upload_images/3251332-9f1bf366f6007b3e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 生成我的二维码



![image.png](http://upload-images.jianshu.io/upload_images/3251332-ab36863653a1c78c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
