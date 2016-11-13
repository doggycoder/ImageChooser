# 说明
项目功能为图片选择器，当前已有功能及后续计划见更新说明，如有其它建议和意见，请mail至1558183202@qq.com。
围绕此项目写的相关博客：[相册基本实现](http://blog.csdn.net/junzia/article/details/53091606)、[图片裁剪](http://blog.csdn.net/junzia/article/details/52950210)
## 简单使用示例
如需使用图片选择器功能，将chooser加入为依赖工程，然后使用指定功能：
### 选择单张图片
如果需要选择单张图片，调用：
```java
Intent intent=new Intent(IcFinal.ACTION_ALBUM);
intent.putExtra(IcFinal.INTENT_MAX_IMG,1);  
startActivityForResult(intent,1);
```
### 选择多张图片
```java
Intent intent=new Intent(IcFinal.ACTION_ALBUM);
intent.putExtra(IcFinal.INTENT_MAX_IMG,9);
startActivityForResult(intent,1);
```
### 裁剪
如果需要选择单张图片并且裁剪，调用：
```java
Intent intent=new Intent(IcFinal.ACTION_ALBUM);
intent.putExtra(IcFinal.INTENT_IS_CROP,true);
startActivityForResult(intent,1);
```
默认为圆形图片，大小为500*500。如果需要自行设定，给intent增加以下参数：
```java
intent.putExtra(IcFinal.INTENT_CROP_SHAPE,CropPath.SHAPE_RECT);   //矩形，默认为CropPath.SHAPE_CIRCEL圆形
intent.putExtra(IcFinal.INTENT_CROP_WIDTH,512); //裁剪宽度
intent.putExtra(IcFinal.INTENT_CROP_HEIGHT,280); //裁剪高度
```
或者，你要的是其他不规则的形状，那么你可以选择继承ACropCoverDrawable类，自己来控制你需要的裁剪形状，然后使用以下代码来启动裁剪：
```java
Intent intent=new Intent(IcFinal.ACTION_ALBUM);
intent.putExtra(IcFinal.INTENT_IS_CROP,true);
intent.putExtra(IcFinal.INTENT_CROP_COVER,ExampleCropPath.class.getName());    
intent.putExtra(IcFinal.INTENT_CROP_PARAM,1);   //参数
startActivityForResult(intent,1);
```
你所实现的ACropCoverDrawable类，只能使用一个int参数来控制它的反射出来的实体。其实一个int已经可以做很多事了。

## 更多设置
也许图片选择器默认的UI不符合你的要求，你可以选择利用ChooserSetting中的静态参数来更改图片选择器的效果：
```java
/**标题的背景颜色*/
public static int TITLE_COLOR=0xFF584512;
/**图片选择页，每行显示数*/
public static int NUM_COLUMNS=3;
/**图片加载失败的图片*/
public static int errorResId=0;
/**图片加载的占位图片*/
public static int placeResId=R.mipmap.image_chooser_placeholder;
/**图片加载的动画*/
public static int loadAnimateResId=0;
/**选中图片的滤镜颜色*/
public static int chooseFilter=0x55000000;
/**未被选中的图片的滤镜颜色*/
public static int unChooseFilter=0;
/**最新的图片集合显示名字*/
public static String newestAlbumName="最新图片";
/**最新图片集合的最大数量*/
public static int newestAlbumSize=100;
public static int albumPopupHeight=600;
public static String tantoToast="";
/**照片选择指示器*/
public static IChooseDrawable chooseDrawable=new CircleChooseDrawable(true,0xFF25c2e6);
```
如果这些也无法满足你的UI要求，你也可以参照EntryActivity重新写相册的入口Activity，参照CropActivity重写裁剪的入口Activity。

# 更新说明

## 已完成功能
1. 图片单选多选功能
2. 多图选择指示器
3. 图片裁剪功能
4. 拍照功能
5. 裁剪形状的完全定制

## 待完成工作
1. ~~图片单选多选~~
2. ~~多图选择指示器~~
3. ~~图片裁剪~~
4. ~~裁剪形状的完全定制~~
4. 多图选择时的大图浏览
5. ~~拍照功能~~
6. UI让使用者可高度定制
7. 加入到jcenter和maven
8. 拍照的美颜及其他滤镜

## 20161111更新
基本完成相册选择、拍照、裁剪功能

## 20161113更新
实现裁剪形状的完全定制入口
