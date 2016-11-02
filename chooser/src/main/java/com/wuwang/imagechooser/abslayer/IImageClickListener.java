/*
 *
 * IImageClickListener.java
 * 
 * Created by Wuwang on 2016/11/2
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.abslayer;

import com.wuwang.imagechooser.album.ImageInfo;

import java.util.List;

/**
 * Description:
 */
public interface IImageClickListener {

    //返回值为是否阻止默认操作，默认会增加选中
    boolean onAdd(List<ImageInfo> data,ImageInfo info);
    //返回值为是否阻止默认操作，默认会取消选中
    boolean onCancel(List<ImageInfo> data,ImageInfo info);

}
