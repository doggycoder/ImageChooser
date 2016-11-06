/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wuwang.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A standard toolbar for use within application content.
 *
 * <p>A Toolbar is a generalization of {@link ActionBar action bars} for use
 * within application layouts. While an action bar is traditionally part of an
 * {@link android.app.Activity Activity's} opaque window decor controlled by the framework,
 * a Toolbar may be placed at any arbitrary level of nesting within a view hierarchy.
 * An application may choose to designate a Toolbar as the action bar for an Activity
 * using the {@link android.support.v7.app.AppCompatActivity#setSupportActionBar(Toolbar)
 * setSupportActionBar()} method.</p>
 *
 * <p>Toolbar supports a more focused feature set than ActionBar. From start to end, a toolbar
 * may contain a combination of the following optional elements:
 *
 * <ul>
 *     <li><em>A navigation button.</em> This may be an Up arrow, navigation menu toggle, close,
 *     collapse, done or another glyph of the app's choosing. This button should always be used
 *     to access other navigational destinations within the container of the Toolbar and
 *     its signified content or otherwise leave the current context signified by the Toolbar.
 *     The navigation button is vertically aligned within the Toolbar's minimum height,
 *     if set.</li>
 *     <li><em>A branded logo image.</em> This may extend to the height of the bar and can be
 *     arbitrarily wide.</li>
 *     <li><em>A title and subtitle.</em> The title should be a signpost for the Toolbar's current
 *     position in the navigation hierarchy and the content contained there. The subtitle,
 *     if present should indicate any extended information about the current content.
 *     If an app uses a logo image it should strongly consider omitting a title and subtitle.</li>
 *     <li><em>One or more custom views.</em> The application may add arbitrary child views
 *     to the Toolbar. They will appear at this position within the layout. If a child view's
 *     {@link LayoutParams} indicates a {@link Gravity} value of
 *     {@link Gravity#CENTER_HORIZONTAL CENTER_HORIZONTAL} the view will attempt to center
 *     within the available space remaining in the Toolbar after all other elements have been
 *     measured.</li>
 *     <li><em>An {@link ActionMenuView action menu}.</em> The menu of actions will pin to the
 *     end of the Toolbar offering a few
 *     <a href="http://developer.android.com/design/patterns/actionbar.html#ActionButtons">
 *     frequent, important or typical</a> actions along with an optional overflow menu for
 *     additional actions. Action buttons are vertically aligned within the Toolbar's
 *     minimum height, if set.</li>
 * </ul>
 * </p>
 *
 * <p>In modern Android UIs developers should lean more on a visually distinct color scheme for
 * toolbars than on their application icon. The use of application icon plus title as a standard
 * layout is discouraged on API 21 devices and newer.</p>
 *
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_buttonGravity
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_collapseContentDescription
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_collapseIcon
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetEnd
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetLeft
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetRight
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetStart
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetStartWithNavigation
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetEndWithActions
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_android_gravity
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_logo
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_logoDescription
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_maxButtonHeight
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_navigationContentDescription
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_navigationIcon
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_popupTheme
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_subtitle
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_subtitleTextAppearance
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_subtitleTextColor
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_title
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_titleMargin
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginBottom
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginEnd
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginStart
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginTop
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_titleTextAppearance
 * @attr ref android.support.v7.appcompat.R.styleable#Toolbar_titleTextColor
 */
public class ToolBar extends Toolbar {

    private TextView mTitleTextView;
    private TextView mSubtitleTextView;

    public ToolBar(Context context) {
        super(context);
    }

    public ToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTitle(CharSequence title) {

    }

}
